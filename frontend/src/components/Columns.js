import React, { useState } from 'react';
import {
  Box, Button, Dialog, DialogActions, DialogContent, DialogTitle,
  Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper,
  TextField, MenuItem, Select, InputLabel, FormControl, Checkbox, ListItemText,
  OutlinedInput, IconButton, Typography, Stack
} from '@mui/material';
import { Edit, Delete } from '@mui/icons-material';

const dataTypes = ['String', 'Integer', 'Long', 'Boolean', 'Date'];
const validationConstraintsList = [
  'NOT NULL', 'NOT BLANK', 'EMAIL', 'UNIQUE',
  'MIN=', 'MAX=', 'SIZE=', 'PATTERN='
];
const databaseConstraintsList = ['PRIMARY KEY', 'UNIQUE', 'NOT NULL'];

const Columns = ({ columns, setColumns }) => {
  const [open, setOpen] = useState(false);
  const [editIndex, setEditIndex] = useState(null);
  const [newColumn, setNewColumn] = useState({
    name: '',
    dataType: '',
    validationConstraints: [],
    databaseConstraints: []
  });

  const resetColumnState = () => ({
    name: '',
    dataType: '',
    validationConstraints: [],
    databaseConstraints: []
  });

  const getConstraintValue = (key) => {
    const found = newColumn.validationConstraints.find(c => c.startsWith(key));
    return found ? found.substring(key.length) : '';
  };

  const setConstraintValue = (key, value) => {
    setNewColumn(prev => {
      let filtered = prev.validationConstraints.filter(c => !c.startsWith(key));
      if (value.trim() !== '') {
        filtered = [...filtered, `${key}${value}`];
      }
      return { ...prev, validationConstraints: filtered };
    });
  };

  const handleOpen = (index = null) => {
    if (index !== null) {
      setEditIndex(index);
      setNewColumn(columns[index]);
    } else {
      setEditIndex(null);
      setNewColumn(resetColumnState());
    }
    setOpen(true);
  };

  const handleClose = () => {
    setNewColumn(resetColumnState());
    setEditIndex(null);
    setOpen(false);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setNewColumn(prev => ({ ...prev, [name]: value }));
  };

  const handleValidationConstraintsChange = (event) => {
    const value = event.target.value;
    setNewColumn(prev => ({ ...prev, validationConstraints: value }));
  };

  const handleDatabaseConstraintChange = (event) => {
    const value = event.target.value;
    const newConstraints = typeof value === 'string' ? value.split(',') : value;
    const isAddingPK = newConstraints.includes('PRIMARY KEY');
    const pkExistsElsewhere = columns.some((col, idx) =>
      col.databaseConstraints?.includes('PRIMARY KEY') && idx !== editIndex
    );
    if (isAddingPK && pkExistsElsewhere) {
      alert('Only one column can have PRIMARY KEY.');
      return;
    }
    setNewColumn(prev => ({
      ...prev,
      databaseConstraints: newConstraints
    }));
  };

  const handleAddOrUpdate = () => {
    if (!newColumn.name || !newColumn.dataType) {
      alert('Name and Data Type are required.');
      return;
    }
    if (editIndex !== null) {
      const updated = [...columns];
      updated[editIndex] = newColumn;
      setColumns(updated);
    } else {
      setColumns(prev => [...prev, newColumn]);
    }
    handleClose();
  };

  const handleDelete = (index) => {
    setColumns(prev => prev.filter((_, i) => i !== index));
  };

  return (
    <Box sx={{ padding: 4, marginTop: "1%", bgcolor: '#f9f9f9', borderRadius: 2 }}>
      <Stack direction="row" justifyContent="space-between" alignItems="center" mb={2}>
        <Typography variant="h5">Model</Typography>
        <Button variant="contained" onClick={() => handleOpen()}>Add Column</Button>
      </Stack>

      <Dialog open={open} onClose={handleClose} fullWidth maxWidth="sm">
        <DialogTitle>{editIndex !== null ? 'Edit Column' : 'Add Column'}</DialogTitle>
        <DialogContent
          dividers
          sx={{
            height: 480,
            overflowY: 'auto',
            display: 'flex',
            flexDirection: 'column',
            gap: 2,
          }}
        >
          <TextField
            fullWidth
            margin="dense"
            label="Column Name"
            name="name"
            value={newColumn.name}
            onChange={handleChange}
          />
          <FormControl fullWidth margin="dense">
            <InputLabel>Data Type</InputLabel>
            <Select
              name="dataType"
              value={newColumn.dataType}
              onChange={handleChange}
              input={<OutlinedInput label="Data Type" />}
            >
              {dataTypes.map(type => (
                <MenuItem key={type} value={type}>{type}</MenuItem>
              ))}
            </Select>
          </FormControl>

          <FormControl fullWidth margin="dense">
            <InputLabel>Validation Constraints</InputLabel>
            <Select
              multiple
              value={newColumn.validationConstraints}
              onChange={handleValidationConstraintsChange}
              input={<OutlinedInput label="Validation Constraints" />}
              renderValue={(selected) => selected.join(', ')}
            >
              {validationConstraintsList.map(vc => (
                <MenuItem key={vc} value={vc}>
                  <Checkbox checked={newColumn.validationConstraints.includes(vc)} />
                  <ListItemText primary={vc} />
                </MenuItem>
              ))}
            </Select>
          </FormControl>

          {newColumn.validationConstraints.includes('MIN=') && (
            <Stack direction="row" alignItems="center" spacing={1} sx={{ mb: 1 }}>
              <Typography>MIN</Typography>
              <TextField
                type="number"
                size="small"
                value={getConstraintValue('MIN=')}
                onChange={e => setConstraintValue('MIN=', e.target.value)}
                sx={{ width: 100 }}
              />
            </Stack>
          )}
          {newColumn.validationConstraints.includes('MAX=') && (
            <Stack direction="row" alignItems="center" spacing={1} sx={{ mb: 1 }}>
              <Typography>MAX</Typography>
              <TextField
                type="number"
                size="small"
                value={getConstraintValue('MAX=')}
                onChange={e => setConstraintValue('MAX=', e.target.value)}
                sx={{ width: 100 }}
              />
            </Stack>
          )}
          {newColumn.validationConstraints.includes('SIZE=') && (() => {
            const sizeVal = getConstraintValue('SIZE=');
            const [minSize, maxSize] = sizeVal.includes('-') ? sizeVal.split('-') : ['', ''];
            return (
              <Stack direction="row" alignItems="center" spacing={1} sx={{ mb: 1 }}>
                <Typography>SIZE (min - max)</Typography>
                <TextField
                  type="number"
                  size="small"
                  value={minSize}
                  onChange={e => setConstraintValue('SIZE=', `${e.target.value}-${maxSize}`)}
                  sx={{ width: 70 }}
                />
                <Typography>-</Typography>
                <TextField
                  type="number"
                  size="small"
                  value={maxSize}
                  onChange={e => setConstraintValue('SIZE=', `${minSize}-${e.target.value}`)}
                  sx={{ width: 70 }}
                />
              </Stack>
            );
          })()}
          {newColumn.validationConstraints.includes('PATTERN=') && (
            <Stack direction="row" alignItems="center" spacing={1} sx={{ mb: 1 }}>
              <Typography>Regex Pattern</Typography>
              <TextField
                size="small"
                value={getConstraintValue('PATTERN=')}
                onChange={e => setConstraintValue('PATTERN=', e.target.value)}
                sx={{ flexGrow: 1 }}
                placeholder="Enter regex"
              />
            </Stack>
          )}

          <FormControl fullWidth margin="dense">
            <InputLabel>Database Constraints</InputLabel>
            <Select
              multiple
              value={newColumn.databaseConstraints}
              onChange={handleDatabaseConstraintChange}
              input={<OutlinedInput label="Database Constraints" />}
              renderValue={(selected) => selected.join(', ')}
            >
              {databaseConstraintsList.map(dbConstraint => (
                <MenuItem key={dbConstraint} value={dbConstraint}>
                  <Checkbox checked={newColumn.databaseConstraints.includes(dbConstraint)} />
                  <ListItemText primary={dbConstraint} />
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button variant="contained" onClick={handleAddOrUpdate}>
            {editIndex !== null ? 'Update' : 'Add'}
          </Button>
        </DialogActions>
      </Dialog>

      <TableContainer component={Paper}>
        <Table size="small">
          <TableHead>
            <TableRow>
              <TableCell>Column Name</TableCell>
              <TableCell>Data Type</TableCell>
              <TableCell>Validation Constraints</TableCell>
              <TableCell>Database Constraints</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {columns.map((col, idx) => (
              <TableRow key={idx}>
                <TableCell>{col.name}</TableCell>
                <TableCell>{col.dataType}</TableCell>
                <TableCell>{col.validationConstraints.join(', ')}</TableCell>
                <TableCell>{col.databaseConstraints.join(', ')}</TableCell>
                <TableCell>
                  <IconButton onClick={() => handleOpen(idx)} size="small" color="primary">
                    <Edit />
                  </IconButton>
                  <IconButton onClick={() => handleDelete(idx)} size="small" color="error">
                    <Delete />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
};

export default Columns;

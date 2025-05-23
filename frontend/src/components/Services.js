import React, { useState } from 'react';
import {
  Box, Stack, Typography, Button, Dialog, DialogTitle, DialogContent,
  FormControl, InputLabel, Select, MenuItem, Checkbox, ListItemText, OutlinedInput, TextField, TableContainer,
  Paper, Table, TableHead, TableRow, TableCell, TableBody, IconButton,
} from '@mui/material';
import { Edit, Delete, Add, Visibility } from '@mui/icons-material';

const mapping = ['GET', 'POST', 'PUT', 'DELETE'];
const order = ['ASC', 'DESC'];
const paramRequestTypes = ['path', 'body', 'query'];
const paramTypes = ['int', 'string', 'boolean', 'float', 'double'];

const Services = ({ services, setServices, columns }) => {
  const [editIndex, setEditIndex] = useState(null);
  const [open, setOpen] = useState(false);
  const [paramsOpen, setParamsOpen] = useState(false);
  const [paramServiceIndex, setParamServiceIndex] = useState(null);
  const [viewParamsOpen, setViewParamsOpen] = useState(false);
  const [viewParams, setViewParams] = useState([]);
  const [newService, setNewService] = useState({
    field: [],
    mapping: '',
    action: '',
    orderBy: [],
    order: '',
    groupBy: [],
    params: [],
    returnType: '',
  });
  const [paramsState, setParamsState] = useState([]);
  const [tableParamsState, setTableParamsState] = useState([]);

  const resetServiceState = () => ({
    field: [],
    mapping: '',
    action: '',
    orderBy: [],
    order: '',
    groupBy: [],
    params: [],
    returnType: '',
  });

  const handleOpen = (index = null) => {
    if (index !== null) {
      setEditIndex(index);
      setNewService(services[index]);
    } else {
      setEditIndex(null);
      setNewService(resetServiceState());
    }
    setParamsState([]);
    setOpen(true);
  };

  const handleClose = () => {
    setNewService(resetServiceState());
    setEditIndex(null);
    setParamsState([]);
    setOpen(false);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (Array.isArray(value)) {
      setNewService(prev => ({ ...prev, [name]: value }));
    } else {
      setNewService(prev => ({ ...prev, [name]: value }));
    }
  };

  const handleSave = () => {
    if (editIndex !== null) {
      const updated = [...services];
      updated[editIndex] = newService;
      setServices(updated);
    } else {
      setServices(prev => [...prev, newService]);
    }
    handleClose();
  };

  const handleDelete = (index) => {
    setServices(prev => prev.filter((_, i) => i !== index));
  };

  const handleOpenParamsInsideDialog = () => {
    setParamsState(newService.params || []);
    setParamsOpen(true);
  };

  const handleSaveParamsInsideDialog = () => {
    setNewService(prev => ({ ...prev, params: paramsState }));
    setParamsOpen(false);
  };

  const handleCloseParams = () => {
    setParamsOpen(false);
    setParamsState([]);
  };

  const handleParamChange = (index, field, value) => {
    const newParams = [...paramsState];
    newParams[index] = { ...newParams[index], [field]: value };
    setParamsState(newParams);
  };

  const handleAddParam = () => {
    setParamsState([...paramsState, { paramName: '', paramType: '', paramRequestType: '' }]);
  };

  const handleRemoveParam = (index) => {
    setParamsState(paramsState.filter((_, i) => i !== index));
  };

  const handleOpenParamsFromTable = (serviceIndex) => {
    setParamServiceIndex(serviceIndex);
    setTableParamsState(services[serviceIndex]?.params || []);
    setParamsOpen(true);
  };

  const handleSaveParamsFromTable = () => {
    if (paramServiceIndex !== null) {
      const updatedServices = [...services];
      updatedServices[paramServiceIndex].params = tableParamsState;
      setServices(updatedServices);
    }
    setParamsOpen(false);
    setParamServiceIndex(null);
  };

  const handleTableParamChange = (index, field, value) => {
    const newParams = [...tableParamsState];
    newParams[index] = { ...newParams[index], [field]: value };
    setTableParamsState(newParams);
  };

  const handleTableAddParam = () => {
    setTableParamsState([...tableParamsState, { paramName: '', paramType: '', paramRequestType: '' }]);
  };

  const handleTableRemoveParam = (index) => {
    setTableParamsState(tableParamsState.filter((_, i) => i !== index));
  };

  const handleViewParams = (params) => {
    setViewParams(params || []);
    setViewParamsOpen(true);
  };

  const handleCloseViewParams = () => {
    setViewParamsOpen(false);
    setViewParams([]);
  };

  const renderParamsEditor = (
    <DialogContent
      dividers
      sx={{
        height: 400,
        overflowY: 'auto',
        display: 'flex',
        flexDirection: 'column',
        gap: 2,
      }}
    >
      {(paramServiceIndex !== null ? tableParamsState : paramsState).map((param, index) => {
        const paramValue = paramServiceIndex !== null ? tableParamsState[index] : paramsState[index];
        const onChange = paramServiceIndex !== null ? handleTableParamChange : handleParamChange;
        const onRemove = paramServiceIndex !== null ? handleTableRemoveParam : handleRemoveParam;

        return (
          <Stack key={index} direction="row" spacing={2} alignItems="center">
            <TextField
              label="Param Name"
              value={paramValue.paramName}
              onChange={e => onChange(index, 'paramName', e.target.value)}
              size="small"
              fullWidth
            />
            <FormControl size="small" sx={{ minWidth: 120 }}>
              <InputLabel>Type</InputLabel>
              <Select
                value={paramValue.paramType}
                onChange={e => onChange(index, 'paramType', e.target.value)}
                label="Type"
              >
                {paramTypes.map((type) => (
                  <MenuItem key={type} value={type}>{type}</MenuItem>
                ))}
              </Select>
            </FormControl>
            <FormControl size="small" sx={{ minWidth: 120 }}>
              <InputLabel>Request Type</InputLabel>
              <Select
                value={paramValue.paramRequestType}
                onChange={e => onChange(index, 'paramRequestType', e.target.value)}
                label="Request Type"
              >
                {paramRequestTypes.map((type) => (
                  <MenuItem key={type} value={type}>{type}</MenuItem>
                ))}
              </Select>
            </FormControl>
            <IconButton onClick={() => onRemove(index)} color="error" size="small">
              &times;
            </IconButton>
          </Stack>
        );
      })}

      {(paramServiceIndex !== null
        ? <Button variant="outlined" onClick={handleTableAddParam}>Add New Parameter</Button>
        : <Button variant="outlined" onClick={handleAddParam}>Add New Parameter</Button>
      )}
    </DialogContent>
  );

  return (
    <Box sx={{ padding: 4, marginTop: "1%", bgcolor: '#f9f9f9', borderRadius: 2 }}>
      <Stack direction="row" justifyContent="space-between" alignItems="center" mb={2}>
        <Typography variant="h5">Services</Typography>
        <Button variant="contained" onClick={() => handleOpen()}>Add Service</Button>
      </Stack>

      <Dialog open={open} onClose={handleClose} fullWidth maxWidth="sm">
        <DialogTitle>{editIndex !== null ? 'Edit Service' : 'Add Service'}</DialogTitle>
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
          <FormControl fullWidth size="small">
            <InputLabel>Field</InputLabel>
            <Select
              multiple
              name="field"
              value={newService.field}
              onChange={handleChange}
              renderValue={(selected) => selected.join(', ')}
              input={<OutlinedInput label="Field" />}
            >
              {columns.map((col, idx) => (
                <MenuItem key={idx} value={col.name}>
                  <Checkbox checked={newService.field.includes(col.name)} />
                  <ListItemText primary={col.name} />
                </MenuItem>
              ))}
            </Select>
          </FormControl>
          <FormControl>
            <InputLabel>Mapping</InputLabel>
            <Select
              name="mapping"
              value={newService.mapping}
              onChange={handleChange}
              input={<OutlinedInput label="Mapping" />}>
              {mapping.map((option, index) => (
                <MenuItem key={index} value={option}>
                  {option}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
          <TextField
            name='action'
            label='Action'
            value={newService.action}
            onChange={handleChange}
            fullWidth
            size="small"
          />
          <FormControl fullWidth size="small">
            <InputLabel>Order By</InputLabel>
            <Select
              multiple
              name='orderBy'
              value={newService.orderBy}
              onChange={handleChange}
              input={<OutlinedInput label='Order By' />}
              renderValue={(selected) => selected.join(', ')}
            >
              {columns.map((col, idx) => (
                <MenuItem key={idx} value={col.name}>
                  <Checkbox checked={newService.orderBy.includes(col.name)} />
                  <ListItemText primary={col.name} />
                </MenuItem>
              ))}
            </Select>
          </FormControl>
          <FormControl>
            <InputLabel>Order</InputLabel>
            <Select
              name='order'
              value={newService.order}
              onChange={handleChange}
              input={<OutlinedInput label='Order' />}
            >
              {order.map((o, idx) => (
                <MenuItem key={idx} value={o}>{o}</MenuItem>
              ))}
            </Select>
          </FormControl>
          <FormControl fullWidth size="small">
            <InputLabel>Group By</InputLabel>
            <Select
              multiple
              name='groupBy'
              value={newService.groupBy}
              onChange={handleChange}
              input={<OutlinedInput label='Group By' />}
              renderValue={(selected) => selected.join(', ')}
            >
              {columns.map((col, idx) => (
                <MenuItem key={idx} value={col.name}>
                  <Checkbox checked={newService.groupBy.includes(col.name)} />
                  <ListItemText primary={col.name} />
                </MenuItem>
              ))}
            </Select>
          </FormControl>
          <FormControl>
            <InputLabel>Return Type</InputLabel>
            <Select
              name='returnType'
              value={newService.returnType || ''}
              onChange={handleChange}
              input={<OutlinedInput label='Return Type' />}
            >
              {['List', 'Object', 'Integer', 'Boolean', 'String'].map((option) => (
                <MenuItem key={option} value={option}>{option}</MenuItem>
              ))}
            </Select>
          </FormControl>
          <Button variant="outlined" onClick={handleOpenParamsInsideDialog}>
            {newService.params && newService.params.length > 0 ? 'Edit Parameters' : 'Add Parameters'}
          </Button>
        </DialogContent>
        <Stack direction="row" spacing={2} justifyContent="flex-end" p={2}>
          <Button onClick={handleClose}>Cancel</Button>
          <Button variant="contained" onClick={handleSave}>Save</Button>
        </Stack>
      </Dialog>

      <Dialog
        open={paramsOpen}
        onClose={() => {
          setParamsOpen(false);
          setParamServiceIndex(null);
          setParamsState([]);
          setTableParamsState([]);
        }}
        fullWidth
        maxWidth="md"
      >
        <DialogTitle>Parameters</DialogTitle>
        {renderParamsEditor}
        <Stack direction="row" spacing={2} justifyContent="flex-end" p={2}>
          <Button
            onClick={() => {
              setParamsOpen(false);
              setParamServiceIndex(null);
              setParamsState([]);
              setTableParamsState([]);
            }}
          >
            Cancel
          </Button>
          <Button
            variant="contained"
            onClick={() => {
              if (paramServiceIndex !== null) {
                handleSaveParamsFromTable();
              } else {
                handleSaveParamsInsideDialog();
              }
            }}
          >
            Save
          </Button>
        </Stack>
      </Dialog>

      <Dialog open={viewParamsOpen} onClose={handleCloseViewParams} fullWidth maxWidth="sm">
        <DialogTitle>View Parameters</DialogTitle>
        <DialogContent dividers>
          {viewParams.length === 0 && <Typography>No parameters defined.</Typography>}
          {viewParams.length > 0 && (
            <TableContainer component={Paper}>
              <Table size="small" aria-label="parameters table">
                <TableHead>
                  <TableRow>
                    <TableCell>Param Name</TableCell>
                    <TableCell>Type</TableCell>
                    <TableCell>Request Type</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {viewParams.map((param, idx) => (
                    <TableRow key={idx}>
                      <TableCell>{param.paramName}</TableCell>
                      <TableCell>{param.paramType}</TableCell>
                      <TableCell>{param.paramRequestType}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          )}
        </DialogContent>
        <Stack direction="row" justifyContent="flex-end" p={2}>
          <Button onClick={handleCloseViewParams}>Close</Button>
        </Stack>
      </Dialog>

      <TableContainer component={Paper}>
        <Table size="small" aria-label="services table">
          <TableHead>
            <TableRow>
              <TableCell>Field</TableCell>
              <TableCell>Mapping</TableCell>
              <TableCell>Action</TableCell>
              <TableCell>Order By</TableCell>
              <TableCell>Order</TableCell>
              <TableCell>Group By</TableCell>
              <TableCell>Return Type</TableCell>
              <TableCell>Parameters</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {services.map((serv, index) => (
              <TableRow key={index}>
                <TableCell>{serv.field?.join(', ')}</TableCell>
                <TableCell>{serv.mapping}</TableCell>
                <TableCell>{serv.action}</TableCell>
                <TableCell>{serv.orderBy?.join(', ')}</TableCell>
                <TableCell>{serv.order}</TableCell>
                <TableCell>{serv.groupBy?.join(', ')}</TableCell>
                <TableCell>{serv.returnType}</TableCell>
                <TableCell>{serv.params && serv.params.length > 0 ? `${serv.params.length} param(s)` : 'No params'}</TableCell>
                <TableCell>
                  <Stack direction="row" spacing={1} alignItems="center">
                    <Button
                      size="small"
                      variant="outlined"
                      startIcon={serv.params && serv.params.length > 0 ? <Edit /> : <Add />}
                      onClick={() => handleOpenParamsFromTable(index)}
                    >
                      {serv.params && serv.params.length > 0 ? 'Edit Params' : 'Add Params'}
                    </Button>
                    {serv.params && serv.params.length > 0 && (
                      <IconButton
                        size="small"
                        onClick={() => handleViewParams(serv.params)}
                        title="View Parameters"
                      >
                        <Visibility />
                      </IconButton>
                    )}
                    <Button size="small" variant="outlined" onClick={() => handleOpen(index)}>Edit</Button>
                    <Button size="small" variant="outlined" color="error" onClick={() => handleDelete(index)}>Delete</Button>
                  </Stack>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
};

export default Services;

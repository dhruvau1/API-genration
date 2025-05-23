import React, { useState } from 'react';
import {
  Box, Stack, Typography, Button, Dialog, DialogTitle, DialogContent,
  FormControl, InputLabel, Select, MenuItem, Checkbox, ListItemText, OutlinedInput,
} from '@mui/material';

const mapping = ['GET','POST','PUT','DELETE'];

const Services = ({ services, setServices, columns }) => {
  const [editIndex, setEditIndex] = useState(null);
  const [open, setOpen] = useState(false);
  const [newService, setNewService] = useState({
    field: [],
    mapping: '',
    action: '',
    orderBy: '',
    groupBy: '',
    params: [],
    returnType: '',
  });

  const resetServiceState = () => ({
    field: [],
    mapping: '',
    action: '',
    orderBy: '',
    groupBy: '',
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
    setOpen(true);
  };

  const handleClose = () => {
    setNewService(resetServiceState());
    setEditIndex(null);
    setOpen(false);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setNewService(prev => ({ ...prev, [name]: value }));
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
              input={<OutlinedInput label="Data Type" />}>

              </Select>
          </FormControl>
        </DialogContent>

        <Stack direction="row" spacing={2} sx={{ p: 2, justifyContent: 'flex-end' }}>
          <Button onClick={handleClose}>Cancel</Button>
          <Button variant="contained" onClick={handleSave}>
            {editIndex !== null ? 'Update' : 'Add'}
          </Button>
        </Stack>
      </Dialog>
    </Box>
  );
};

export default Services;

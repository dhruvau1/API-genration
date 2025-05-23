import React, { useState } from 'react';
import {
  Box,
  Stack,
  Typography,
  Select,
  MenuItem,
  Checkbox,
  ListItemText,
  OutlinedInput,
  FormControl,
  InputLabel,
} from '@mui/material';

const Requirements = () => {
  const options = ['getAll', 'getById', 'add', 'count', 'update'];
  const [requirements, setRequirements] = useState([]);

  const handleChange = (e) => {
    const { value } = e.target;
    setRequirements(typeof value === 'string' ? value.split(',') : value);
  };

  return (
    <Box sx={{ padding: 4, marginTop: "1%", bgcolor: '#f9f9f9', borderRadius: 2 }}>
      <Stack direction="row" justifyContent="space-between" alignItems="center" mb={2}>
        <Typography variant="h5">Requirements</Typography>
        <FormControl sx={{ width: 400 }}> {/* Increase width here */}
          <InputLabel>Requirements</InputLabel>
          <Select
            multiple
            name="requirements"
            value={requirements}
            onChange={handleChange}
            input={<OutlinedInput label="Requirements" />}
            renderValue={(selected) => selected.join(', ')}
            MenuProps={{
              PaperProps: {
                style: {
                  maxHeight: 300,
                  width: 400,
                },
              },
            }}
          >
            {options.map((option) => (
              <MenuItem key={option} value={option}>
                <Checkbox checked={requirements.includes(option)} />
                <ListItemText primary={option} />
              </MenuItem>
            ))}
          </Select>
        </FormControl>
      </Stack>
    </Box>
  );
};

export default Requirements;

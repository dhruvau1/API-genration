import React, { useState } from 'react';
import { Box, TextField, Stack } from '@mui/material';

const Starter = () => {
  const [schema, setSchema] = useState('');
  const [tableName, setTableName] = useState('');
  const [toPackage, setToPackage] = useState('');

  return (
    <Box
      sx={{
        backgroundColor: '#f9f9f9',
        padding: 3,
        borderRadius: 2,
        marginTop: "5%",
      }}
    >
      <Stack direction="row" spacing={2}>
        <TextField
          label="Schema"
          value={schema}
          onChange={(e) => setSchema(e.target.value)}
          fullWidth
          size="small"
          sx={{
            '& .MuiInputBase-input': {
              backgroundColor: '#fff',
            },
          }}
        />
        <TextField
          label="Table Name"
          value={tableName}
          onChange={(e) => setTableName(e.target.value)}
          fullWidth
          size="small"
          sx={{
            '& .MuiInputBase-input': {
              backgroundColor: '#fff',
            },
          }}
        />
        <TextField
          label="Package"
          value={toPackage}
          onChange={(e) => setToPackage(e.target.value)}
          fullWidth
          size="small"
          sx={{
            '& .MuiInputBase-input': {
              backgroundColor: '#fff',
            },
          }}
        />
      </Stack>
    </Box>
  );
};

export default Starter;
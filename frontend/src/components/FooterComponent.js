import React from 'react';
import { Box, Toolbar } from '@mui/material';

const FooterComponent = () => {
  return (
    <Box
      component="footer"
      sx={{
        width: '100%',
        backgroundColor: '#FFFACD',
        color: 'black',
        boxShadow: 'none',
        zIndex: 1300,
        py: 1.5,
        textAlign: 'center',
      }}
    >
      <Toolbar sx={{ justifyContent: 'center', padding: 0, minHeight: 'auto' }}>
        © All Rights Reserved. Mphasis Ltd.
      </Toolbar>
    </Box>
  );
};

export default FooterComponent;

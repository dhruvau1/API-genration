import React from 'react';
import { Box } from '@mui/material';
import FooterComponent from './FooterComponent';

const PageLayout = ({ children }) => {
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        minHeight: '100vh',
      }}
    >
      {/* Main content expands to fill available space */}
      <Box component="main" sx={{ flexGrow: 1 }}>
        {children}
      </Box>

      {/* Footer stays at bottom if content is short, or below content if scrollable */}
      <FooterComponent />
    </Box>
  );
};

export default PageLayout;

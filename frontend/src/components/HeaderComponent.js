import React from 'react';
import { AppBar, Toolbar, Typography } from '@mui/material';
import { Stack, Button } from '@mui/material';
import logo from '../resources/images/logo.png';

const HeaderComponent = () => {
  return (
    <div>
      <AppBar sx={{
        backgroundColor: '#FFFACD',
        color: 'black',
        boxShadow: "none"
      }}>
        <Toolbar sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <img src={logo} alt="Mphasis Logo" className="logo" />
            <Typography className="typography" sx={{ fontWeight: 'bold' }}>
              API Code Generator
            </Typography>
          <Button variant="contained">Generate</Button>
        </Toolbar>

      </AppBar>
    </div>
  )
}

export default HeaderComponent
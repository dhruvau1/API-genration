import React from 'react'
import {AppBar, Toolbar, Typography} from '@mui/material'
import logo from '../resources/images/logo.png';

const HeaderComponent = () => {
  return (
    <div>
        <AppBar sx={{ 
            backgroundColor: '#FFFACD',
            color: 'black' }}>
            <Toolbar>
                <img src={logo} alt="Mphasis Logo" className='logo' />
                <Typography className='typography' style={{ fontWeight: 'bold'}}>API Code Generator</Typography>
            </Toolbar>
        </AppBar>
    </div>
  )
}

export default HeaderComponent
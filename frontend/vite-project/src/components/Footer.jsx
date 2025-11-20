import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';

export default function Footer() {
    return (
        <Box sx={{flexGrow: 1}}>
            <AppBar position="static">
                <Toolbar sx={{display: 'flex', justifyContent: 'center'}}>
                    <Typography variant="h6" component="div">
                        © 2025 Base
                    </Typography>
                </Toolbar>
            </AppBar>
        </Box>
    );
}
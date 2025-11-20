import * as React from 'react';
import Box from '@mui/material/Box';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';

export default function ProjectCard({project, onSelect}) {
    return (
        <Box sx={{
            width: '100%',
            cursor: 'pointer',
            '&:hover': {
                backgroundColor: 'rgba(0, 0, 0, 0.03)',
                boxShadow: '0 4px 6px rgba(0,0,0,0.12)',
            },
            borderRadius: 2,
            boxShadow: '0 1px 3px rgba(0,0,0,0.15)',
            my: 1

        }} data-testid="project-card" onClick={onSelect}>
            <CardContent>
                <Typography gutterBottom sx={{
                    color: 'text.secondary',
                    typography: 'h4'
                }}>
                    {project.priority}
                </Typography>
                <Typography
                    gutterBottom
                    component="div"
                    sx={{
                        typography: 'h3',
                    }}>
                    {project.name}
                </Typography>
                <Typography
                    sx={{
                        color: 'text.secondary',
                        mb: 1.5,
                        typography: 'h5',
                    }}>
                    due {project.dueDate}
                </Typography>
            </CardContent>
        </Box>
    );
}

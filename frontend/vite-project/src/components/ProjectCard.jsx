import * as React from 'react';
import Box from '@mui/material/Box';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';

export default function ProjectCard({project, onSelect}) {
    return (
        <Box sx={{
            minWidth: 275,
            cursor: 'pointer',
            '&:hover': {
                backgroundColor: 'rgba(0, 0, 0, 0.04)'
            }
        }} data-testid="project-card" onClick={onSelect}>
            <CardContent>
                <Typography gutterBottom sx={{color: 'text.secondary', fontSize: 14}}>{project.priority}
                </Typography>
                <Typography variant="h5" component="div">
                    {project.name}
                </Typography>
                <Typography sx={{color: 'text.secondary', mb: 1.5}}>{project.dueDate}</Typography>
            </CardContent>
        </Box>
    );
}

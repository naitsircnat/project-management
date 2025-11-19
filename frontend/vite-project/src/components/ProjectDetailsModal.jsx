import * as React from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';

export default function ProjectDetailsModal({open, close, edit, project}) {
    return (
        <Dialog
            open={open}
            onClose={close}
            aria-labelledby="project-details-title"
            aria-describedby="project-details-description"
            maxWidth="sm" // CHANGED: Sets fixed small width
            fullWidth={true} // CHANGED: Ensures it uses the full maxWidth
        >
            {/* CHANGED: DialogTitle instead of Typography in Box */}
            <DialogTitle id="project-details-title">
                {project.name}
            </DialogTitle>

            {/* CHANGED: DialogContent for the main content area */}
            <DialogContent>
                {/* CHANGED: Organized content with better spacing and structure */}
                <Box sx={{display: 'flex', flexDirection: 'column', gap: 2, mt: 1}}>
                    <Typography>
                        <strong>Description</strong>
                        <br/>
                        {project.description}
                    </Typography>
                    <Typography>
                        <strong>Due</strong>
                        <br/>
                        {project.dueDate}
                    </Typography>
                    <Typography>
                        <strong>Priority</strong>
                        <br/>
                        {project.priority}
                    </Typography>
                    <Typography>
                        <strong>Status</strong>
                        <br/>
                        {project.status}
                    </Typography>

                    <Typography>
                        <strong>Created</strong>
                        <br/>
                        {project.createdAt}
                    </Typography>
                </Box>
            </DialogContent>

            {/* CHANGED: DialogActions for the button group */}
            <DialogActions>
                <Button onClick={close}>Close</Button>
                <Button onClick={edit}>Edit</Button>
                <Button color="error">Delete</Button>
            </DialogActions>
        </Dialog>
    );
}
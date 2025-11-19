import * as React from 'react';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Modal from '@mui/material/Modal';
import Button from "@mui/material/Button";

const style = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 400,
    bgcolor: 'background.paper',
    border: '2px solid #000',
    boxShadow: 24,
    p: 4,
};

export default function ProjectDetailsModal({open, close, edit, project}) {

    return (
        <div>
            <Modal
                open={open}
                onClose={close}
                aria-labelledby="modal-modal-title"
                aria-describedby="modal-modal-description"
            >
                <Box sx={style}>
                    <Typography id="modal-modal-title" variant="h6" component="h2">
                        {project.name}
                    </Typography>
                    <Typography id="modal-modal-description" sx={{mt: 2}}>
                        {project.dueDate}
                    </Typography>
                    <Typography id="modal-modal-description" sx={{mt: 2}}>
                        {project.priority}
                    </Typography>
                    <Typography id="modal-modal-description" sx={{mt: 2}}>
                        {project.status}
                    </Typography>
                    <Typography id="modal-modal-description" sx={{mt: 2}}>
                        {project.description}
                    </Typography>
                    <Typography id="modal-modal-description" sx={{mt: 2}}>
                        {project.createdAt}
                    </Typography>
                    <Button onClick={close}>Close</Button>
                    <Button onClick={edit} type="submit" form="subscription-form">
                        Edit
                    </Button>
                    <Button type="submit" form="subscription-form">
                        Delete
                    </Button>
                </Box>
            </Modal>
        </div>
    );
}
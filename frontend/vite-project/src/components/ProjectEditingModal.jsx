import * as React from 'react';
import {useEffect} from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import {FormControl, FormHelperText, InputLabel, Select} from "@mui/material";
import MenuItem from "@mui/material/MenuItem";
import DateSelector from "./DateSelector.jsx";
import {Controller, useForm} from "react-hook-form";
import axios from "axios";
import dayjs from "dayjs";

export default function ProjectEditingModal({open, onClose, project}) {

    const {
        control,
        handleSubmit,
        formState: {errors},
        reset
    } = useForm({
        defaultValues: {
            name: project?.name || '',
            description: project?.description || '',
            priority: project?.priority,
            status: project?.status,
            dueDate: project.dueDate ? dayjs(project.dueDate) : null
        }
    })

    useEffect(() => {
        if (open && project) {
            reset({
                name: project.name || '',
                description: project.description || '',
                priority: project.priority || '',
                status: project.status || '',
                dueDate: project.dueDate ? dayjs(project.dueDate) : null
            });
        }
    }, [open, project, reset]);

    const onSubmit = async (data) => {
        const projectData = {
            ...data,
            dueDate: data.dueDate ? data.dueDate.format('YYYY-MM-DD') : null
        }

        try {

            await axios.put(
                `${import.meta.env.VITE_API_URL}/${project.id}`,
                projectData,
                {
                    headers: {
                        'Content-Type': 'application/json',
                    }
                }
            )

            onClose(true);

        } catch (error) {
            console.error('Error creating project:', error);

            if (error.response) {
                // Server responded with error status
                console.error('Server error:', error.response.data);
                // You might want to show this to the user:
                // setError('Failed to create project: ' + error.response.data.message);
            } else if (error.request) {
                // Request was made but no response received
                console.error('Network error:', error.request);
                // setError('Network error - please check your connection');
            } else {
                // Something else happened
                console.error('Error:', error.message);
                // setError('An unexpected error occurred');
            }
        }
    };

    const handleCancel = () => {
        onClose(false);
    }

    return (
        <Dialog
            open={open}
            onClose={handleCancel}
        >
            <DialogTitle>Edit Project</DialogTitle>
            <DialogContent>

                <form id="project-editing-form">

                    <Controller
                        name="name"
                        control={control}
                        rules={{
                            required: 'Project name is required',
                        }}
                        render={({field}) => (
                            <TextField
                                {...field}
                                autoFocus
                                margin="dense"
                                label="Project Name"
                                type="text"
                                fullWidth
                                variant="standard"
                                error={!!errors.name}
                                helperText={errors.name?.message}
                            />
                        )}
                    />

                    <Controller
                        name="description"
                        control={control}
                        rules={{
                            required: 'Description is required',
                        }}
                        render={({field}) => (
                            <TextField
                                {...field}
                                margin="dense"
                                label="Description"
                                type="text"
                                fullWidth
                                variant="standard"
                                error={!!errors.description}
                                helperText={errors.description?.message}
                            />
                        )}
                    />

                    <Controller
                        name="dueDate"
                        control={control}
                        rules={{
                            required: 'Due date is required',
                            validate: {
                                notPastDate: (value) => {
                                    if (!value) return true;

                                    const selectedDate = value.toDate();
                                    const today = new Date();

                                    today.setHours(0, 0, 0, 0);
                                    selectedDate.setHours(0, 0, 0, 0);

                                    return selectedDate >= today || 'Due date cannot be in the past';
                                }
                            }
                        }}
                        render={({field, fieldState: {error}}) => {

                            return (
                                <DateSelector
                                    value={field.value}
                                    onChange={field.onChange}
                                    label="Due Date"
                                    error={!!error}
                                    helperText={error?.message}
                                />
                            );
                        }}
                    />

                    <Controller
                        name="priority"
                        control={control}
                        rules={{required: 'Priority is required'}}
                        render={({field, fieldState: {error}}) => (
                            <FormControl
                                fullWidth
                                error={!!error}
                                margin="dense"
                                variant="standard"
                            >
                                <InputLabel id="priority-label">Priority</InputLabel>
                                <Select
                                    {...field}
                                    labelId="priority-label"
                                    id="priority"
                                    label="Priority"
                                >
                                    <MenuItem value="HIGH">HIGH</MenuItem>
                                    <MenuItem value="MEDIUM">MEDIUM</MenuItem>
                                    <MenuItem value="LOW">LOW</MenuItem>
                                </Select>

                                {error && <FormHelperText error>{error.message}</FormHelperText>}
                            </FormControl>
                        )}
                    />

                    <Controller
                        name="status"
                        control={control}
                        rules={{required: 'Status is required'}}
                        render={({field, fieldState: {error}}) => (
                            <FormControl
                                fullWidth
                                error={!!error}
                                margin="dense"
                                variant="standard"
                            >
                                <InputLabel id="status-label">Status</InputLabel>
                                <Select
                                    {...field}
                                    labelId="status-label"
                                    id="status"
                                    label="Priority"
                                >
                                    <MenuItem value="NOT_STARTED">NOT STARTED</MenuItem>
                                    <MenuItem value="IN_PROGRESS">IN PROGRESS</MenuItem>
                                    <MenuItem value="READY_FOR_QA">READY FOR QA</MenuItem>
                                    <MenuItem value="READY_FOR_CLIENT_REVIEW">READY FOR CLIENT</MenuItem>
                                    <MenuItem value="CLIENT_REVIEW">CLIENT REVIEW</MenuItem>
                                    <MenuItem value="COMPLETED">COMPLETED</MenuItem>
                                </Select>

                                {error && <FormHelperText error>{error.message}</FormHelperText>}
                            </FormControl>
                        )}
                    />

                </form>
            </DialogContent>
            <DialogActions>
                <Button onClick={handleCancel}>Cancel</Button>
                <Button
                    type="button"
                    onClick={handleSubmit(onSubmit)}
                >
                    Save
                </Button>
            </DialogActions>
        </Dialog>
    );
}
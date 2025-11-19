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

export default function ProjectCreationModal({open, onClose}) {

    const {
        control,
        handleSubmit,
        formState: {errors},
        reset
    } = useForm({
        defaultValues: {
            name: '',
            description: '',
            priority: '',
            dueDate: null
        }
    })

    useEffect(() => {
        if (open) {
            reset({
                name: '',
                description: '',
                priority: '',
                dueDate: null
            });
        }
    }, [open, reset]);

    const onSubmit = async (data) => {
        const projectData = {
            ...data,
            dueDate: data.dueDate ? data.dueDate.format('YYYY-MM-DD') : null
        }

        console.log('Form data:', projectData);

        console.log('VITE_API_URL:', import.meta.env.VITE_API_URL);

        try {

            const response = await axios.post(
                `${import.meta.env.VITE_API_URL}`,
                projectData,
                {
                    headers: {
                        'Content-Type': 'application/json',
                    }
                }
            )

            console.log('Project created successfully:', response.data);

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
            <DialogTitle>Create Project</DialogTitle>
            <DialogContent>

                <form id="project-creation-form">

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
                            console.log('Due Date Field State:', {
                                value: field.value,
                                hasError: !!error,
                                errorMessage: error?.message
                            });
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

                </form>
            </DialogContent>
            <DialogActions>
                <Button onClick={handleCancel}>Cancel</Button>
                <Button
                    type="button"
                    onClick={handleSubmit(onSubmit)}
                >
                    Create
                </Button>
            </DialogActions>
        </Dialog>
    );
}
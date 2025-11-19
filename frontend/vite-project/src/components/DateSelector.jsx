import * as React from 'react';
import {DemoContainer} from '@mui/x-date-pickers/internals/demo';
import {LocalizationProvider} from '@mui/x-date-pickers/LocalizationProvider';
import {AdapterDayjs} from '@mui/x-date-pickers/AdapterDayjs';
import {DatePicker} from '@mui/x-date-pickers/DatePicker';
import {Box} from '@mui/material';

export default function DateSelector({value, onChange, label = "Due Date", error, helperText}) {
    return (
        <LocalizationProvider dateAdapter={AdapterDayjs}>
            <Box sx={{my: 3}}>
                <DemoContainer components={['DatePicker', 'DatePicker']}>
                    <DatePicker
                        label={label}
                        value={value}
                        onChange={onChange}
                        slotProps={{
                            textField: {
                                variant: 'standard',
                                fullWidth: true,
                                error: error,           // ← This enables red styling
                                helperText: helperText   // ← This shows the error message
                            }
                        }}
                    />
                </DemoContainer>
            </Box>
        </LocalizationProvider>
    );
}

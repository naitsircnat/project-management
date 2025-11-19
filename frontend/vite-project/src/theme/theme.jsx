import {createTheme} from '@mui/material/styles';

const customPalette = {

    primary: {
        main: '#5c6bc0',
        light: '#e8eaf6',
        dark: '#283593',
        contrastText: '#fff',
    },
    success: {
        main: '#2e7d32',
        light: '#4caf50',
        dark: '#1b5e20',
    },
    warning: {
        main: '#ed6c02',
        light: '#ff9800',
        dark: '#e65100',
    },
    error: {
        main: '#d32f2f',
        light: '#ef5350',
        dark: '#c62828',
    },
    background: {
        default: '#f5f5f5',
        paper: '#ffffff',
    },
    // Custom colors for your app
    custom: {
        projectCard: '#e3f2fd',
        header: '#1e88e5',
        sidebar: '#f8f9fa',
        highlight: '#fff9c4',
    }
};

const theme = createTheme({
    palette: customPalette,
    typography: {
        fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
        h2: {
            fontSize: '1.25rem',
            fontWeight: 600,
            lineHeight: 1.2,
        },
        h3: {
            fontSize: '1.1rem',
            fontWeight: 500,
        },
        h4: {
            fontSize: '1rem',  // CHANGED: 18px (much smaller)
            fontWeight: 400,       // CHANGED: Lighter weight
            lineHeight: 1.2,
            letterSpacing: '0.00735em',
        },
        h5: {
            fontSize: '0.8rem',      // CHANGED: 16px (much smaller)
            fontWeight: 400,       // CHANGED: Lighter weight
            lineHeight: 1.2,
            letterSpacing: '0em',
        },
    },
    components: {
        MuiButton: {
            styleOverrides: {
                root: {
                    borderRadius: 8,
                },
            },
        },
    },
});

export default theme;
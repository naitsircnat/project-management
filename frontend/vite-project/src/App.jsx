import './App.css'
import Footer from "./components/Footer.jsx";
import Container from '@mui/material/Container';
import {useEffect, useState} from "react";
import axios from 'axios';
import Header from "./components/Header.jsx";
import Box from "@mui/material/Box";
import ProjectCard from "./components/ProjectCard.jsx";
import Button from "@mui/material/Button";
import ProjectDetailsModal from "./components/ProjectDetailsModal.jsx";
import ProjectCreationModal from "./components/ProjectCreationModal.jsx";
import ProjectEditingModal from "./components/ProjectEditingModal.jsx";

function App() {

    const statusColumnMap = [
        {status: "NOT_STARTED", columnName: "NOT STARTED"},
        {status: "IN_PROGRESS", columnName: "IN PROGRESS"},
        {status: "READY_FOR_QA", columnName: "READY FOR QA"},
        {status: "READY_FOR_CLIENT_REVIEW", columnName: "READY FOR CLIENT"},
        {status: "CLIENT_REVIEW", columnName: "CLIENT REVIEW"},
        {status: "COMPLETED", columnName: "COMPLETED"}
    ];

    const [projects, setProjects] = useState([]);

    const fetchProjects = async () => {
        try {
            const response = await axios.get(`${import.meta.env.VITE_API_URL}`);
            console.log('API Response:', response);
            setProjects(response.data.projects);
        } catch (error) {
            console.error('Error fetching projects:', error);
        }
    }

    useEffect(() => {
        fetchProjects();
    }, []);

    console.log(projects);

    const filteredProjects = (status) => projects.filter(project => project.status === status);

    const [projectCreationDialogOpen, setProjectCreationDialogOpen] = useState(false);
    const handleOpenProjectCreationDialog = () => setProjectCreationDialogOpen(true);
    const handleCloseProjectCreationDialog = (shouldRefetch = false) => {
        setProjectCreationDialogOpen(false);
        if (shouldRefetch) {
            fetchProjects();
        }
    }

    const [selectedProject, setSelectedProject] = useState(null);

    const [projectDetailsModalOpen, setProjectDetailsModalOpen] = useState(false);
    const handleCloseProjectDetailsModal = () => setProjectDetailsModalOpen(false);

    const handleShowProjectDetails = (project) => {
        setSelectedProject(project);
        setProjectDetailsModalOpen(true);
    }

    const handleEditProjectDetailsModal = () => {
        setProjectDetailsModalOpen(false);
        setProjectEditingModalOpen(true);
    }

    const [projectEditingModalOpen, setProjectEditingModalOpen] = useState(false);
    const handleCloseProjectEditingModal = (shouldRefetch = false) => {
        setProjectEditingModalOpen(false);
    }


    return (
        <>
            <Header/>
            <Container sx={{my: 4}}>
                <Button onClick={handleOpenProjectCreationDialog} variant="outlined">Add Project</Button>
            </Container>

            <Container>
                <Box sx={{display: 'flex', flexDirection: 'row', gap: 4}}>
                    {statusColumnMap.map(columnMap => (
                        <Box key={columnMap.status}>
                            <Box component="h2" sx={{
                                p: 1,
                                backgroundColor: 'primary.light',
                                typography: 'h3',
                                borderRadius: 2,
                                width: 240,
                                boxSizing: 'border-box',

                            }}>
                                {columnMap.columnName}
                            </Box>
                            <Box sx={{width: '100%'}}>
                                {
                                    filteredProjects(columnMap.status).map(
                                        project => (
                                            <ProjectCard
                                                key={project.id}
                                                project={project}
                                                onSelect={() => handleShowProjectDetails(project)}
                                            />
                                        )
                                    )
                                }
                            </Box>
                        </Box>
                    ))}
                </Box>
            </Container>

            <ProjectCreationModal
                open={projectCreationDialogOpen}
                onClose={handleCloseProjectCreationDialog}
            />

            {
                selectedProject &&
                <ProjectDetailsModal
                    open={projectDetailsModalOpen}
                    close={handleCloseProjectDetailsModal}
                    edit={handleEditProjectDetailsModal}
                    project={selectedProject}
                />
            }

            {
                selectedProject &&
                <ProjectEditingModal
                    open={projectEditingModalOpen}
                    onClose={handleCloseProjectEditingModal}
                    project={selectedProject}
                />
            }
            <Footer/>
        </>
    )
}

export default App;

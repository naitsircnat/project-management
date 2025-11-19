import {render, screen, waitFor} from "@testing-library/react";
import axios from "axios";
import userEvent from '@testing-library/user-event';
import App from "../App.jsx";

vi.mock('axios');

const user = userEvent.setup();

describe('App', () => {

    const mockProjects = [
        {
            "id": 1,
            "name": "Demo Project 1",
            "description": "Test project 1",
            "dueDate": "2040-09-01",
            "priority": "MEDIUM",
            "status": "NOT_STARTED",
            "createdAt": "2025-11-07T08:27:36.350+00:00"
        },
        {
            "id": 2,
            "name": "Demo Project 2",
            "description": "Test project 2",
            "dueDate": "2020-09-01",
            "priority": "HARD",
            "status": "NOT_STARTED",
            "createdAt": "2025-11-07T08:33:47.765+00:00"
        }
    ]

    beforeEach(() => {
        vi.mocked(axios.get).mockResolvedValueOnce(
            {
                data: {
                    projects: mockProjects
                }
            }
        )

        render(<App/>)

    })

    it('should render status columns when the page is loaded', () => {

        const expectedStatusColumns = [
            "Not Started", "In Progress", "Ready for QA",
            "Ready for Client Review", "Client Review", "Completed"
        ];

        expectedStatusColumns.forEach(expectedStatus => {
            expect(
                screen.getByRole('heading', {name: expectedStatus})
            ).toBeInTheDocument();
        });
    })

    it('should render project cards when the page is loaded ', async () => {

        const projectCards = await screen.findAllByTestId('project-card');

        expect(projectCards).toHaveLength(mockProjects.length);

        mockProjects.forEach(project => {
            expect(screen.getByText(project.name)).toBeInTheDocument();
            expect(screen.getByText(project.priority)).toBeInTheDocument();
            expect(screen.getByText(project.dueDate)).toBeInTheDocument();
        })
    });

    it('should open project details modal to display correct data when a card is clicked', async () => {

        const firstProjectCard = await screen.findByText('Demo Project 1');
        await user.click(firstProjectCard);

        await waitFor(() => {
            expect(screen.getByText("Test project 1")).toBeInTheDocument();
            expect(screen.getByText("2025-11-07T08:27:36.350+00:00")).toBeInTheDocument();

            expect(screen.getByRole('button', {name: /close/i})).toBeInTheDocument();
            expect(screen.getByRole('button', {name: /edit/i})).toBeInTheDocument();
            expect(screen.getByRole('button', {name: /delete/i})).toBeInTheDocument();

        });
    });

    it('should open project details modal that displays different data when a different card is clicked', async () => {

        const secondProjectCard = await screen.findByText('Demo Project 2');
        await userEvent.click(secondProjectCard);

        await waitFor(() => {
            expect(screen.getByText("Test project 2")).toBeInTheDocument();
            expect(screen.getByText("2025-11-07T08:33:47.765+00:00")).toBeInTheDocument();

            expect(screen.getByRole('button', {name: /close/i})).toBeInTheDocument();
            expect(screen.getByRole('button', {name: /edit/i})).toBeInTheDocument();
            expect(screen.getByRole('button', {name: /delete/i})).toBeInTheDocument();
        });
    });

    it('should close project details modal when cancel button is clicked', async () => {

        const projectCard = await screen.findByText('Demo Project 1');

        await user.click(projectCard);

        const closeButton = await screen.findByRole('button', {name: 'Close'});

        await user.click(closeButton);

        await waitFor(() => {
            expect(screen.queryByRole('button', {name: 'Close'})).not.toBeInTheDocument();
            expect(screen.queryByRole('button', {name: 'Edit'})).not.toBeInTheDocument();
            expect(screen.queryByRole('button', {name: 'Delete'})).not.toBeInTheDocument();
            expect(screen.queryByText('Test project 1')).not.toBeInTheDocument();
        });
    });

    it('should open project creation modal when Add Project button is clicked', async () => {

        const button = await screen.findByRole('button', {name: 'Add Project'})

        await user.click(button);

        await waitFor(() => {
            expect(screen.getByText(/create project/i)).toBeInTheDocument();
            expect(screen.getByText(/project name/i)).toBeInTheDocument();
            expect(screen.getByText(/description/i)).toBeInTheDocument();
            expect(screen.getByText(/due/i)).toBeInTheDocument();
            expect(screen.getByText(/priority/i)).toBeInTheDocument();

            expect(screen.getByRole('button', {name: /cancel/i})).toBeInTheDocument();
            expect(screen.getByRole('button', {name: /create/i})).toBeInTheDocument();
        });
    });

    it('should close project creation modal when Cancel button in the modal is clicked', async () => {

        const projectCreationButton = await screen.findByRole('button', {name: 'Add Project'})

        await user.click(projectCreationButton);

        const cancelButton = await screen.findByRole('button', {name: 'Cancel'});

        await user.click(cancelButton);

        await waitFor(() => {
            expect(screen.queryByText('Create Project')).not.toBeInTheDocument();
            expect(screen.queryByRole('button', {name: 'Cancel'})).not.toBeInTheDocument();
            expect(screen.queryByRole('button', {name: 'Create'})).not.toBeInTheDocument();
        });
    });

    it('should successfully create a project and display it when modal form is filled and submitted', async () => {
        const newProject = {
            id: 3,
            name: 'New Test Project',
            description: 'New project description',
            dueDate: '2040-12-31',
            priority: 'HIGH',
            status: 'NOT_STARTED',
            createdAt: '2025-11-08T10:00:00.000+00:00'
        };

        vi.mocked(axios.post).mockResolvedValueOnce({data: newProject});

        const updatedProjects = [...mockProjects, newProject];

        vi.mocked(axios.get).mockResolvedValueOnce({
            data: {projects: updatedProjects}
        })

        const projectCreationButton = await screen.findByRole('button', {name: 'Add Project'});
        await user.click(projectCreationButton);

        await expect(screen.getByText('Create Project')).toBeInTheDocument();

        await user.type(screen.getByLabelText(/project name/i), 'New Test Project');

        await user.type(screen.getByLabelText(/description/i), 'New project description');

        await user.click(screen.getByLabelText(/priority/i));
        const highPriorityOption = await screen.findByRole('option', {name: 'HIGH'});
        await user.click(highPriorityOption);

        const calendarIcon = screen.getByTestId('CalendarIcon');
        await user.click(calendarIcon);
        const dateOption = await screen.getByRole('gridcell', {name: '22'});
        await user.click(dateOption);

        const createButton = await screen.getByRole('button', {name: 'Create'});
        await user.click(createButton);

        await waitFor(() => {
            expect(axios.post).toHaveBeenCalledWith(
                expect.any(String),
                expect.objectContaining({
                    name: 'New Test Project',
                    description: 'New project description',
                    priority: 'HIGH'
                }),
                expect.any(Object)
            );
        });

        await waitFor(() => {
            expect(screen.queryByText('Create Project')).not.toBeInTheDocument();
        });

        await expect(screen.getByText('New Test Project')).toBeInTheDocument();

        const projectCards = await screen.findAllByTestId('project-card');
        expect(projectCards).toHaveLength(3);
    });

    it('should close project details modal when edit button is clicked, and open an edit project modal populated with original values', async () => {
        const firstProjectCard = await screen.findByText('Demo Project 1');
        await user.click(firstProjectCard);

        const editButton = screen.getByRole('button', {name: /edit/i});

        await user.click(editButton);

        await waitFor(() => {
            expect(screen.queryByRole('button', {name: /close/i})).not.toBeInTheDocument();
            expect(screen.queryByRole('button', {name: /edit/i})).not.toBeInTheDocument();
            expect(screen.queryByRole('button', {name: /delete/i})).not.toBeInTheDocument();
        });

        await waitFor(() => {
            expect(screen.getByText(/edit project/i)).toBeInTheDocument();

            expect(screen.getByText(/description/i)).toBeInTheDocument();
            expect(screen.getByDisplayValue("Test project 1")).toBeInTheDocument();

            expect(screen.getByText(/priority/i)).toBeInTheDocument();
            expect(screen.getByDisplayValue("MEDIUM")).toBeInTheDocument();

            expect(screen.getByRole('button', {name: /cancel/i})).toBeInTheDocument();
            expect(screen.getByRole('button', {name: /save/i})).toBeInTheDocument();
        });
    });

    it('should close project editing modal when cancel button is clicked', async () => {
        const firstProjectCard = await screen.findByText('Demo Project 1');
        await user.click(firstProjectCard);

        const editButton = screen.getByRole('button', {name: /edit/i});
        await user.click(editButton);

        const cancelButton = screen.getByRole('button', {name: /cancel/i});
        await user.click(cancelButton);

        await waitFor(() => {
            expect(screen.queryByRole('button', {name: /cancel/i})).not.toBeInTheDocument();
            expect(screen.queryByRole('button', {name: /save/i})).not.toBeInTheDocument();
        });
    });

    it('should display project card containing updated details on main page when details are edited and save button is clicked  ', async () => {
        const firstProjectCard = await screen.findByText('Demo Project 1');
        await user.click(firstProjectCard);

        const editButton = screen.getByRole('button', {name: /edit/i});
        await user.click(editButton);
        
    });
})

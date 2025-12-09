package com.example.demo.service;

import com.example.demo.exception.ProjectNotFoundException;
import com.example.demo.model.Priority;
import com.example.demo.model.Project;
import com.example.demo.model.Status;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.demo.model.Priority.LOW;
import static com.example.demo.model.Priority.MEDIUM;
import static com.example.demo.model.Status.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectServiceImpl projectService;

    public static long ID = 1;

    public static String NAME = "testName";
    public static String UPDATED_NAME = "updatedTestName";

    public static String TEST_DESCRIPTION = "testDescription";
    public static String UPDATED_DESCRIPTION = "updatedTestDescription";

    public static Date DUE_DATE = Date.valueOf("2025-12-31");

    public static Priority PRIORITY = Priority.HIGH;

    public static Status STATUS = IN_PROGRESS;

    public static Status UPDATED_STATUS = READY_FOR_QA;

    public static Timestamp CREATED_AT = Timestamp.valueOf("2025-12-31 12:00:00");

    public static final Project project = new Project(ID, NAME, TEST_DESCRIPTION, DUE_DATE, PRIORITY, STATUS, CREATED_AT);
    public static final Project updatedProject = new Project(ID, UPDATED_NAME, UPDATED_DESCRIPTION, DUE_DATE, PRIORITY, UPDATED_STATUS, CREATED_AT);

    @Test
    public void shouldInsertProject() {

        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Project projectToBeInserted = projectService.insertProject(
                NAME,
                TEST_DESCRIPTION,
                DUE_DATE,
                PRIORITY
        );

        ArgumentCaptor<Project> captor = ArgumentCaptor.forClass(Project.class);
        verify(projectRepository).save(captor.capture());

        Project capturedProject = captor.getValue();

        assertEquals(capturedProject.getName(), NAME);
        assertEquals(capturedProject.getDescription(), TEST_DESCRIPTION);
        assertEquals(capturedProject.getDueDate(), DUE_DATE);
        assertEquals(capturedProject.getPriority(), PRIORITY);

        assertEquals(project, projectToBeInserted);
    }

    @Test
    public void shouldGetProjectById() {

        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));

        Optional<Project> retrievedProject = projectService.getProjectById(ID);

        verify(projectRepository).findById(ID);

        assertEquals(retrievedProject.get(), project);
    }

    @Test
    public void shouldGetAllProjects() {

        long id1 = 456;
        String name1 = "Project1";
        String description1 = "Description1";
        Date dueDate1 = Date.valueOf("2026-12-31");
        Priority priority1 = MEDIUM;
        Status status1 = IN_PROGRESS;
        Timestamp createdAt1 = Timestamp.valueOf("2026-12-31 12:00:00");
        Project project1 = new Project(id1, name1, description1, dueDate1, priority1, status1, createdAt1);

        long id2 = 789;
        String name2 = "Project2";
        String description2 = "Description2";
        Date dueDate2 = Date.valueOf("2027-12-31");
        Priority priority2 = LOW;
        Status status2 = READY_FOR_CLIENT_REVIEW;
        Timestamp createdAt2 = Timestamp.valueOf("2027-12-31 12:00:00");
        Project project2 = new Project(id2, name2, description2, dueDate2, priority2, status2, createdAt2);

        List<Project> projects = Arrays.asList(project1, project2);

        when(projectRepository.findAll()).thenReturn(projects);

        List<Project> retrievedProjects = projectService.getAllProjects();

        verify(projectRepository).findAll();

        assertEquals(retrievedProjects, projects);
    }

    @Test
    public void shouldUpdateProject() {

        when(projectRepository.findById(ID)).thenReturn(Optional.of(project));

        when(projectRepository.save(any(Project.class))).thenReturn(updatedProject);

        Project result = projectService.updateProject(
                ID,
                UPDATED_NAME,
                UPDATED_DESCRIPTION,
                DUE_DATE,
                PRIORITY,
                UPDATED_STATUS
        );

        verify(projectRepository).findById(ID);

        ArgumentCaptor<Project> captor = ArgumentCaptor.forClass(Project.class);

        verify(projectRepository).save(captor.capture());

        Project capturedProject = captor.getValue();

        assertEquals(capturedProject.getName(), UPDATED_NAME);
        assertEquals(capturedProject.getDescription(), UPDATED_DESCRIPTION);
        assertEquals(capturedProject.getDueDate(), DUE_DATE);
        assertEquals(capturedProject.getPriority(), PRIORITY);
        assertEquals(capturedProject.getStatus(), UPDATED_STATUS);

        assertEquals(result, updatedProject);
    }

    @Test
    public void updateProject_shouldThrowExceptionWhenProjectNotFound() {
        when(projectRepository.findById(1000L)).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () ->
                projectService.updateProject(1000L, NAME, TEST_DESCRIPTION, DUE_DATE, PRIORITY, STATUS));
    }

    @Test
    public void shouldDeleteProject() {
        when(projectRepository.findById(ID)).thenReturn(Optional.of(project));

        projectService.deleteProject(ID);

        verify(projectRepository).findById(ID);
        verify(projectRepository).delete(project);
    }

    @Test
    public void deleteProject_shouldThrowExceptionWhenProjectNotFound() {
        when(projectRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () ->
                projectService.deleteProject(999L)
        );

        verify(projectRepository).findById(999L);
        verify(projectRepository, never()).delete(any());
    }
}

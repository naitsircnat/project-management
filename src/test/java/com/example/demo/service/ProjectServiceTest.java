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

    public static long TEST_ID = 1;

    public static String TEST_NAME = "testName";
    public static String UPDATED_TEST_NAME = "updatedTestName";

    public static String TEST_DESCRIPTION = "testDescription";
    public static String UPDATED_TEST_DESCRIPTION = "updatedTestDescription";

    public static Date TEST_DUE_DATE = Date.valueOf("2025-12-31");

    public static Priority TEST_PRIORITY = Priority.HIGH;

    public static Status TEST_STATUS = IN_PROGRESS;

    public static Status UPDATED_TEST_STATUS = READY_FOR_QA;

    public static Timestamp TEST_CREATED_AT = Timestamp.valueOf("2025-12-31 12:00:00");

    public static final Project project = new Project(TEST_ID, TEST_NAME, TEST_DESCRIPTION, TEST_DUE_DATE, TEST_PRIORITY, TEST_STATUS, TEST_CREATED_AT);
    public static final Project updatedProject = new Project(TEST_ID, UPDATED_TEST_NAME, UPDATED_TEST_DESCRIPTION, TEST_DUE_DATE, TEST_PRIORITY, UPDATED_TEST_STATUS, TEST_CREATED_AT);

    @Test
    public void shouldInsertProject() {

        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Project projectToBeInserted = projectService.insertProject(
                TEST_NAME,
                TEST_DESCRIPTION,
                TEST_DUE_DATE,
                TEST_PRIORITY
        );

        ArgumentCaptor<Project> captor = ArgumentCaptor.forClass(Project.class);
        verify(projectRepository).save(captor.capture());

        Project capturedProject = captor.getValue();

        assertEquals(capturedProject.getName(), TEST_NAME);
        assertEquals(capturedProject.getDescription(), TEST_DESCRIPTION);
        assertEquals(capturedProject.getDueDate(), TEST_DUE_DATE);
        assertEquals(capturedProject.getPriority(), TEST_PRIORITY);

        assertEquals(project, projectToBeInserted);
    }

    @Test
    public void shouldGetProjectById() {

        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));

        Optional<Project> retrievedProject = projectService.getProjectById(TEST_ID);

        verify(projectRepository).findById(TEST_ID);

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

        when(projectRepository.findById(TEST_ID)).thenReturn(Optional.of(project));

        when(projectRepository.save(any(Project.class))).thenReturn(updatedProject);

        Project result = projectService.updateProject(
                TEST_ID,
                UPDATED_TEST_NAME,
                UPDATED_TEST_DESCRIPTION,
                TEST_DUE_DATE,
                TEST_PRIORITY,
                UPDATED_TEST_STATUS
        );

        verify(projectRepository).findById(TEST_ID);

        ArgumentCaptor<Project> captor = ArgumentCaptor.forClass(Project.class);

        verify(projectRepository).save(captor.capture());

        Project capturedProject = captor.getValue();

        assertEquals(capturedProject.getName(), UPDATED_TEST_NAME);
        assertEquals(capturedProject.getDescription(), UPDATED_TEST_DESCRIPTION);
        assertEquals(capturedProject.getDueDate(), TEST_DUE_DATE);
        assertEquals(capturedProject.getPriority(), TEST_PRIORITY);
        assertEquals(capturedProject.getStatus(), UPDATED_TEST_STATUS);

        assertEquals(result, updatedProject);
    }

    @Test
    public void updateProject_shouldThrowExceptionWhenProjectNotFound() {
        when(projectRepository.findById(1000L)).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () ->
                projectService.updateProject(1000L, TEST_NAME, TEST_DESCRIPTION, TEST_DUE_DATE, TEST_PRIORITY, TEST_STATUS));
    }

    @Test
    public void shouldDeleteProject() {
        when(projectRepository.findById(TEST_ID)).thenReturn(Optional.of(project));

        projectService.deleteProject(TEST_ID);

        verify(projectRepository).findById(TEST_ID);
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

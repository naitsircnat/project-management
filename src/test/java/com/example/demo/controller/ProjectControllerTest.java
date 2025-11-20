package com.example.demo.controller;

import com.example.demo.controller.responsedtos.ProjectResponseBody;
import com.example.demo.controller.responsedtos.ProjectsResponseBody;
import com.example.demo.exception.ProjectNotFoundException;
import com.example.demo.model.Priority;
import com.example.demo.model.Project;
import com.example.demo.model.Status;
import com.example.demo.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.demo.controller.responsedtos.mappers.ProjectResponseBodyMapper.fromProject;
import static com.example.demo.model.Priority.*;
import static com.example.demo.model.Status.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService projectService;

    public static String PROJECT_REQUEST_BODY = "{\n" +
            "\"name\": \"%s\",\n" +
            "\"description\": \"%s\",\n" +
            "\"dueDate\": \"%s\",\n" +
            "\"priority\": \"%s\",\n" +
            "\"status\": \"%s\"\n" +
            "}";

    public static String PROJECT_RESPONSE_BODY = "{\n" +
            "\"id\": %d,\n" +
            "\"name\": \"%s\",\n" +
            "\"description\": \"%s\",\n" +
            "\"dueDate\": \"%s\",\n" +
            "\"priority\": \"%s\",\n" +
            "\"status\": \"%s\",\n" +
            "\"createdAt\": \"%s\"\n" +
            "}";

    public static long TEST_ID = 1;

    public static String ENDPOINT = "/projects";
    public static String PROJECT_ENDPOINT = ENDPOINT + "/" + TEST_ID;

    public static String TEST_NAME = "testName";
    public static String UPDATED_TEST_NAME = "updatedTestName";

    public static String TEST_DESCRIPTION = "testDescription";
    public static String UPDATED_TEST_DESCRIPTION = "updatedTestDescription";

    public static Date TEST_DUE_DATE = Date.valueOf("2025-12-31");
    public static String TEST_DUE_DATE_STR = TEST_DUE_DATE.toString();

    public static Priority TEST_PRIORITY = HIGH;
    public static String TEST_PRIORITY_STR = TEST_PRIORITY.name();

    public static Status TEST_STATUS = IN_PROGRESS;
    public static String TEST_STATUS_STR = IN_PROGRESS.name();

    public static Status UPDATED_TEST_STATUS = READY_FOR_QA;
    public static String UPDATED_TEST_STATUS_STR = READY_FOR_QA.name();


    public static Timestamp TEST_CREATED_AT = Timestamp.valueOf("2025-12-31 12:00:00");
    public static String TEST_CREATED_AT_STR = "2025-12-31T04:00:00.000+00:00";

    public static final Project project = new Project(TEST_ID, TEST_NAME, TEST_DESCRIPTION, TEST_DUE_DATE, TEST_PRIORITY, TEST_STATUS, TEST_CREATED_AT);
    public static final Project updatedProject = new Project(TEST_ID, UPDATED_TEST_NAME, UPDATED_TEST_DESCRIPTION, TEST_DUE_DATE, TEST_PRIORITY, UPDATED_TEST_STATUS, TEST_CREATED_AT);


    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldCreateNewProject() throws Exception {

        when(projectService.insertProject(
                eq(TEST_NAME),
                eq(TEST_DESCRIPTION),
                any(Date.class),
                any(Priority.class)))
                .thenReturn(project);

        String createProjectRequestJson = String.format(
                PROJECT_REQUEST_BODY,
                TEST_NAME,
                TEST_DESCRIPTION,
                TEST_DUE_DATE_STR,
                TEST_PRIORITY_STR);

        String expectedResponseBody = String.format(
                PROJECT_RESPONSE_BODY,
                TEST_ID,
                TEST_NAME,
                TEST_DESCRIPTION,
                TEST_DUE_DATE_STR,
                TEST_PRIORITY_STR,
                TEST_STATUS_STR,
                TEST_CREATED_AT_STR);

        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createProjectRequestJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedResponseBody));

        verify(projectService).insertProject(
                eq(TEST_NAME),
                eq(TEST_DESCRIPTION),
                any(Date.class),
                any(Priority.class));
    }

    @Test
    public void shouldGetProjectById() throws Exception {

        when(projectService.getProjectById(anyLong())).thenReturn(Optional.of(project));

        String expectedResponseBody = String.format(
                PROJECT_RESPONSE_BODY,
                TEST_ID,
                TEST_NAME,
                TEST_DESCRIPTION,
                TEST_DUE_DATE_STR,
                TEST_PRIORITY_STR,
                TEST_CREATED_AT_STR);

        mockMvc.perform(get(ENDPOINT + "/" + TEST_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));

        verify(projectService).getProjectById(TEST_ID);
    }

    @Test
    public void shouldGetAllProjects() throws Exception {
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

        when(projectService.getAllProjects()).thenReturn(projects);

        List<ProjectResponseBody> projectResponseBodyList = projects.stream()
                .map(project -> fromProject(project)
                )
                .toList();

        ProjectsResponseBody expectedProjectsResponseBody = new ProjectsResponseBody(projectResponseBodyList);

        String expectedResponseJson = objectMapper.writeValueAsString(expectedProjectsResponseBody);

        mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseJson));
    }

    @Test
    public void shouldUpdateProject() throws Exception {

        when(projectService.updateProject(
                eq(TEST_ID),
                eq(UPDATED_TEST_NAME),
                eq(UPDATED_TEST_DESCRIPTION),
                any(Date.class),
                any(Priority.class),
                any(Status.class)
        ))
                .thenReturn(updatedProject);

        String updateProjectRequestJson = String.format(
                PROJECT_REQUEST_BODY,
                UPDATED_TEST_NAME,
                UPDATED_TEST_DESCRIPTION,
                TEST_DUE_DATE_STR,
                TEST_PRIORITY_STR,
                UPDATED_TEST_STATUS_STR);

        String expectedResponseBody = String.format(
                PROJECT_RESPONSE_BODY,
                TEST_ID,
                UPDATED_TEST_NAME,
                UPDATED_TEST_DESCRIPTION,
                TEST_DUE_DATE_STR,
                TEST_PRIORITY_STR,
                UPDATED_TEST_STATUS_STR,
                TEST_CREATED_AT_STR);

        mockMvc.perform(put(PROJECT_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateProjectRequestJson))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));

        verify(projectService).updateProject(
                eq(TEST_ID),
                eq(UPDATED_TEST_NAME),
                eq(UPDATED_TEST_DESCRIPTION),
                any(Date.class),
                any(Priority.class),
                any(Status.class));
    }

    @Test
    public void updateProject_shouldReturn404ErrorWhenProjectNotFound() throws Exception {
        when(projectService.updateProject(eq(1000L), any(), any(), any(), any(), any()))
                .thenThrow(new ProjectNotFoundException("Project not found"));

        String updateProjectRequestJson = String.format(
                PROJECT_REQUEST_BODY,
                UPDATED_TEST_NAME,
                UPDATED_TEST_DESCRIPTION,
                TEST_DUE_DATE_STR,
                TEST_PRIORITY_STR,
                UPDATED_TEST_STATUS_STR);

        mockMvc.perform(put("/projects/1000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateProjectRequestJson))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeleteProject() throws Exception {
        doNothing().when(projectService).deleteProject(TEST_ID);

        mockMvc.perform(delete(PROJECT_ENDPOINT))
                .andExpect(status().isNoContent());

        verify(projectService).deleteProject(TEST_ID);
    }

    @Test
    public void shouldReturn404WhenDeletingNonExistentProject() throws Exception {

        doThrow(new ProjectNotFoundException("Project not found"))
                .when(projectService).deleteProject(999L);

        mockMvc.perform(delete("/projects/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Project not found"));

        verify(projectService).deleteProject(999L);
    }
}


package com.example.demo;

import com.example.demo.controller.requestdtos.CreateProjectRequestBody;
import com.example.demo.controller.requestdtos.UpdateProjectRequestBody;
import com.example.demo.controller.responsedtos.ProjectResponseBody;
import com.example.demo.controller.responsedtos.ProjectsResponseBody;
import com.example.demo.model.Priority;
import com.example.demo.model.Status;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ComponentScan(basePackages = "com.example.demo")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate;

    @Autowired
    private TestH2Repository h2Repository;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setUp() {
        baseUrl = baseUrl.concat(":").concat(port + "").concat("/projects");
    }

    @Test
    public void createProject_WithValidRequest_ShouldReturn201Created() {
        CreateProjectRequestBody createProjectRequestBody = new CreateProjectRequestBody(
                "New Project",
                "Project Description",
                Date.valueOf(LocalDate.now().plusDays(30)),
                Priority.HIGH
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateProjectRequestBody> requestEntity = new HttpEntity<>(createProjectRequestBody, headers);

        ResponseEntity<ProjectResponseBody> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.POST,
                requestEntity,
                ProjectResponseBody.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();
        assertThat(response.getHeaders().getLocation().getPath()).matches("/projects/\\d+");

        ProjectResponseBody responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.id()).isNotNull();
        assertThat(responseBody.name()).isEqualTo("New Project");
        assertThat(responseBody.description()).isEqualTo("Project Description");
        assertThat(responseBody.priority()).isEqualTo(Priority.HIGH);
        assertThat(responseBody.status()).isNotNull();
        assertThat(responseBody.createdAt()).isNotNull();

        assertThat(h2Repository.count()).isEqualTo(1);
        var savedProject = h2Repository.findById(responseBody.id());
        assertThat(savedProject).isPresent();
        assertThat(savedProject.get().getName()).isEqualTo("New Project");
    }

    @Test
    public void getProjects_ShouldReturnListOfProjects() {

        CreateProjectRequestBody project1 = new CreateProjectRequestBody(
                "E-commerce Platform",
                "Build online shopping platform",
                Date.valueOf(LocalDate.now().plusDays(30)),
                Priority.HIGH
        );

        CreateProjectRequestBody project2 = new CreateProjectRequestBody(
                "Mobile App",
                "Develop React Native application",
                Date.valueOf(LocalDate.now().plusDays(60)),
                Priority.MEDIUM
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        restTemplate.exchange(baseUrl, HttpMethod.POST, new HttpEntity<>(project1, headers), ProjectResponseBody.class);
        restTemplate.exchange(baseUrl, HttpMethod.POST, new HttpEntity<>(project2, headers), ProjectResponseBody.class);

        ResponseEntity<ProjectsResponseBody> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                null,
                ProjectsResponseBody.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ProjectsResponseBody responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.projects()).isNotNull();
        assertThat(responseBody.projects()).hasSize(2);

        ProjectResponseBody firstProject = responseBody.projects().get(0);
        assertThat(firstProject.id()).isNotNull();
        assertThat(firstProject.name()).isEqualTo("E-commerce Platform");
        assertThat(firstProject.description()).isEqualTo("Build online shopping platform");
        assertThat(firstProject.priority()).isEqualTo(Priority.HIGH);
        assertThat(firstProject.status()).isNotNull();
        assertThat(firstProject.createdAt()).isNotNull();
        assertThat(firstProject.dueDate()).isNotNull();

        ProjectResponseBody secondProject = responseBody.projects().get(1);
        assertThat(secondProject.id()).isNotNull();
        assertThat(secondProject.name()).isEqualTo("Mobile App");
        assertThat(secondProject.description()).isEqualTo("Develop React Native application");
        assertThat(secondProject.priority()).isEqualTo(Priority.MEDIUM);
        assertThat(secondProject.status()).isNotNull();
        assertThat(secondProject.createdAt()).isNotNull();
    }

    @Test
    public void getProjectsById_RequestedProjectExists_ShouldReturnProject() {

        CreateProjectRequestBody project1 = new CreateProjectRequestBody(
                "E-commerce Platform",
                "Build online shopping platform",
                Date.valueOf(LocalDate.now().plusDays(30)),
                Priority.HIGH
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        restTemplate.exchange(
                baseUrl,
                HttpMethod.POST,
                new HttpEntity<>(project1, headers),
                ProjectResponseBody.class
        );

        ResponseEntity<ProjectResponseBody> response = restTemplate.exchange(
                baseUrl + "/{id}",
                HttpMethod.GET,
                null,
                ProjectResponseBody.class, 1
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ProjectResponseBody projectResponseBody = response.getBody();

        assertThat(projectResponseBody).isNotNull();

        assertThat(projectResponseBody.id()).isNotNull();
        assertThat(projectResponseBody.name()).isEqualTo("E-commerce Platform");
        assertThat(projectResponseBody.description()).isEqualTo("Build online shopping platform");
        assertThat(projectResponseBody.priority()).isEqualTo(Priority.HIGH);
        assertThat(projectResponseBody.status()).isNotNull();
        assertThat(projectResponseBody.createdAt()).isNotNull();
        assertThat(projectResponseBody.dueDate()).isNotNull();
    }

    @Test
    public void editProject_WithValidRequest_ShouldReturn201Created() {
        CreateProjectRequestBody createProjectRequestBody = new CreateProjectRequestBody(
                "New Project",
                "Project Description",
                Date.valueOf(LocalDate.now().plusDays(30)),
                Priority.HIGH
        );

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateProjectRequestBody> requestEntity = new HttpEntity<>(createProjectRequestBody, headers);

        restTemplate.exchange(
                baseUrl,
                HttpMethod.POST,
                requestEntity,
                ProjectResponseBody.class
        );

        UpdateProjectRequestBody updateProjectRequestBody = new UpdateProjectRequestBody(
                1,
                "New Project",
                "Updated Project Description",
                Date.valueOf(LocalDate.now().plusDays(30)),
                Priority.HIGH,
                Status.IN_PROGRESS
        );

        HttpEntity<UpdateProjectRequestBody> requestEntityUpdated = new HttpEntity<>(updateProjectRequestBody, headers);

        ResponseEntity<ProjectResponseBody> response = restTemplate.exchange(
                baseUrl + "/{id}",
                HttpMethod.PUT,
                requestEntityUpdated,
                ProjectResponseBody.class,
                1
        );

        ProjectResponseBody responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.id()).isNotNull();
        assertThat(responseBody.name()).isEqualTo("New Project");
        assertThat(responseBody.description()).isEqualTo("Updated Project Description");
        assertThat(responseBody.dueDate()).isNotNull();
        assertThat(responseBody.priority()).isEqualTo(Priority.HIGH);
        assertThat(responseBody.status()).isEqualTo(Status.IN_PROGRESS);
        assertThat(responseBody.createdAt()).isNotNull();


        var updatedProject = h2Repository.findById(responseBody.id());

        assertThat(updatedProject).isNotNull();
        assertThat(updatedProject.get().getName()).isEqualTo("New Project");
        assertThat(updatedProject.get().getDescription()).isEqualTo("Updated Project Description");
        assertThat(updatedProject.get().getStatus()).isEqualTo(Status.IN_PROGRESS);
    }

    @Test
    public void deleteProject_ShouldRemoveProjectFromDb() {
        CreateProjectRequestBody createProjectRequestBody = new CreateProjectRequestBody(
                "New Project",
                "Project Description",
                Date.valueOf(LocalDate.now().plusDays(30)),
                Priority.HIGH
        );

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateProjectRequestBody> requestEntity = new HttpEntity<>(createProjectRequestBody, headers);

        restTemplate.exchange(
                baseUrl,
                HttpMethod.POST,
                requestEntity,
                ProjectResponseBody.class
        );

        UpdateProjectRequestBody updateProjectRequestBody = new UpdateProjectRequestBody(
                1,
                "New Project",
                "Updated Project Description",
                Date.valueOf(LocalDate.now().plusDays(30)),
                Priority.HIGH,
                Status.IN_PROGRESS
        );

        HttpEntity<UpdateProjectRequestBody> requestEntityUpdated = new HttpEntity<>(updateProjectRequestBody, headers);

        restTemplate.exchange(
                baseUrl + "/{id}",
                HttpMethod.PUT,
                requestEntityUpdated,
                ProjectResponseBody.class,
                1
        );

        int recordCount = h2Repository.findAll().size();
        assertThat(recordCount).isEqualTo(1);
        restTemplate.delete(baseUrl + "/{id}", 1);
        assertEquals(0, h2Repository.findAll().size());
    }


}

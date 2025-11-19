package com.example.demo.controller;

import com.example.demo.controller.requestdtos.CreateProjectRequestBody;
import com.example.demo.controller.requestdtos.UpdateProjectRequestBody;
import com.example.demo.controller.responsedtos.ProjectResponseBody;
import com.example.demo.controller.responsedtos.ProjectsResponseBody;
import com.example.demo.model.Project;
import com.example.demo.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static com.example.demo.controller.responsedtos.mappers.ProjectResponseBodyMapper.fromProject;

@RequestMapping("/projects")
@RestController
public class ProjectController {
    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectResponseBody> createProject(@Valid @RequestBody CreateProjectRequestBody createProjectRequestBody) {

        Project project = projectService.insertProject(
                createProjectRequestBody.name(),
                createProjectRequestBody.description(),
                createProjectRequestBody.dueDate(),
                createProjectRequestBody.priority()
        );

        ProjectResponseBody projectResponseBody = new ProjectResponseBody(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getDueDate(),
                project.getPriority(),
                project.getStatus(),
                project.getCreatedAt()
        );

        URI location = URI.create("/projects/" + project.getId());

        return ResponseEntity.created(location).body(projectResponseBody);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseBody> getProjectById(@PathVariable long id) {

        Optional<Project> projectOpt = projectService.getProjectById(id);

        if (projectOpt.isPresent()) {

            Project actualProject = projectOpt.get();

            ProjectResponseBody projectResponseBody = fromProject(actualProject);

            return ResponseEntity.ok(projectResponseBody);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<ProjectsResponseBody> getAllProjects() {

        List<Project> projects = projectService.getAllProjects();

        List<ProjectResponseBody> projectResponseBodyList = projects.stream().map(project -> fromProject(project)
        ).toList();

        return ResponseEntity.ok(new ProjectsResponseBody(projectResponseBodyList));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseBody> updateProject(@PathVariable long id, @Valid @RequestBody UpdateProjectRequestBody updateProjectRequestBody) {

        Project project = projectService.updateProject(
                id,
                updateProjectRequestBody.name(),
                updateProjectRequestBody.description(),
                updateProjectRequestBody.dueDate(),
                updateProjectRequestBody.priority(),
                updateProjectRequestBody.status()
        );

        ProjectResponseBody projectResponseBody = fromProject(project);

        return ResponseEntity.ok(projectResponseBody);
    }

    
}


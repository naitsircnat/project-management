package com.example.demo.service.impl;

import com.example.demo.exception.ProjectNotFoundException;
import com.example.demo.model.Priority;
import com.example.demo.model.Project;
import com.example.demo.model.Status;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.service.ProjectService;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Project insertProject(String name, String description, Date dueDate, Priority priority) {

        Project project = Project.builder()
                .name(name)
                .description(description)
                .dueDate(dueDate)
                .priority(priority)
                .build();

        return projectRepository.save(project);
    }

    @Override
    public Optional<Project> getProjectById(long id) {
        return projectRepository.findById(id);
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Project updateProject(long id, String name, String description, Date dueDate, Priority priority, Status status) {
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found")
                );

        existingProject.setName(name);
        existingProject.setDescription(description);
        existingProject.setDueDate(dueDate);
        existingProject.setPriority(priority);
        existingProject.setStatus(status);

        return projectRepository.save(existingProject);
    }

    @Override
    public void deleteProject(long id) {
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));
        projectRepository.delete(existingProject);
    }
}

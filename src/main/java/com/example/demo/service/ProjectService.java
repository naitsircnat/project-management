package com.example.demo.service;

import com.example.demo.model.Priority;
import com.example.demo.model.Project;
import com.example.demo.model.Status;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface ProjectService {
    public Project insertProject(String name, String description, Date dueDate, Priority priority);

    public Optional<Project> getProjectById(long id);

    public List<Project> getAllProjects();

    public Project updateProject(
            long id,
            String name,
            String description,
            Date dueDate,
            Priority priority,
            Status status
    );

    public void deleteProject(long id);

    ;
}

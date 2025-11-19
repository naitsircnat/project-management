package com.example.demo.controller.responsedtos.mappers;

import com.example.demo.controller.responsedtos.ProjectResponseBody;
import com.example.demo.model.Project;

public class ProjectResponseBodyMapper {
    public static ProjectResponseBody fromProject(Project project
    ) {
        return new ProjectResponseBody(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getDueDate(),
                project.getPriority(),
                project.getStatus(),
                project.getCreatedAt()
        );
    }
}

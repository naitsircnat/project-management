package com.example.demo.controller.requestdtos.mappers;

import com.example.demo.controller.requestdtos.CreateProjectRequestBody;
import com.example.demo.model.Priority;

import java.sql.Date;

public class ProjectRequestBodyMapper {
    public static CreateProjectRequestBody fromProject(
            String name,
            String description,
            Date dueDate,
            Priority priority
    ) {
        return new CreateProjectRequestBody(name, description, dueDate, priority);
    }
}

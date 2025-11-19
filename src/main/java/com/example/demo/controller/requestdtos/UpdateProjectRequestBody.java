package com.example.demo.controller.requestdtos;

import com.example.demo.model.Priority;
import com.example.demo.model.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.sql.Date;

public record UpdateProjectRequestBody(
        long id,
        @NotBlank String name,
        String description,
        @NotNull Date dueDate,
        @NotNull Priority priority,
        @NotNull Status status
) {
}

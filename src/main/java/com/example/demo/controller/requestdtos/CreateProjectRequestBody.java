package com.example.demo.controller.requestdtos;

import com.example.demo.model.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.sql.Date;

public record CreateProjectRequestBody(
        @NotBlank String name,
        String description,
        @NotNull Date dueDate,
        @NotNull Priority priority
) {
}

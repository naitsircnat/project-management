package com.example.demo.controller.responsedtos;

import com.example.demo.model.Priority;
import com.example.demo.model.Status;

import java.sql.Date;
import java.sql.Timestamp;

public record ProjectResponseBody(
        long id,
        String name,
        String description,
        Date dueDate,
        Priority priority,
        Status status,
        Timestamp createdAt
) {
}

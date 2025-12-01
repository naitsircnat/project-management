package com.example.demo;

import com.example.demo.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestH2Repository extends JpaRepository<Project, Long> {
}

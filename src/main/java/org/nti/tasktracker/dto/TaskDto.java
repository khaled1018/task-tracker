package org.nti.tasktracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TaskDto(
        @NotNull(message = "Title is required")
        @NotBlank(message = "Title is required")
        String title,

        String description,

        LocalDate dueDate
) { }

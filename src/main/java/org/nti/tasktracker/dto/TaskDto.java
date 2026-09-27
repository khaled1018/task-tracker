package org.nti.tasktracker;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TaskDto(
        @NotNull
        @NotBlank(message = "Title is required")
        String title,

        String description,

        LocalDate dueDate
) { }

package ru.itmo.secure_api.note;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateNoteRequest(
        @NotBlank
        @Size(max = 2000)
        String content
) {
}
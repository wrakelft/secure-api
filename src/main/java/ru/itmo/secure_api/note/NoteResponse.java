package ru.itmo.secure_api.note;

import org.springframework.web.util.HtmlUtils;

public record NoteResponse(
        Long id,
        String content
) {
    public static NoteResponse from(Note note) {
        return new NoteResponse(
                note.getId(),
                HtmlUtils.htmlEscape(note.getContent(), "UTF-8")
        );
    }
}
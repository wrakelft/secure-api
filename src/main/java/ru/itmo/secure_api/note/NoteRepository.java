package ru.itmo.secure_api.note;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findAllByOwner_IdOrderByIdAsc(Long ownerId);
}
package ru.itmo.secure_api.note;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.itmo.secure_api.user.AppUser;
import ru.itmo.secure_api.user.UserRepository;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public NoteService(
            NoteRepository noteRepository,
            UserRepository userRepository
    ) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<NoteResponse> getNotes(String subject) {
        AppUser owner = getCurrentUser(subject);

        return noteRepository
                .findAllByOwner_IdOrderByIdAsc(owner.getId())
                .stream()
                .map(NoteResponse::from)
                .toList();
    }

    @Transactional
    public NoteResponse createNote(
            String subject,
            CreateNoteRequest request
    ) {
        AppUser owner = getCurrentUser(subject);
        Note note = new Note(request.content(), owner);

        return NoteResponse.from(noteRepository.save(note));
    }

    private AppUser getCurrentUser(String subject) {
        Long userId;

        try {
            userId = Long.valueOf(subject);
        } catch (NumberFormatException exception) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid token subject"
            );
        }

        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "User no longer exists"
                ));
    }
}
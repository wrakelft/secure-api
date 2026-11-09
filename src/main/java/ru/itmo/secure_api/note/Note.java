package ru.itmo.secure_api.note;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import ru.itmo.secure_api.user.AppUser;

@Entity
@Table(name = "notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private AppUser owner;

    protected Note() {
    }

    public Note(String content, AppUser owner) {
        this.content = content;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public AppUser getOwner() {
        return owner;
    }
}
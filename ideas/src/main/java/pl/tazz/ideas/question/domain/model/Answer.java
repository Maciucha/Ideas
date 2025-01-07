package pl.tazz.ideas.question.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "answers")
@Getter
@Setter
@ToString
public class Answer {

    @Id
    private UUID id;

    private String name;
    private String content;
    private String username;
    private String email;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }

    @ManyToOne
    private Question question;

    public Answer() {
        this.id = UUID.randomUUID();
    }

    public Answer(String name, String content, String username, String email, Question question) {
        this();
        this.name = name;
        this.content = content;
        this.question = question;
        this.username = username;
        this.email = email;
    }
}

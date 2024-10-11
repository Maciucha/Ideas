package pl.tazz.ideas.question.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.tazz.ideas.question.domain.model.Answer;

import java.util.List;
import java.util.UUID;

@Repository
public interface AnswerRepository extends JpaRepository <Answer, UUID> {
    List<Answer> findByQuestionId(UUID questionId);
}

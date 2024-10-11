package pl.tazz.ideas.question.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.tazz.ideas.question.domain.model.Question;

import java.util.List;
import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository <Question, UUID> {

    List<Question> findAllByCategoryId(UUID id);

    @Query("SELECT q FROM Question q ORDER BY SIZE(q.answers) DESC")
    Page<Question> findHot(Pageable pageable);


    @Query("SELECT q FROM Question q where SIZE(q.answers) = 0")
    Page<Question> findUnanswered(Pageable pageable);
}

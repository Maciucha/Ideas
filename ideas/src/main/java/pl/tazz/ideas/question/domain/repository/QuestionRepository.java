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
public interface QuestionRepository extends JpaRepository<Question, UUID> {

    List<Question> findAllByCategoryId(UUID id);

    @Query("SELECT q FROM Question q ORDER BY SIZE(q.answers) DESC")
    Page<Question> findHot(Pageable pageable);

    @Query("SELECT q FROM Question q WHERE SIZE(q.answers) = 0")
    Page<Question> findUnanswered(Pageable pageable);

    List<Question> findTop10ByOrderByCreatedDateDesc();

    List<Question> findTop4ByOrderByCreatedDateDesc();

    List<Question> findTop3ByOrderByCreatedDateDesc();

    Page<Question> findByNameContainingIgnoreCase(String name, Pageable pageable);


    // Wyszukiwanie z zapytaniem w SQL
    @Query(
            value = "SELECT * FROM questions q WHERE UPPER(q.name) LIKE UPPER(CONCAT('%', :query, '%'))",
            countQuery = "SELECT COUNT(*) FROM questions q WHERE UPPER(q.name) LIKE UPPER(CONCAT('%', :query, '%'))",
            nativeQuery = true
    )
    Page<Question> findByQuery(String query, Pageable pageable);

    @Query("SELECT q FROM Question q ORDER BY SIZE(q.answers) DESC")
    List<Question> findListAllHot();

    @Query("SELECT q FROM Question q WHERE SIZE(q.answers) = 0")
    List<Question> findListAllUnanswered();

}

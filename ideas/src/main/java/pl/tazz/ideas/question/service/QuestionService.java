package pl.tazz.ideas.question.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.tazz.ideas.question.domain.model.Question;
import pl.tazz.ideas.question.domain.repository.QuestionRepository;

import java.util.List;
import java.util.UUID;

@Service
public class QuestionService {


    private QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Transactional(readOnly = true)
    public Page<Question> getQuestions(Pageable pageable) {
        return questionRepository.findAll(pageable);
    }


    @Transactional(readOnly = true)
    public Question getQuestion(UUID id) {
        return questionRepository.getById(id);
    }

    @Transactional
    public Question createQuestion(Question questionRequest) {
        Question question = new Question();

        question.setName(questionRequest.getName());
        return questionRepository.save(question);
    }

    @Transactional
    public Question updateQuestion(UUID id, Question questionRequest) {

        Question question = questionRepository.getReferenceById(id);

        question.setName(questionRequest.getName());
        return questionRepository.save(question);
    }

    @Transactional
    public void deleteQuestion(UUID id) {
        questionRepository.deleteById(id);
    }


    @Transactional(readOnly = true)
    public List<Question> findAllByCategoryId(UUID id) {
        return questionRepository.findAllByCategoryId(id);

    }

    @Transactional(readOnly = true)
    public Page<Question> findHot(Pageable pageable) {
        return questionRepository.findHot(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Question> findUnanswered(Pageable pageable) {
        return questionRepository.findUnanswered(pageable);
    }
}
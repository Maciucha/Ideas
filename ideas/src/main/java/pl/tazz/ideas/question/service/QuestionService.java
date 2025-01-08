package pl.tazz.ideas.question.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.tazz.ideas.common.dto.QuestionMapper;
import pl.tazz.ideas.question.domain.model.Question;
import pl.tazz.ideas.question.domain.repository.QuestionRepository;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestionService {


    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;
    private final Random random = new Random();


    @Transactional(readOnly = true)
    public Page<Question> getQuestions(Pageable pageable) {
        return questionRepository.findAll(pageable);
    }


    @Transactional(readOnly = true)
    public Question getQuestion(UUID id) {
        return questionRepository.findById(id).orElseThrow(() -> new RuntimeException("Question not found"));
    }


    @Transactional
    public Question createQuestion(Question questionRequest) {
        Question question = new Question();
        questionMapper.copyProperties(questionRequest, question);
        return questionRepository.save(question);
    }

    @Transactional
    public Question updateQuestion(UUID id, Question questionRequest) {
        Question question = questionRepository.getReferenceById(id);
        questionMapper.copyProperties(questionRequest, question);
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


    @Transactional(readOnly = true)
    public Page<Question> findByQuery(String query, Pageable pageable) {
        return questionRepository.findByQuery(query, pageable);
    }

    @Transactional(readOnly = true)
    public List<Question> getLatestQuestions() {
        return questionRepository.findTop10ByOrderByCreatedDateDesc();
    }

    @Transactional(readOnly = true)
    public Question getRandomHotQuestion() {
        List<Question> hotQuestions = questionRepository.findListAllHot();
        return getRandomQuestionFromList(hotQuestions);
    }

    @Transactional(readOnly = true)
    public Question getRandomUnansweredQuestion() {
        List<Question> unansweredQuestions = questionRepository.findListAllUnanswered();
        return getRandomQuestionFromList(unansweredQuestions);
    }

    @Transactional(readOnly = true)
    public Question getRandomQuestion() {
        List<Question> allQuestions = questionRepository.findAll();
        return getRandomQuestionFromList(allQuestions);
    }

    private Question getRandomQuestionFromList(List<Question> questions) {
        if (questions == null || questions.isEmpty()) {
            return null; // Brak pytań
        }
        int randomIndex = random.nextInt(questions.size());
        return questions.get(randomIndex);
    }
}
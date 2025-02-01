package pl.tazz.ideas.question.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.tazz.ideas.question.domain.model.Answer;
import pl.tazz.ideas.question.domain.model.Question;
import pl.tazz.ideas.question.domain.repository.AnswerRepository;
import pl.tazz.ideas.question.domain.repository.QuestionRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;


    @Transactional(readOnly = true)
    public Page<Answer> getAnswers(String search, Pageable pageable) {
        if (search == null) {
            return answerRepository.findAll(pageable);
        } else {
            return answerRepository.findByNameContainingIgnoreCase(search, pageable);
        }
    }

    @Transactional(readOnly = true)
    public List<Answer> getAnswers(UUID questionId) {
        return answerRepository.findByQuestionId(questionId);
    }

    @Transactional(readOnly = true)
    public Answer getAnswer(UUID id) {
        return answerRepository.getReferenceById(id);
    }

    @Transactional
    public Answer createAnswer(UUID questionId, Answer answerRequest) {
        // Pobieramm pytanie z bazy danych
        Question question = questionRepository.getReferenceById(questionId);
        // Powiązuję odpowiedź z pytaniem
        answerRequest.setQuestion(question);
        answerRepository.save(answerRequest);

        return answerRequest;
    }


    @Transactional
    public Answer updateAnswer(UUID answerId, Answer answerRequest) {
        Answer answer = answerRepository.getReferenceById(answerId);
        answer.setContent(answerRequest.getContent());


        return answerRepository.save(answer);
    }

    @Transactional
    public void deleteAnswer(UUID answerId) {
        answerRepository.deleteById(answerId);
    }

    @Transactional(readOnly = true)
    public List<Answer> getLatestAnswers() {
        return answerRepository.findTop10ByOrderByCreatedDateDesc();
    }
}
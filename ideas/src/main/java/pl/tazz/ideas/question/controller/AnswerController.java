package pl.tazz.ideas.question.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.tazz.ideas.question.domain.model.Answer;
import pl.tazz.ideas.question.domain.model.Question;
import pl.tazz.ideas.question.service.AnswerService;
import pl.tazz.ideas.question.service.QuestionService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/questions/{question-id}/answers")
public class AnswerController {
    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @GetMapping
    List<Answer> getAnswers(@PathVariable("question-id") UUID questionID) {
        return answerService.getAnswers(questionID);
    }

    @GetMapping("{answer-id}")
    Answer getAnswer(@PathVariable("question-id") UUID questionID, @PathVariable("answer-id") UUID answerId) {
        return answerService.getAnswer(answerId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Answer createAnswer(@PathVariable("question-id") UUID questionID, @RequestBody Answer answer) {
        return answerService.createAswer(questionID, answer);
    }


    @PutMapping("{answer-id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    Answer updateAnswer(@PathVariable("question-id") UUID questionID, @PathVariable("answer-id") UUID answerId,
                        @RequestBody Answer answer) {
        return answerService.updateAnswer(answerId, answer);
    }


    @DeleteMapping("{answer-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteAnswer(@PathVariable("answer-id") UUID answerId) {
        answerService.deleteAnswer(answerId);
    }
}

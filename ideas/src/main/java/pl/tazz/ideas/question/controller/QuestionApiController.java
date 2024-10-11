package pl.tazz.ideas.question.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.tazz.ideas.question.service.QuestionService;
import pl.tazz.ideas.question.domain.model.Question;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/questions")
public class QuestionApiController {
    private final QuestionService questionsService;

    public QuestionApiController(QuestionService questionsService) {
        this.questionsService = questionsService;
    }

    @GetMapping
    Page<Question> getQuestions(Pageable pageable) {
        return questionsService.getQuestions(pageable);
    }


    @GetMapping("{id}")
    Question getQuestion(@PathVariable UUID id) {
        return questionsService.getQuestion(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Question createQuestion(@RequestBody Question question) {
        return questionsService.createQuestion(question);
    }


    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    Question updateQuestion(@PathVariable UUID id, @RequestBody Question question) {
        return questionsService.updateQuestion(id, question);
    }


    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteQuestion(@PathVariable UUID id) {
        questionsService.deleteQuestion(id);
    }
}

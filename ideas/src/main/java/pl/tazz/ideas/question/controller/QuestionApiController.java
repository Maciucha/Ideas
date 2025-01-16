package pl.tazz.ideas.question.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.tazz.ideas.question.service.QuestionService;
import pl.tazz.ideas.question.domain.model.Question;

import java.util.UUID;

@RestController

@RequestMapping("api/v1/questions")
@RequiredArgsConstructor
public class QuestionApiController {
    private final QuestionService questionsService;

    @GetMapping
    Page<Question> getQuestions(
            @RequestParam(name = "s", required = false) String search,
            Pageable pageable) {
        return questionsService.getQuestions(search, pageable);
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

    @GetMapping("/hot")
    public Page<Question> getHotQuestions(Pageable pageable) {
        return questionsService.findHot(pageable);
    }

    @GetMapping("/unanswered")
    public Page<Question> getUnansweredQuestions(Pageable pageable) {
        return questionsService.findUnanswered(pageable);
    }

    @GetMapping("/hot/random")
    public Question getRandomHotQuestion() {
        return questionsService.getRandomHotQuestion();
    }

    @GetMapping("/unanswered/random")
    public Question getRandomUnansweredQuestion() {
        return questionsService.getRandomUnansweredQuestion();
    }

    @GetMapping("/all/random")
    public Question getRandomQuestion() {
        return questionsService.getRandomQuestion();
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

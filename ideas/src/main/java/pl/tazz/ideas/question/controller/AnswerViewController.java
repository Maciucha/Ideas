package pl.tazz.ideas.question.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.tazz.ideas.IdeasConfiguration;
import pl.tazz.ideas.common.controller.ContorllerUtils;
import pl.tazz.ideas.common.controller.IdeasCommonViewController;
import pl.tazz.ideas.question.domain.model.Answer;
import pl.tazz.ideas.question.domain.model.Question;
import pl.tazz.ideas.question.service.AnswerService;
import pl.tazz.ideas.question.service.QuestionService;

import java.util.UUID;

@Controller
@RequestMapping("/answers")
@RequiredArgsConstructor
public class AnswerViewController extends IdeasCommonViewController {

    private final QuestionService questionService;
    private final AnswerService answersService;
    private final IdeasConfiguration ideasConfiguration;

    @GetMapping({"/", ""})
    public String indexView(@RequestParam(name = "page", defaultValue = "1") Integer page, Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, ideasConfiguration.getPagingPageSize());
        Page<Question> questionsPage = questionService.getQuestions(pageRequest);

        model.addAttribute("questionsPage", questionsPage);
        ContorllerUtils.paging(model, questionsPage);
        addGlobalAttributes(model);

        return "answer/index";
    }


    @GetMapping("{id}")
    public String singleView(@PathVariable UUID id, Model model) {

        model.addAttribute("question", questionService.getQuestion(id));
        model.addAttribute("answers", answersService.getAnswers(id));
        addGlobalAttributes(model);

        return "answer/single";
    }

    @GetMapping("/add")
    public String addView(@RequestParam(name = "questionId", required = false) UUID id, Model model) {

        System.out.println("Received questionId: " + id);
        if (id == null) {
            throw new IllegalArgumentException("Parametr 'questionId' jest wymagany.");
        }
        Question question = questionService.getQuestion(id);
        model.addAttribute("question", question);
        addGlobalAttributes(model);
        model.addAttribute("answer", new Answer());

        return "answer/add";
    }

    @PostMapping("/add")
    public String addAnswer(@RequestParam("questionId") UUID questionId,
                            @ModelAttribute Answer answer) {

        if (questionId == null) {
            throw new IllegalArgumentException("Brakuje ID pytania.");
        }

        Question question = questionService.getQuestion(questionId);
        if (question == null) {
            throw new IllegalArgumentException("Pytanie o ID " + questionId + " nie istnieje.");
        }

        // Przypisuję pytanie do odpowiedzi
        answer.setQuestion(question);
        answersService.createAnswer(questionId, answer);

        return "redirect:/questions/" + questionId;
    }


}

package pl.tazz.ideas.admin.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.tazz.ideas.common.controller.ContorllerUtils;
import pl.tazz.ideas.question.domain.model.Answer;
import pl.tazz.ideas.question.domain.model.Question;
import pl.tazz.ideas.question.service.AnswerService;
import pl.tazz.ideas.question.service.QuestionService;


@Controller
@RequestMapping("/admin")
public class AdminViewController {

    private final QuestionService questionService;
    private final AnswerService answerService;


    public AdminViewController(QuestionService questionService, AnswerService answerService) {
        this.questionService = questionService;
        this.answerService = answerService;
    }

    @GetMapping
    public String indexView(
            @RequestParam(name = "s", required = false) String search,
            @RequestParam(name = "field", required = false, defaultValue = "id") String field,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "qPage", defaultValue = "0") int qPage,
            @RequestParam(name = "qSize", defaultValue = "10") int qSize,
            @RequestParam(name = "aPage", defaultValue = "0") int aPage,
            @RequestParam(name = "aSize", defaultValue = "10") int aSize,
            Model model
    ) {
        // Paginacja dla pytań
        Pageable questionsPageable = PageRequest.of(qPage, qSize, Sort.Direction.fromString(direction), field);
        Page<Question> questionsPage = questionService.getQuestions(search, questionsPageable);

        // Paginacja dla odpowiedzi
        Pageable answersPageable = PageRequest.of(aPage, aSize, Sort.Direction.fromString(direction), field);
        Page<Answer> answersPage = answerService.getAnswers(search, answersPageable);

        model.addAttribute("questions", questionsPage.getContent());
        model.addAttribute("answers", answersPage.getContent());
        model.addAttribute("questionsPage", questionsPage);
        model.addAttribute("answersPage", answersPage);
        model.addAttribute("latestQuestions", questionService.getLatestQuestions());
        model.addAttribute("latestAnswers", answerService.getLatestAnswers());


        ContorllerUtils.paging(model, questionsPage, "qPageNumbers");
        ContorllerUtils.paging(model, answersPage, "aPageNumbers");

        return "admin/index";
    }


    @GetMapping("/contact")
    public String contactPage() {
        return "admin/contact";
    }
}

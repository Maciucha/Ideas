package pl.tazz.ideas.admin.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Question> questionsPage = questionService.getQuestions(pageable);
        Page<Answer> answersPage = answerService.getPageableAnswers(pageable);
        model.addAttribute("answers", answersPage.getContent());
        model.addAttribute("questions", questionsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", questionsPage.getTotalPages());
        model.addAttribute("latestQuestions", questionService.getLatestQuestions());
        model.addAttribute("latestAnswers", answerService.getLatestAnswers());
        return "admin/index";
    }

    @GetMapping("/contact")
    public String contactPage() {
        return "admin/contact";
    }
}

package pl.tazz.ideas.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public String indexView(Model model) {
        model.addAttribute("latestQuestions", questionService.getLatestQuestions());
        model.addAttribute("latestAnswer", answerService.getLatestAnswers());
        return "admin/index";
    }

    @GetMapping("/contact")
    public String contactPage() {
        return "admin/contact";
    }
}

package pl.tazz.ideas.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.tazz.ideas.common.controller.IdeasCommonViewController;
import pl.tazz.ideas.question.service.QuestionService;

import java.util.UUID;

@Controller
@RequestMapping("/")
public class HomeViewController extends IdeasCommonViewController {

    private final QuestionService questionService;

    public HomeViewController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("")
    public String homeView(Model model) {
        model.addAttribute("latestQuestions", questionService.getLatestQuestions());
        addGlobalAttributes(model);
        return "home/index";
    }
    @GetMapping("contact")
    public String contactView(Model model) {
        addGlobalAttributes(model);
        return "home/contact";
    }
}

package pl.tazz.ideas.question.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.tazz.ideas.IdeasConfiguration;
import pl.tazz.ideas.category.domain.model.Category;
import pl.tazz.ideas.common.controller.ContorllerUtils;
import pl.tazz.ideas.common.controller.IdeasCommonViewController;
import pl.tazz.ideas.question.domain.model.Answer;
import pl.tazz.ideas.question.domain.model.Question;
import pl.tazz.ideas.question.service.AnswerService;
import pl.tazz.ideas.question.service.QuestionService;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionViewController extends IdeasCommonViewController {

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

        return "question/index";
    }


    @GetMapping("{id}")
    public String singleView(@PathVariable UUID id, Model model) {
        List<Answer> answers = answersService.getAnswers(id);
        model.addAttribute("question", questionService.getQuestion(id));
        model.addAttribute("answers", answers);
        addGlobalAttributes(model);

        return "question/single";
    }


    @GetMapping("hot")
    public String hotView(@RequestParam(name = "page", defaultValue = "1") Integer page, Model model) {

        PageRequest pageRequest = PageRequest.of(page - 1, ideasConfiguration.getPagingPageSize());
        Page<Question> questionsPage = questionService.findHot(pageRequest);

        model.addAttribute("questionsPage", questionsPage);
        ContorllerUtils.paging(model, questionsPage);
        addGlobalAttributes(model);

        return "question/index";
    }

    @GetMapping("unanswered")
    public String unansweredView(@RequestParam(name = "page", defaultValue = "1") Integer page, Model model) {

        PageRequest pageRequest = PageRequest.of(page - 1, ideasConfiguration.getPagingPageSize());
        Page<Question> questionsPage = questionService.findUnanswered(pageRequest);

        model.addAttribute("questionsPage", questionsPage);
        ContorllerUtils.paging(model, questionsPage);
        addGlobalAttributes(model);

        return "question/index";
    }

    @GetMapping("add")
    public String addView(@RequestParam(name = "categoryId", required = false) UUID categoryId, Model model) {
        Question question = new Question();
        if (categoryId != null) {
            Category category = categoryService.getCategory(categoryId);
            question.setCategory(category);
        }
        model.addAttribute("question", question);

        addGlobalAttributes(model);

        return "question/add";
    }


    @PostMapping("/add")
    public String addQuestion(@ModelAttribute Question question) {
        questionService.createQuestion(question);
        return "redirect:/questions";
    }
}

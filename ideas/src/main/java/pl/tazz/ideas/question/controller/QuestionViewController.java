package pl.tazz.ideas.question.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
import pl.tazz.ideas.user.domain.model.User;
import pl.tazz.ideas.user.domain.repository.UserRepository;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionViewController extends IdeasCommonViewController {

    private final QuestionService questionService;
    private final AnswerService answersService;
    private final IdeasConfiguration ideasConfiguration;
    private final UserRepository userRepository;

    @GetMapping({"/", ""})
    public String indexView(

    @RequestParam(name = "page", defaultValue = "1") Integer page, Model model,
    @RequestParam(name = "s", required = false) String search)

    {
        PageRequest pageRequest = PageRequest.of(page - 1, ideasConfiguration.getPagingPageSize());
        Page<Question> questionsPage = questionService.getQuestions(search, pageRequest);

        model.addAttribute("latest3Questions", questionService.getLatest3Questions());
        model.addAttribute("questionsPage", questionsPage);
        ContorllerUtils.paging(model, questionsPage, "questionsPageNumbers");
        addGlobalAttributes(model);

        return "question/index";
    }


    @GetMapping("{id}")
    public String singleView(@PathVariable UUID id, Model model) {
        List<Answer> answers = answersService.getAnswers(id);

        model.addAttribute("latest3Questions", questionService.getLatest3Questions());
        model.addAttribute("question", questionService.getQuestion(id));
        model.addAttribute("answers", answers);
        addGlobalAttributes(model);

        return "user/question/single";
    }


    @GetMapping("hot")
    public String hotView(@RequestParam(name = "page", defaultValue = "1") Integer page, Model model) {

        PageRequest pageRequest = PageRequest.of(page - 1, ideasConfiguration.getPagingPageSize());
        Page<Question> questionsPage = questionService.findHot(pageRequest);

        model.addAttribute("latest3Questions", questionService.getLatest3Questions());
        model.addAttribute("questionsPage", questionsPage);
        ContorllerUtils.paging(model, questionsPage,"questionsPageNumbers");
        addGlobalAttributes(model);

        return "question/index";
    }

    @GetMapping("unanswered")
    public String unansweredView(@RequestParam(name = "page", defaultValue = "1") Integer page, Model model) {

        PageRequest pageRequest = PageRequest.of(page - 1, ideasConfiguration.getPagingPageSize());
        Page<Question> questionsPage = questionService.findUnanswered(pageRequest);

        model.addAttribute("latest3Questions", questionService.getLatest3Questions());
        model.addAttribute("questionsPage", questionsPage);
        ContorllerUtils.paging(model, questionsPage, "questionsPageNumbers");
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            // Rzucam wyjątek lub przekierowuję na stronę logowania
            throw new IllegalStateException("User is not authenticated");
        }

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String email = user.getEmail();

        model.addAttribute("question", question);
        model.addAttribute("username", username);
        model.addAttribute("email", email);

        addGlobalAttributes(model);

        return "user/question/add";
    }



    @PostMapping("/add")
    public String addQuestion(@ModelAttribute Question question) {
        questionService.createQuestion(question);
        return "redirect:/questions";
    }
}

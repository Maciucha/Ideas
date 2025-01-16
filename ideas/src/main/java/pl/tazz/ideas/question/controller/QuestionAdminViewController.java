package pl.tazz.ideas.question.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.tazz.ideas.common.controller.ContorllerUtils;
import pl.tazz.ideas.common.dto.Message;
import pl.tazz.ideas.question.domain.model.Question;
import pl.tazz.ideas.question.service.QuestionService;

import java.util.UUID;

@Controller
@RequestMapping("/admin/question")
@RequiredArgsConstructor
public class QuestionAdminViewController {

    private final QuestionService questionService;


    @GetMapping
    public String indexQuestionAdminView(
            @RequestParam(name = "s", required = false) String search,
            @RequestParam(name = "field", required = false, defaultValue = "id") String field,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), field);

        String reverseSort = null;
        if (direction.equals("asc")) {
            reverseSort = "desc";
        } else {
            reverseSort = "asc";
        }

        Page<Question> questionsPagePage = questionService.getQuestions(search, pageable);
        model.addAttribute("questionsPage", questionsPagePage);
        model.addAttribute("search", search);
        model.addAttribute("reverseSort", reverseSort);
        ContorllerUtils.paging(model, questionsPagePage);

        return "/admin/question/index";
    }

    @GetMapping("{id}")
    public String questionEditView(Model model, @PathVariable UUID id) {
        model.addAttribute("question", questionService.getQuestion(id));

        return "admin/question/edit";
    }


    @PostMapping("{id}")
    public String questionEdit(
            @PathVariable UUID id,
            @Valid @ModelAttribute("question") Question question,
            BindingResult bindingResult,
            RedirectAttributes ra,
            Model model

    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            model.addAttribute("message", Message.error("Błąd zapisu"));
            return "admin/question/edit";
        }

        try {
            questionService.updateQuestion(id, question);
            ra.addFlashAttribute("message", Message.info("Pytanie zapisane."));

        } catch (Exception e) {
            model.addAttribute("question", question);
            model.addAttribute("message", Message.error("Nieznany błąd zapisu"));
            return "admin/question/edit";
        }

        return "redirect:/admin/question";
    }


    @GetMapping("{id}/delete")
    public String deleteView(@PathVariable UUID id, RedirectAttributes ra) {
        questionService.deleteQuestion(id);
        ra.addFlashAttribute("message", Message.info("Pytanie usunięte."));

        return "redirect:/admin/question";
    }
}

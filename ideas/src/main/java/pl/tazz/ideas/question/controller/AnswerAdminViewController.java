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
import pl.tazz.ideas.question.domain.model.Answer;
import pl.tazz.ideas.question.service.AnswerService;

import java.util.UUID;

@Controller
@RequestMapping("/admin/answer")
@RequiredArgsConstructor
public class AnswerAdminViewController {

    private final AnswerService answerService;


    @GetMapping
    public String indexAnswerAdminView(
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

        Page<Answer> answerPage = answerService.getPageableAnswers(search, pageable);
        model.addAttribute("answerPage", answerPage);
        model.addAttribute("search", search);
        model.addAttribute("reverseSort", reverseSort);
        ContorllerUtils.paging(model, answerPage);

        return "/admin/answer/index";
    }

    @GetMapping("{id}")
    public String answerEditView(Model model, @PathVariable UUID id) {
        model.addAttribute("answer", answerService.getAnswer(id));

        return "admin/answer/edit";
    }


    @PostMapping("{id}")
    public String answerEdit(
            @PathVariable UUID id,
            @Valid @ModelAttribute("answer") Answer answer,
            BindingResult bindingResult,
            RedirectAttributes ra,
            Model model

    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("answer", answer);
            model.addAttribute("message", Message.error("Błąd zapisu"));
            return "admin/answer/edit";
        }

        try {
            answerService.updateAnswer(id, answer);
            ra.addFlashAttribute("message", Message.info("Odpowiedź zapisana."));

        } catch (Exception e) {
            model.addAttribute("answer", answer);
            model.addAttribute("message", Message.error("Nieznany błąd zapisu"));
            return "admin/answer/edit";
        }

        return "redirect:/admin/answer";
    }


    @GetMapping("{id}/delete")
    public String deleteView(@PathVariable UUID id, RedirectAttributes ra) {
        answerService.deleteAnswer(id);
        ra.addFlashAttribute("message", Message.info("Odpowiedź usunięta."));

        return "redirect:/admin/answer";
    }
}

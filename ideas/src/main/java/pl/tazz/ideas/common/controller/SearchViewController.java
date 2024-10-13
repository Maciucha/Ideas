package pl.tazz.ideas.common.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.tazz.ideas.IdeasConfiguration;
import pl.tazz.ideas.question.domain.model.Question;
import pl.tazz.ideas.question.service.QuestionService;

@Controller
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchViewController extends IdeasCommonViewController {

    private final QuestionService questionsService;
    private final IdeasConfiguration ideasConfiguration;

    @GetMapping
    public String searchView(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            Model model) {

        PageRequest pageRequest = PageRequest.of(page - 1, ideasConfiguration.getPagingPageSize());

        if(query != null) {
            Page<Question> questionsPage = questionsService.findByQuery(query, pageRequest);

            model.addAttribute("questionsPage", questionsPage);
            model.addAttribute("query", query);

            ContorllerUtils.paging(model, questionsPage);
        }

        addGlobalAttributes(model);

        return "search/index";
    }
}

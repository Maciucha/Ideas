package pl.tazz.ideas.common.controller;

import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class ContorllerUtils {

    public static void paging(Model model, Page<?> page, String pageNumbersAttributeName) {
        int totalPages = page.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute(pageNumbersAttributeName, pageNumbers);
        }
    }
}

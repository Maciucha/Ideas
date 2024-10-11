package pl.tazz.ideas.category.controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.tazz.ideas.category.domain.model.Category;
import pl.tazz.ideas.category.service.CategoryService;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryApiController {

    private final CategoryService categoriesService;

    public CategoryApiController(CategoryService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @GetMapping
    Page<Category> getCategories(Pageable  pageable) {
        return categoriesService.getCategories(pageable);
    }

    @GetMapping("{id}")
    Category getCategory(@PathVariable UUID id) {
        return categoriesService.getCategory(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Category createCategory(@RequestBody Category category) {
        return categoriesService.createCategory(category);
    }


    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    Category updateCategory(@PathVariable UUID id, @RequestBody Category category) {
        return categoriesService.updateCategory(id, category);
    }


    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCategory(@PathVariable UUID id) {
        categoriesService.deleteCategory(id);
    }
}

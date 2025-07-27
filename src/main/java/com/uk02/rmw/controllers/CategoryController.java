package com.uk02.rmw.controllers;

import com.uk02.rmw.models.Category;
import com.uk02.rmw.models.User;
import com.uk02.rmw.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("create")
    public ResponseEntity<Category> createCategory(@RequestBody Category category, @AuthenticationPrincipal User user) {
        Category createdCategory = categoryService.createCategory(category, user);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping("list")
    public ResponseEntity<Object> getCategories(@AuthenticationPrincipal User user) {
        return new ResponseEntity<>(categoryService.getCategoriesPerUser(user), HttpStatus.OK);
    }

}

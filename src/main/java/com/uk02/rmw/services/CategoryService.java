package com.uk02.rmw.services;

import com.uk02.rmw.models.Category;
import com.uk02.rmw.models.User;
import com.uk02.rmw.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getCategoriesPerUser(User user) {
        return categoryRepository.findByUser_IdOrUser_IdIsNull(user.getId());
    }

    public Category createCategory(Category category, User user) {
        category.setUser(user);
        return categoryRepository.save(category);
    }

}
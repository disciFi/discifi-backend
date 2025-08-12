package com.uk02.rmw.services;

import com.uk02.rmw.models.Category;
import com.uk02.rmw.models.User;
import com.uk02.rmw.repositories.CategoryRepository;
import com.uk02.rmw.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    public List<Category> getCategoriesPerUser(User user) {
        return categoryRepository.findByUser_IdOrUser_IdIsNull(user.getId());
    }

    public Category createCategory(Category category, User user) {
        category.setUser(user);
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long categoryId, Category categoryDetails, User user) {
        Category category = categoryRepository.findById(categoryId)
                .filter(cat -> cat.getUser() != null && cat.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Custom category not found or access denied"));

        category.setName(categoryDetails.getName());

        return categoryRepository.save(category);
    }

    public void deleteCategory(Long categoryId, User user) {
        Category category = categoryRepository.findById(categoryId)
                .filter(cat -> cat.getUser() != null && cat.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Custom category not found or access denied"));

        if (!transactionRepository.findByCategoryIdWithDetails(categoryId, user.getId()).isEmpty()) {
            throw new IllegalStateException("Cannot delete category with existing transactions.");
        }

        categoryRepository.delete(category);
    }
}
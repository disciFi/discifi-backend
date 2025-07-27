package com.uk02.rmw.config;

import com.uk02.rmw.models.Category;
import com.uk02.rmw.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) {
        if (categoryRepository.count() == 0) {
            seedCategories();
        }
    }

    private void seedCategories() {
        List<Category> defaultCategories = Arrays.asList(
                Category.builder().name("Salary").type("Income").build(),
                Category.builder().name("Gifts").type("Income").build(),
                Category.builder().name("Food & Dining").type("Expense").build(),
                Category.builder().name("Transport").type("Expense").build(),
                Category.builder().name("Shopping").type("Expense").build(),
                Category.builder().name("Utilities").type("Expense").build(),
                Category.builder().name("Housing").type("Expense").build(),
                Category.builder().name("Entertainment").type("Expense").build()
        );

        categoryRepository.saveAll(defaultCategories);
        System.out.println("Default categories seeded.");
    }
}
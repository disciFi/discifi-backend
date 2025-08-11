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
                Category.builder().name("Salary").build(),
                Category.builder().name("Gifts").build(),
                Category.builder().name("Food & Dining").build(),
                Category.builder().name("Transport").build(),
                Category.builder().name("Shopping").build(),
                Category.builder().name("Utilities").build(),
                Category.builder().name("Housing").build(),
                Category.builder().name("Entertainment").build(),
                Category.builder().name("Balance Adjustment").build()
        );

        categoryRepository.saveAll(defaultCategories);
        System.out.println("Default categories seeded.");
    }
}
package com.uk02.rmw.repositories;

import com.uk02.rmw.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUser_IdOrUser_IdIsNull(Long userId);
    Category findByName(String name);
}

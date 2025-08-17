package com.uk02.rmw.repositories;

import com.uk02.rmw.models.Insight;
import com.uk02.rmw.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InsightRepository extends JpaRepository<Insight, Long> {
    List<Insight> findTop10ByUserOrderByIdDesc(User user);
}

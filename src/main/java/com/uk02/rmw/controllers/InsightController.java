package com.uk02.rmw.controllers;

import com.uk02.rmw.enums.InsightPeriod;
import com.uk02.rmw.models.Insight;
import com.uk02.rmw.models.User;
import com.uk02.rmw.repositories.InsightRepository;
import com.uk02.rmw.services.InsightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class InsightController {

    private final InsightService insightService;
    private final InsightRepository insightRepository;

    @PostMapping("/generate-insight")
    public ResponseEntity<String> generateInsight(
            @AuthenticationPrincipal User user,
            @RequestParam("period") InsightPeriod period
    ) {
        String insight = insightService.getFinancialInsight(user, period);
        insightService.saveInsight(insight, user, period);
        return ResponseEntity.ok(insight);
    }

    @GetMapping("/insights")
    public ResponseEntity<List<Insight>> getInsights(@AuthenticationPrincipal User user) {
        List<Insight> insights = insightRepository.findTop10ByUserOrderByIdDesc(user);
        return ResponseEntity.ok(insights);
    }
}

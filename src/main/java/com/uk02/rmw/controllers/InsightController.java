package com.uk02.rmw.controllers;

import com.uk02.rmw.dtos.insights.InsightResponseDTO;
import com.uk02.rmw.enums.InsightPeriod;
import com.uk02.rmw.models.User;
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
    public ResponseEntity<List<InsightResponseDTO>> getInsights(@AuthenticationPrincipal User user) {
        List<InsightResponseDTO> insights = insightService.getInsights(user);
        return ResponseEntity.ok(insights);
    }
}

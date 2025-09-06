package com.uk02.rmw.controllers;

import com.uk02.rmw.dtos.insights.InsightRequestDTO;
import com.uk02.rmw.dtos.insights.InsightResponseDTO;
import com.uk02.rmw.enums.InsightPeriod;
import com.uk02.rmw.models.User;
import com.uk02.rmw.services.InsightService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class InsightController {

    private final InsightService insightService;
    private final RabbitTemplate rabbitTemplate;

    @PostMapping("/generate-insight")
    public ResponseEntity<String> generateInsight(
            @AuthenticationPrincipal User user,
            @RequestParam("period") InsightPeriod period
    ) {
        InsightRequestDTO request = new InsightRequestDTO(user.getId(), period);
        rabbitTemplate.convertAndSend("insight.generation.queue", request);

        return new ResponseEntity<>("Insight generation request received for period: " + period, HttpStatus.ACCEPTED);
    }

    @GetMapping("/insights")
    public ResponseEntity<List<InsightResponseDTO>> getInsights(@AuthenticationPrincipal User user) {
        List<InsightResponseDTO> insights = insightService.getInsights(user);
        return ResponseEntity.ok(insights);
    }
}

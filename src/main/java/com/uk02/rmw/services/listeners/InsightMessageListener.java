package com.uk02.rmw.services.listeners;

import com.uk02.rmw.dtos.insights.InsightRequestDTO;
import com.uk02.rmw.models.Insight;
import com.uk02.rmw.models.User;
import com.uk02.rmw.repositories.InsightRepository;
import com.uk02.rmw.repositories.UserRepository;
import com.uk02.rmw.services.InsightService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InsightMessageListener {

    private final InsightService insightService;
    private final UserRepository userRepository;
    private final InsightRepository insightRepository;

    @RabbitListener(queues = "insight.generation.queue")
    public void handleInsightGeneration(InsightRequestDTO request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("User not found for insight generation"));

        String insightContent = insightService.getFinancialInsight(user, request.period());

        if (insightContent != null && !insightContent.contains("Not enough recent transaction data")) {
            Insight insight = Insight.builder()
                    .content(insightContent)
                    .user(user)
                    .type(request.period())
                    .build();
            insightRepository.save(insight);
        }
    }
}
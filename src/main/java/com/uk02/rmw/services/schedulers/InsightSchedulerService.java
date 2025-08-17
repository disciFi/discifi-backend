package com.uk02.rmw.services.schedulers;

import com.uk02.rmw.Constants;
import com.uk02.rmw.enums.InsightPeriod;
import com.uk02.rmw.models.Insight;
import com.uk02.rmw.models.User;
import com.uk02.rmw.repositories.InsightRepository;
import com.uk02.rmw.repositories.UserRepository;
import com.uk02.rmw.services.InsightService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InsightSchedulerService {

    private final UserRepository userRepository;
    private final InsightService insightService;
    private final InsightRepository insightRepository;

    @Scheduled(cron = "0 0 4 * * ?")
    public void generateDailyInsightsForAllUsers() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            String insightContent = insightService.getFinancialInsight(user, InsightPeriod.TODAY);
            saveInsight(insightContent, user, InsightPeriod.TODAY);
        }
    }

    @Scheduled(cron = "0 0 4 ? * SUN")
    public void generateWeeklyInsightsForAllUsers() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            String insightContent = insightService.getFinancialInsight(user, InsightPeriod.WEEK);
            saveInsight(insightContent, user, InsightPeriod.WEEK);
        }
    }

    @Scheduled(cron = "0 0 4 L * ?")
    public void generateMonthlyInsightsForAllUsers() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            String insightContent = insightService.getFinancialInsight(user, InsightPeriod.MONTH);
            saveInsight(insightContent, user, InsightPeriod.MONTH);
        }
    }

    private void saveInsight(String content, User user, InsightPeriod type) {
        if (content != null && !content.equals(Constants.NO_TRANSACTIONS)) {
            Insight insight = Insight.builder()
                    .content(content)
                    .user(user)
                    .type(type)
                    .build();
            insightRepository.save(insight);
        }
    }

}
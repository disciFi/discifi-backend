package com.uk02.rmw.services;

import com.uk02.rmw.Constants;
import com.uk02.rmw.enums.InsightPeriod;
import com.uk02.rmw.models.Insight;
import com.uk02.rmw.models.Transaction;
import com.uk02.rmw.models.User;
import com.uk02.rmw.repositories.InsightRepository;
import com.uk02.rmw.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InsightService {

    private final OpenAiChatModel chatModel;
    private final TransactionRepository transactionRepository;
    private final InsightRepository insightRepository;

    private LocalDate getStartDateForPeriod(InsightPeriod period) {
        LocalDate today = LocalDate.now();
        return switch (period) {
            case TODAY -> today;
            case WEEK -> today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            case MONTH -> today.withDayOfMonth(1);
        };
    }

    public String getFinancialInsight(User user, InsightPeriod period) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = getStartDateForPeriod(period);

        List<Transaction> transactions = transactionRepository.findUserExpensesBetweenDates(user.getId(), startDate, endDate);

        if (transactions.isEmpty()) {
            return Constants.NO_TRANSACTIONS;
        }

        String transactionData = transactions.stream()
                .map(t -> String.format("- On %s, you spent %.2f on %s in the '%s' category.",
                        t.getDate(), t.getAmount(), t.getTitle(), t.getCategory().getName()))
                .collect(Collectors.joining("\n"));

        String promptString = """
            You are an expert financial analyst. Analyze the following transaction data for the user '{username}'
            and provide ONE short summary over all the transactions (under 50 words).
            Focus on specific patterns, high-spending areas, or notable changes.
            Example: 'Heads up! Your spending on Food & Dining is up this week with negligible spends on shopping, kudos for that.'

            Transaction Data:
            {transactions}

            Notification:
            """;

        PromptTemplate promptTemplate = new PromptTemplate(promptString);
        Prompt prompt = promptTemplate.create(Map.of(
                "username", user.getActualUsername(),
                "transactions", transactionData
        ));

        return chatModel.call(prompt).getResult().getOutput().getText();
    }

    public void saveInsight(String content, User user, InsightPeriod type) {
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

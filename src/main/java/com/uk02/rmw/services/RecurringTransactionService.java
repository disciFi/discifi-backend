package com.uk02.rmw.services;

import com.uk02.rmw.dtos.TransactionDTO;
import com.uk02.rmw.models.Transaction;
import com.uk02.rmw.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecurringTransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;

    @Scheduled(cron = "0 0 2 * * ?")
    public void processRecurringTransactions() {
        LocalDate today = LocalDate.now();
        List<Transaction> dueTransactions = transactionRepository.findByIsRecurringTrueAndNextRecurrenceDateLessThanEqual(today);

        for (Transaction transaction : dueTransactions) {
            TransactionDTO newTransactionDto = new TransactionDTO(
                    transaction.getTitle(),
                    transaction.getAmount(),
                    transaction.getType(),
                    transaction.getNextRecurrenceDate(),
                    transaction.getAccount().getId(),
                    transaction.getCategory().getId(),
                    false,
                    null
            );

            transactionService.createTransaction(newTransactionDto, transaction.getAccount().getUser());

            transaction.setNextRecurrenceDate(
                    transactionService.calculateNextDate(transaction.getNextRecurrenceDate(), transaction.getRecurrencePeriod())
            );
            transactionRepository.save(transaction);
        }
    }
}
package com.uk02.rmw.services.search;

import com.uk02.rmw.dtos.transactions.TransactionDocument;
import com.uk02.rmw.models.Transaction;
import com.uk02.rmw.repositories.search.TransactionSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ElasticsearchSyncService {

    private final TransactionSearchRepository repository;

    public void syncTransaction(Transaction transaction) {
        TransactionDocument doc = new TransactionDocument(
                transaction.getId(),
                transaction.getTitle(),
                transaction.getAmount().doubleValue(),
                transaction.getDate(),
                transaction.getCategory().getName(),
                transaction.getAccount().getUser().getId()
        );

        repository.save(doc);
    }
}

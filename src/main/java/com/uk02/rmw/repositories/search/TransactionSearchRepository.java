package com.uk02.rmw.repositories.search;

import com.uk02.rmw.dtos.transactions.TransactionDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface TransactionSearchRepository extends ElasticsearchRepository<TransactionDocument, Long> {
    List<TransactionDocument> findByUserIdAndTitleContainingIgnoreCase(Long userId, String title);
}

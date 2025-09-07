package com.uk02.rmw.dtos.transactions;


import jakarta.persistence.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;

@Document(indexName = "transactions")
public record TransactionDocument (
    @Id Long id,
    @Field(type = FieldType.Text) String title,
    @Field(type = FieldType.Double) Double amount,
    @Field(type = FieldType.Date) LocalDate date,
    @Field(type = FieldType.Keyword) String category,
    @Field(type = FieldType.Long) Long userId
) {  }

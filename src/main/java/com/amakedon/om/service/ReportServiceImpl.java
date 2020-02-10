package com.amakedon.om.service;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.elasticsearch.search.aggregations.metrics.sum.ParsedSum;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

@Service
public class ReportServiceImpl implements ReportService {

    private static final String DATE_HISTOGRAM = "amount_per_day";
    private static final String DATE_HISTOGRAM_FIELD = "createdDate";
    private static final String DATE_FORMAT = "8yyyy-MM-dd";
    private static final String SUB_AGGREGATION = "total_amount";
    private static final String SUB_AGGREGATION_FIELD = "sum";

    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    public ReportServiceImpl(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @Override
    public Map<String, BigDecimal> getAmountOfIncomeByDate(Pageable pageable) {

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder() //
                .withQuery(matchAllQuery()) //
                .withSearchType(SearchType.DEFAULT) //
                .addAggregation(AggregationBuilders.dateHistogram(DATE_HISTOGRAM)
                        .field(DATE_HISTOGRAM_FIELD)
                        .dateHistogramInterval(DateHistogramInterval.DAY)
                        .format(DATE_FORMAT)
                        .subAggregation(AggregationBuilders.sum(SUB_AGGREGATION).field(SUB_AGGREGATION_FIELD)))

                .withPageable(pageable)
                .build();

        Aggregations aggregations = elasticsearchOperations.query(searchQuery, SearchResponse::getAggregations);

        Map<String, BigDecimal> incomes = new HashMap<>();
        aggregations.forEach(zeroLevelAggregation -> {
            if (DateHistogramAggregationBuilder.NAME.equals(zeroLevelAggregation.getType())) {
                ParsedDateHistogram dateHistogram = aggregations.get(zeroLevelAggregation.getName());
                dateHistogram.getBuckets().forEach(bucketLevelZero -> {
                    String keyOfDateBucket = bucketLevelZero.getKeyAsString();
                    bucketLevelZero.getAggregations().forEach(firstLevelAggregation -> {
                        if (firstLevelAggregation.getType().equals(SumAggregationBuilder.NAME)) {
                            ParsedSum sum = bucketLevelZero.getAggregations().get(firstLevelAggregation.getName());
                            incomes.put(keyOfDateBucket, BigDecimal.valueOf(sum.getValue()));
                        }
                    });
                });
            }
        });
        return incomes;
    }

}


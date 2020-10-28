package com.amakedon.om.service;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    private static final String DATE_HISTOGRAM = "amount_per_day";
    private static final String DATE_HISTOGRAM_FIELD = "createdDate";
    private static final String DATE_FORMAT = "8yyyy-MM-dd";
    private static final String SUB_AGGREGATION = "total_amount";
    private static final String SUB_AGGREGATION_FIELD = "sum";

    private RestHighLevelClient elasticsearchClient;

    private static final Logger LOG = LoggerFactory.getLogger(ReportServiceImpl.class);


    @Autowired
    public ReportServiceImpl(RestHighLevelClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public Map<String, BigDecimal> getAmountOfIncomeByDate(Pageable pageable) {

/*        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder() //
                .withQuery(matchAllQuery()) //
                .withSearchType(SearchType.DEFAULT) //
                .addAggregation(AggregationBuilders.dateHistogram(DATE_HISTOGRAM)
                        .field(DATE_HISTOGRAM_FIELD)
                        .calendarInterval(DateHistogramInterval.DAY)
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
        });*/

        SearchResponse response = null;
        try {
            response = elasticsearchClient.search(new SearchRequest("order")
                    .source(new SearchSourceBuilder()
                            //.query(query)
                            .aggregation(
                                    AggregationBuilders.terms(DATE_HISTOGRAM).field(DATE_HISTOGRAM_FIELD)
                                            .subAggregation(AggregationBuilders.dateHistogram(SUB_AGGREGATION)
                                                    .field(SUB_AGGREGATION_FIELD)
                                                    //.fixedInterval(DateHistogramInterval.days(3652))
                                                    //.extendedBounds(new ExtendedBounds(1940L, 2009L))
                                                    .format(DATE_FORMAT)
                                                    .subAggregation(AggregationBuilders.avg("avg_children").field("children"))
                                            )
                            )
                            .from(pageable.getPageNumber()*pageable.getPageSize()) //FIXME
                            .size(pageable.getPageSize())
                            .trackTotalHits(true)
                    ), RequestOptions.DEFAULT);
            LOG.debug("elasticsearch response: {} hits", response.getHits().getTotalHits());
            LOG.trace("elasticsearch response: {} hits", response.toString());
        } catch (IOException e) {
            LOG.error("Error during ES search", e);
        }



        Map<String, BigDecimal> incomes = new HashMap<>();
        return incomes;
    }

}


package com.amakedon.om.data.repository.es;

import com.amakedon.om.data.model.Order;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.metrics.SumAggregationBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;

public class EsOrderCustomRepositoryImpl implements EsOrderCustomRepository {

    private final ElasticsearchOperations operations;

    public EsOrderCustomRepositoryImpl(ElasticsearchOperations operations) {
        this.operations = operations;
    }

    @Override
    public SearchPage<Order> findTotalSumByDate(Pageable pageable) {
        final String dateHistogram = "amount_per_day";

        SumAggregationBuilder sumAggregationBuilder = AggregationBuilders
                .sum("total_amount")
                .field("sum");

        DateHistogramAggregationBuilder datesAggregationBuilder = AggregationBuilders
                .dateHistogram(dateHistogram)
                .field("createdDate")
                .calendarInterval(DateHistogramInterval.DAY)
                .subAggregation(sumAggregationBuilder);
        Query query = new NativeSearchQueryBuilder()                       // we build a Elasticsearch native query
                .addAggregation(datesAggregationBuilder) // add the aggregation
                .withQuery(QueryBuilders.matchAllQuery())   // add the query part
                .withPageable(pageable)                                        // add the requested page
                .build();

        SearchHits<Order> searchHits = operations.search(query, Order.class);  // send it of and get the result

        return SearchHitSupport.searchPageFor(searchHits, pageable);  // convert the result to a SearchPage
    }

}

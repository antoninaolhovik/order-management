package com.amakedon.om.service;

import com.amakedon.om.data.model.Order;
import com.amakedon.om.data.repository.es.EsOrderRepository;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class ReportServiceImpl implements ReportService {

    private static final String DATE_HISTOGRAM = "amount_per_day";
    private static final String DATE_HISTOGRAM_FIELD = "createdDate";
    private static final String SUB_AGGREGATION = "total_amount";
    private static final String SUB_AGGREGATION_FIELD = "sum";

    private static final Logger LOG = LoggerFactory.getLogger(ReportServiceImpl.class);

    private EsOrderRepository esOrderRepository;

    @Autowired
    public ReportServiceImpl(EsOrderRepository esOrderRepository) {
        this.esOrderRepository = esOrderRepository;
    }

    @Override
    public Map<String, Double> getAmountOfIncomeByDate(Pageable pageable) {

        Function<Aggregations, Map<String, Double>> getTotalAmounts = getAggregationsMapFunction();

        SearchPage<Order> searchPage = esOrderRepository.findTotalSumByDate(pageable);
        Aggregations aggregations = searchPage.getSearchHits().getAggregations();
        Map<String, Double> totalAmounts = getTotalAmounts.apply(aggregations);

        return totalAmounts;

    }

    private Function<Aggregations, Map<String, Double>> getAggregationsMapFunction() {
        return aggregations -> {
            Map<String, Double> incomeByDate = new HashMap<>();

            if (aggregations != null) {
                Aggregation aggregationByDates = aggregations.get(DATE_HISTOGRAM);
                if (aggregationByDates != null) {
                    List<? extends Histogram.Bucket> buckets = ((ParsedDateHistogram) aggregationByDates).getBuckets();
                    if (buckets != null) {
                        buckets.stream().forEach(bucket -> {
                            Aggregations sumAggregations = bucket.getAggregations();
                            ParsedSum sumAggregation = sumAggregations.get(SUB_AGGREGATION);

                            incomeByDate.put(bucket.getKeyAsString(), sumAggregation.getValue());
                        });
                    }
                }
            }
            return incomeByDate;
        };
    }

}


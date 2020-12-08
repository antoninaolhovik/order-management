package com.amakedon.om.data.repository.es;

import com.amakedon.om.data.model.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchPage;

public interface EsOrderCustomRepository {

    SearchPage<Order> findTotalSumByDate(Pageable pageable);
}

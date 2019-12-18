package com.amakedon.om.data.repository.es;

import com.amakedon.om.data.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EsOrderRepository extends ElasticsearchRepository<Order, Long> {

    Page<Order> findByOrderItemsProductName(String name, Pageable pageable);

}

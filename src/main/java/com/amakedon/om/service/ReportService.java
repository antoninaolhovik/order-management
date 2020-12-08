package com.amakedon.om.service;

import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Map;

public interface ReportService {

    Map<String, Double> getAmountOfIncomeByDate (Pageable pageable);
}

package com.akif.rental.internal.service.report;

import com.akif.rental.internal.dto.report.LateReturnFilterDto;
import com.akif.rental.internal.dto.report.LateReturnReportDto;
import com.akif.rental.internal.dto.report.LateReturnStatisticsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface LateReturnReportService {

    Page<LateReturnReportDto> getLateReturns(LateReturnFilterDto filter, Pageable pageable);

    LateReturnStatisticsDto getStatistics(LocalDate startDate, LocalDate endDate);
}

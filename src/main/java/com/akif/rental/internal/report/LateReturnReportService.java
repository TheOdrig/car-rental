package com.akif.rental.internal.report;

import com.akif.rental.internal.report.dto.LateReturnFilterDto;
import com.akif.rental.internal.report.dto.LateReturnReportDto;
import com.akif.rental.internal.report.dto.LateReturnStatisticsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface LateReturnReportService {

    Page<LateReturnReportDto> getLateReturns(LateReturnFilterDto filter, Pageable pageable);

    LateReturnStatisticsDto getStatistics(LocalDate startDate, LocalDate endDate);
}

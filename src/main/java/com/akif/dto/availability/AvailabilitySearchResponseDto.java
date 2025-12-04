package com.akif.dto.availability;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AvailabilitySearchResponseDto implements Serializable {

    private List<AvailableCarDto> cars;
    private Long totalElements;
    private Integer totalPages;
    private Integer currentPage;
    private Integer pageSize;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate searchStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate searchEndDate;

    private Integer rentalDays;
}

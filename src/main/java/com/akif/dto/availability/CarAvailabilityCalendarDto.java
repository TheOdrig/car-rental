package com.akif.dto.availability;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.YearMonth;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarAvailabilityCalendarDto implements Serializable {

    private Long carId;
    private String carName;

    @JsonFormat(pattern = "yyyy-MM")
    private YearMonth month;

    private List<DayAvailabilityDto> days;
    private Boolean carBlocked;
    private String blockReason;
}

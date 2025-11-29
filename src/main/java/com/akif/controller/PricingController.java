package com.akif.controller;

import com.akif.dto.request.PricingRequestDto;
import com.akif.dto.response.PricingResponseDto;
import com.akif.service.pricing.IDynamicPricingService;
import com.akif.service.pricing.PricingResult;
import com.akif.service.pricing.PricingStrategy;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/api/pricing")
@RequiredArgsConstructor
@Tag(name = "Pricing", description = "Dynamic pricing management endpoints")
public class PricingController {

    private final IDynamicPricingService dynamicPricingService;

    @PostMapping("/calculate")
    @Operation(summary = "Calculate rental price", 
               description = "Calculates the final rental price with all applicable pricing strategies")
    public ResponseEntity<PricingResponseDto> calculatePrice(@Valid @RequestBody PricingRequestDto request) {
        log.info("Calculating price for car: {}, dates: {} to {}", 
            request.getCarId(), request.getStartDate(), request.getEndDate());

        PricingResult result = dynamicPricingService.calculatePrice(
            request.getCarId(),
            request.getStartDate(),
            request.getEndDate(),
            LocalDate.now()
        );

        PricingResponseDto response = PricingResponseDto.fromPricingResult(result);
        
        log.info("Price calculated: base={}, final={}, savings={}", 
            response.getBaseTotalPrice(), response.getFinalPrice(), response.getTotalSavings());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/preview")
    @Operation(summary = "Preview rental price", 
               description = "Gets a price preview without creating a rental")
    public ResponseEntity<PricingResponseDto> previewPrice(
            @RequestParam Long carId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        
        log.info("Previewing price for car: {}, dates: {} to {}", carId, startDate, endDate);

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        PricingResult result = dynamicPricingService.previewPrice(carId, start, end);
        PricingResponseDto response = PricingResponseDto.fromPricingResult(result);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/strategies")
    @Operation(summary = "List enabled pricing strategies", 
               description = "Returns all currently enabled pricing strategies")
    public ResponseEntity<List<Map<String, Object>>> getEnabledStrategies() {
        log.info("Fetching enabled pricing strategies");

        List<PricingStrategy> strategies = dynamicPricingService.getEnabledStrategies();
        
        List<Map<String, Object>> response = strategies.stream()
            .map(strategy -> Map.of(
                "name", (Object) strategy.getStrategyName(),
                "order", (Object) strategy.getOrder(),
                "enabled", (Object) strategy.isEnabled()
            ))
            .toList();

        log.info("Found {} enabled strategies", response.size());

        return ResponseEntity.ok(response);
    }
}

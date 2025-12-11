package com.akif.car.internal.availability.impl;

import com.akif.car.SimilarCarDto;
import com.akif.car.domain.enums.CarStatusType;
import com.akif.exception.CarNotFoundException;
import com.akif.car.domain.Car;
import com.akif.car.repository.CarRepository;
import com.akif.car.internal.availability.ICarAvailabilityService;
import com.akif.car.internal.availability.ISimilarCarService;
import com.akif.car.internal.pricing.DynamicPricingService;
import com.akif.car.internal.pricing.PricingResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SimilarCarServiceImpl implements ISimilarCarService {

    private final CarRepository carRepository;
    private final ICarAvailabilityService carAvailabilityService;
    private final DynamicPricingService dynamicPricingService;

    @Override
    public List<SimilarCarDto> findSimilarAvailableCars(Long carId, LocalDate startDate, LocalDate endDate, int limit) {
        log.debug("Finding similar available cars for car: {} from {} to {}, limit: {}", 
                carId, startDate, endDate, limit);

        Car referenceCar = carRepository.findByIdAndIsDeletedFalse(carId)
                .orElseThrow(() -> new CarNotFoundException(carId));

        BigDecimal referencePrice = referenceCar.getPrice();
        BigDecimal minPrice = referencePrice.multiply(BigDecimal.valueOf(0.8));
        BigDecimal maxPrice = referencePrice.multiply(BigDecimal.valueOf(1.2));

        log.debug("Reference car: {} {}, price: {}, range: {} - {}", 
                referenceCar.getBrand(), referenceCar.getModel(), referencePrice, minPrice, maxPrice);

        List<CarStatusType> blockingStatuses = Arrays.asList(CarStatusType.getUnavailableStatuses());
        Pageable pageable = PageRequest.of(0, limit * 3);
        
        Page<Car> similarCarsPage = carRepository.findSimilarCars(
                referenceCar.getBodyType(),
                minPrice,
                maxPrice,
                carId,
                blockingStatuses,
                pageable
        );

        List<SimilarCarDto> similarCars = new ArrayList<>();

        for (Car car : similarCarsPage.getContent()) {
            if (similarCars.size() >= limit) {
                break;
            }

            boolean available = carAvailabilityService.isCarAvailable(car.getId(), startDate, endDate);
            if (!available) {
                log.debug("Car {} is not available, skipping", car.getId());
                continue;
            }

            int similarityScore = 0;
            List<String> similarityReasons = new ArrayList<>();

            if (referenceCar.getBodyType() != null && 
                referenceCar.getBodyType().equalsIgnoreCase(car.getBodyType())) {
                similarityScore += 50;
                similarityReasons.add("Same body type");
            }

            if (referenceCar.getBrand() != null && 
                referenceCar.getBrand().equalsIgnoreCase(car.getBrand())) {
                similarityScore += 30;
                similarityReasons.add("Same brand");
            }

            if (car.getPrice() != null && 
                car.getPrice().compareTo(minPrice) >= 0 && 
                car.getPrice().compareTo(maxPrice) <= 0) {
                similarityScore += 20;
                similarityReasons.add("Similar price");
            }

            PricingResult pricingResult = dynamicPricingService.calculatePrice(
                    car.getId(),
                    startDate,
                    endDate,
                    LocalDate.now()
            );

            SimilarCarDto similarCarDto = new SimilarCarDto(
                    car.getId(),
                    car.getBrand(),
                    car.getModel(),
                    car.getProductionYear(),
                    car.getBodyType(),
                    pricingResult.effectiveDailyPrice(),
                    pricingResult.finalPrice(),
                    car.getCurrencyType(),
                    car.getImageUrl(),
                    similarityReasons,
                    similarityScore
            );

            similarCars.add(similarCarDto);
        }

        similarCars.sort((a, b) -> b.similarityScore().compareTo(a.similarityScore()));

        log.info("Found {} similar available cars for car: {}", similarCars.size(), carId);
        return similarCars;
    }
}

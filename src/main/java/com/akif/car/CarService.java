package com.akif.car;

import com.akif.car.internal.dto.CarRequest;
import com.akif.car.internal.pricing.dto.CarPriceUpdateRequest;
import com.akif.car.internal.dto.CarSearchRequest;
import com.akif.car.internal.dto.CarStatusUpdateRequest;
import com.akif.car.domain.enums.CarStatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CarService {

    CarDto getCarDtoById(Long id);

    boolean isCarAvailableForRental(Long carId);



    CarResponse getCarById(Long id);

    CarResponse getCarByLicensePlate(String licensePlate);

    CarResponse createCar(CarRequest carRequest);

    CarResponse updateCar(Long id, CarRequest carRequest);

    void deleteCar(Long id);

    void softDeleteCar(Long id);

    void restoreCar(Long id);


    CarListResponseDto searchCars(CarSearchRequest searchRequest);
    Page<CarResponse> getAllCars(Pageable pageable);

    Page<CarResponse> getCarsByStatus(String status, Pageable pageable);
    long getCarCount();

    Page<CarResponse> getCarsByBrand(String brand, Pageable pageable);

    Page<CarResponse> getCarsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    Page<CarResponse> getNewCars(Pageable pageable);

    Page<CarResponse> getFeaturedCars(Pageable pageable);

    Page<CarResponse> getCarsAvailableForTestDrive(Pageable pageable);

    Page<CarResponse> getAllActiveCars(Pageable pageable);
    long getActiveCarCount();


    CarResponse sellCar(Long id);

    CarResponse reserveCar(Long id);

    CarResponse cancelReservation(Long id);

    CarResponse markAsMaintenance(Long id);

    CarResponse markAsAvailable(Long id);

    CarResponse updateCarStatus(Long id, CarStatusUpdateRequest statusUpdateRequest);

    CarResponse updateCarPrice(Long id, CarPriceUpdateRequest priceUpdateRequest);


    Map<String, Object> getCarStatistics();

    Map<String, Long> getCarsCountByStatus();

    Map<String, Long> getCarsCountByBrand();

    Map<String, BigDecimal> getAveragePriceByBrand();

    List<CarSummaryResponse> getMostViewedCars(int limit);

    List<CarSummaryResponse> getMostLikedCars(int limit);


    boolean existsById(Long id);

    boolean existsByLicensePlate(String licensePlate);

    void incrementViewCount(Long id);

    void incrementLikeCount(Long id);

    void decrementLikeCount(Long id);


    List<String> validateCarData(CarRequest carRequest);

    boolean canCarBeSold(Long id);

    boolean canCarBeReserved(Long id);

    Page<CarResponse> searchCarsByCriteria(String searchTerm, String brand, String model,
                                           BigDecimal minPrice, BigDecimal maxPrice,
                                           CarStatusType status, Pageable pageable);
}

package com.akif.rental.internal.mapper;

import com.akif.car.internal.mapper.CarMapper;
import com.akif.rental.RentalResponse;
import com.akif.rental.internal.dto.request.RentalRequest;
import com.akif.rental.domain.model.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface RentalMapper {
    
    @Mapping(target = "carSummary.id", source = "carId")
    @Mapping(target = "carSummary.brand", source = "carBrand")
    @Mapping(target = "carSummary.model", source = "carModel")
    @Mapping(target = "carSummary.licensePlate", source = "carLicensePlate")
    @Mapping(target = "userSummary.id", source = "userId")
    @Mapping(target = "userSummary.username", source = "userEmail")
    @Mapping(target = "userSummary.email", source = "userEmail")
    RentalResponse toDto(Rental rental);
}
package com.akif.service.pricing.strategy;

import com.akif.config.PricingConfig;
import com.akif.repository.RentalRepository;
import com.akif.service.pricing.PriceModifier;
import com.akif.service.pricing.PricingContext;
import com.akif.service.pricing.PricingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(5)
@RequiredArgsConstructor
public class DemandPricingStrategy implements PricingStrategy {

    private final PricingConfig config;
    private final RentalRepository rentalRepository;

    @Override
    public PriceModifier calculate(PricingContext context) {

        long overlappingRentals = rentalRepository.countOverlappingRentals(
            context.carId(),
            context.startDate(),
            context.endDate()
        );

        int occupancyPercent = (int) Math.min(overlappingRentals * 10, 100);

        if (occupancyPercent > config.getDemand().getHigh().getThreshold()) {
            return PriceModifier.surcharge(
                getStrategyName(),
                config.getDemand().getHigh().getMultiplier(),
                String.format("High demand (%d%% occupancy)", occupancyPercent)
            );
        }

        if (occupancyPercent >= config.getDemand().getModerate().getThreshold()) {
            return PriceModifier.surcharge(
                getStrategyName(),
                config.getDemand().getModerate().getMultiplier(),
                String.format("Moderate demand (%d%% occupancy)", occupancyPercent)
            );
        }

        return PriceModifier.neutral(
            getStrategyName(),
            String.format("Normal demand (%d%% occupancy)", occupancyPercent)
        );
    }

    @Override
    public String getStrategyName() {
        return "Demand Pricing";
    }

    @Override
    public boolean isEnabled() {
        return config.getStrategy().isDemandEnabled();
    }

    @Override
    public int getOrder() {
        return 5;
    }
}

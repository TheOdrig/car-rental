package com.akif.service.pricing.strategy;

import com.akif.config.PricingConfig;
import com.akif.service.pricing.PriceModifier;
import com.akif.service.pricing.PricingContext;
import com.akif.service.pricing.PricingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Order(2)
@RequiredArgsConstructor
public class EarlyBookingStrategy implements PricingStrategy {

    private final PricingConfig config;

    @Override
    public PriceModifier calculate(PricingContext context) {
        int leadTimeDays = context.leadTimeDays();

        if (leadTimeDays >= config.getEarlyBooking().getTier1().getDays()) {
            return PriceModifier.discount(
                getStrategyName(),
                config.getEarlyBooking().getTier1().getMultiplier(),
                String.format("Early booking discount (%d+ days advance)", 
                    config.getEarlyBooking().getTier1().getDays())
            );
        }

        if (leadTimeDays >= config.getEarlyBooking().getTier2().getDays()) {
            return PriceModifier.discount(
                getStrategyName(),
                config.getEarlyBooking().getTier2().getMultiplier(),
                String.format("Early booking discount (%d-%d days advance)",
                    config.getEarlyBooking().getTier2().getDays(),
                    config.getEarlyBooking().getTier1().getDays() - 1)
            );
        }

        if (leadTimeDays >= config.getEarlyBooking().getTier3().getDays()) {
            return PriceModifier.discount(
                getStrategyName(),
                config.getEarlyBooking().getTier3().getMultiplier(),
                String.format("Early booking discount (%d-%d days advance)",
                    config.getEarlyBooking().getTier3().getDays(),
                    config.getEarlyBooking().getTier2().getDays() - 1)
            );
        }

        return PriceModifier.neutral(getStrategyName(), "No early booking discount");
    }

    @Override
    public String getStrategyName() {
        return "Early Booking";
    }

    @Override
    public boolean isEnabled() {
        return config.getStrategy().isEarlyBookingEnabled();
    }

    @Override
    public int getOrder() {
        return 2;
    }
}

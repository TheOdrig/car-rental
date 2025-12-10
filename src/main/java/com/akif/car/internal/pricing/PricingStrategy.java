package com.akif.car.internal.pricing;

public interface PricingStrategy {

    PriceModifier calculate(PricingContext context);

    String getStrategyName();

    boolean isEnabled();

    int getOrder();
}

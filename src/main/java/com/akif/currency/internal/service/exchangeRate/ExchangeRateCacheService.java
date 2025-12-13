package com.akif.currency.internal.service.exchangeRate;

import com.akif.currency.internal.dto.exchangeRate.ExchangeRateResponse;
import com.akif.shared.enums.CurrencyType;

public interface ExchangeRateCacheService {

    ExchangeRateResponse getCachedRates(CurrencyType baseCurrency);

    void evictCache();
}

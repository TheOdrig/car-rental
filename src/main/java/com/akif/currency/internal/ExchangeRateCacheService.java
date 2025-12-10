package com.akif.currency.internal;

import com.akif.currency.internal.dto.ExchangeRateResponse;
import com.akif.shared.enums.CurrencyType;

public interface ExchangeRateCacheService {

    ExchangeRateResponse getCachedRates(CurrencyType baseCurrency);

    void evictCache();
}

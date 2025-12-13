package com.akif.currency.internal.service.exchangeRate;

import com.akif.currency.internal.dto.exchangeRate.ExchangeRateResponse;
import com.akif.shared.enums.CurrencyType;

public interface ExchangeRateClient {

    ExchangeRateResponse fetchRates(CurrencyType baseCurrency);
}

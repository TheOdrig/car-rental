package com.akif.service.currency.impl;

import com.akif.config.CacheConfig;
import com.akif.dto.currency.ExchangeRateResponse;
import com.akif.enums.CurrencyType;
import com.akif.enums.RateSource;
import com.akif.exception.ExchangeRateApiException;
import com.akif.service.currency.IExchangeRateCacheService;
import com.akif.service.currency.IExchangeRateClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateCacheServiceImpl implements IExchangeRateCacheService {

    private static final CurrencyType BASE_CURRENCY = CurrencyType.USD;

    private final IExchangeRateClient exchangeRateClient;

    private static final Map<CurrencyType, BigDecimal> FALLBACK_RATES;

    static {
        FALLBACK_RATES = new EnumMap<>(CurrencyType.class);
        FALLBACK_RATES.put(CurrencyType.USD, BigDecimal.ONE);
        FALLBACK_RATES.put(CurrencyType.TRY, new BigDecimal("42.49"));
        FALLBACK_RATES.put(CurrencyType.EUR, new BigDecimal("0.87"));
        FALLBACK_RATES.put(CurrencyType.GBP, new BigDecimal("0.76"));
        FALLBACK_RATES.put(CurrencyType.JPY, new BigDecimal("156.00"));
    }

    @Override
    @Cacheable(value = CacheConfig.EXCHANGE_RATES_CACHE, key = "#baseCurrency")
    public ExchangeRateResponse getCachedRates(CurrencyType baseCurrency) {
        log.debug("Cache miss - fetching rates for base: {}", baseCurrency);
        return fetchRatesFromApi(baseCurrency);
    }

    @Override
    @CacheEvict(value = CacheConfig.EXCHANGE_RATES_CACHE, allEntries = true)
    public void evictCache() {
        log.info("Exchange rates cache evicted");
    }

    private ExchangeRateResponse fetchRatesFromApi(CurrencyType baseCurrency) {
        try {
            ExchangeRateResponse response = exchangeRateClient.fetchRates(baseCurrency);
            log.info("Fetched {} rates from API for base: {}", response.rates().size(), baseCurrency);
            return response;
        } catch (ExchangeRateApiException e) {
            log.warn("API call failed, using fallback rates: {}", e.getMessage());
            return createFallbackResponse(baseCurrency);
        }
    }

    private ExchangeRateResponse createFallbackResponse(CurrencyType baseCurrency) {
        log.warn("Using fallback rates for base: {}", baseCurrency);

        Map<CurrencyType, BigDecimal> rates = new EnumMap<>(CurrencyType.class);

        if (baseCurrency == BASE_CURRENCY) {
            rates.putAll(FALLBACK_RATES);
        } else {
            BigDecimal baseToUsd = FALLBACK_RATES.get(baseCurrency);
            if (baseToUsd != null && baseToUsd.compareTo(BigDecimal.ZERO) > 0) {
                for (Map.Entry<CurrencyType, BigDecimal> entry : FALLBACK_RATES.entrySet()) {
                    BigDecimal rate = entry.getValue().divide(baseToUsd, 6, RoundingMode.HALF_UP);
                    rates.put(entry.getKey(), rate);
                }
            }
        }

        return new ExchangeRateResponse(baseCurrency, LocalDateTime.now(), rates, RateSource.FALLBACK);
    }
}

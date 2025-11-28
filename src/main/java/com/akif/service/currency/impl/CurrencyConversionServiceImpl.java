package com.akif.service.currency.impl;

import com.akif.dto.currency.ConversionResult;
import com.akif.dto.currency.ExchangeRate;
import com.akif.dto.currency.ExchangeRateResponse;
import com.akif.dto.currency.ExchangeRatesResponse;
import com.akif.enums.CurrencyType;
import com.akif.enums.RateSource;
import com.akif.exception.ExchangeRateApiException;
import com.akif.service.currency.ICurrencyConversionService;
import com.akif.service.currency.IExchangeRateClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class CurrencyConversionServiceImpl implements ICurrencyConversionService {

    private static final CurrencyType BASE_CURRENCY = CurrencyType.USD;
    
    private final IExchangeRateClient exchangeRateClient;

    private final Map<CurrencyType, ExchangeRateResponse> ratesCache = new ConcurrentHashMap<>();

    private static final Map<CurrencyType, BigDecimal> FALLBACK_RATES;
    
    static {
        FALLBACK_RATES = new EnumMap<>(CurrencyType.class);
        FALLBACK_RATES.put(CurrencyType.USD, BigDecimal.ONE);
        FALLBACK_RATES.put(CurrencyType.TRY, new BigDecimal("42.49"));
        FALLBACK_RATES.put(CurrencyType.EUR, new BigDecimal("0.87"));
        FALLBACK_RATES.put(CurrencyType.GBP, new BigDecimal("0.76"));
        FALLBACK_RATES.put(CurrencyType.JPY, new BigDecimal("156.00"));
    }

    public CurrencyConversionServiceImpl(IExchangeRateClient exchangeRateClient) {
        this.exchangeRateClient = exchangeRateClient;
    }

    @Override
    public ConversionResult convert(BigDecimal amount, CurrencyType from, CurrencyType to) {
        log.debug("Converting {} {} to {}", amount, from, to);

        if (from.equals(to)) {
            return new ConversionResult(
                    amount,
                    from,
                    amount,
                    to,
                    BigDecimal.ONE,
                    LocalDateTime.now(),
                    RateSource.LIVE
            );
        }

        ExchangeRate rate = getRate(from, to);
        BigDecimal convertedAmount = calculateConversion(amount, rate.rate(), to);

        log.debug("Converted {} {} to {} {} (rate: {})", 
                amount, from, convertedAmount, to, rate.rate());

        return new ConversionResult(
                amount,
                from,
                convertedAmount,
                to,
                rate.rate(),
                rate.timestamp(),
                rate.source()
        );
    }

    @Override
    public ExchangeRate getRate(CurrencyType from, CurrencyType to) {
        log.debug("Getting exchange rate from {} to {}", from, to);

        if (from.equals(to)) {
            return new ExchangeRate(from, to, BigDecimal.ONE, LocalDateTime.now(), RateSource.LIVE);
        }

        ExchangeRateResponse ratesResponse = fetchOrGetCachedRates(from);
        BigDecimal rate = ratesResponse.getRate(to);

        if (rate == null) {
            log.warn("Rate not found for {} -> {}, using cross-rate calculation", from, to);
            rate = calculateCrossRate(from, to);
            return new ExchangeRate(from, to, rate, LocalDateTime.now(), ratesResponse.source());
        }

        return new ExchangeRate(from, to, rate, ratesResponse.timestamp(), ratesResponse.source());
    }

    @Override
    public ExchangeRatesResponse getAllRates() {
        log.debug("Getting all exchange rates");
        
        ExchangeRateResponse response = fetchOrGetCachedRates(BASE_CURRENCY);
        
        return new ExchangeRatesResponse(
                response.baseCurrency(),
                response.rates(),
                response.timestamp(),
                response.source()
        );
    }

    @Override
    public void refreshRates() {
        log.info("Refreshing exchange rates");
        ratesCache.clear();
        
        try {
            ExchangeRateResponse response = exchangeRateClient.fetchRates(BASE_CURRENCY);
            ratesCache.put(BASE_CURRENCY, response);
            log.info("Exchange rates refreshed successfully");
        } catch (ExchangeRateApiException e) {
            log.warn("Failed to refresh rates: {}", e.getMessage());
        }
    }

    private ExchangeRateResponse fetchOrGetCachedRates(CurrencyType baseCurrency) {
        ExchangeRateResponse cached = ratesCache.get(baseCurrency);
        
        if (cached != null) {
            log.debug("Using cached rates for base: {}", baseCurrency);
            return new ExchangeRateResponse(
                    cached.baseCurrency(),
                    cached.timestamp(),
                    cached.rates(),
                    RateSource.CACHED
            );
        }

        try {
            ExchangeRateResponse response = exchangeRateClient.fetchRates(baseCurrency);
            ratesCache.put(baseCurrency, response);
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

    private BigDecimal calculateConversion(BigDecimal amount, BigDecimal rate, CurrencyType targetCurrency) {
        BigDecimal converted = amount.multiply(rate);
        return converted.setScale(targetCurrency.getDecimalPlaces(), RoundingMode.HALF_UP);
    }

    private BigDecimal calculateCrossRate(CurrencyType from, CurrencyType to) {

        ExchangeRateResponse usdRates = fetchOrGetCachedRates(BASE_CURRENCY);
        
        BigDecimal fromToUsd = usdRates.getRate(from);
        BigDecimal toToUsd = usdRates.getRate(to);
        
        if (fromToUsd == null || toToUsd == null || fromToUsd.compareTo(BigDecimal.ZERO) == 0) {
            log.error("Cannot calculate cross rate for {} -> {}", from, to);
            return BigDecimal.ONE;
        }

        return toToUsd.divide(fromToUsd, 6, RoundingMode.HALF_UP);
    }
}

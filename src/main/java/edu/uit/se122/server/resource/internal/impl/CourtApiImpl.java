package edu.uit.se122.server.resource.internal.impl;

import edu.uit.se122.server.resource.CourtApi;
import edu.uit.se122.server.resource.internal.component.CourtPrice;
import edu.uit.se122.server.resource.internal.component.PriceManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class CourtApiImpl implements CourtApi {
    private final PriceManager priceManager;

    @Override
    public BigDecimal getCurrentCourtPrice() {
        CourtPrice courtPrice = priceManager.getCurrentCourtPrice();
        return courtPrice.getUnitPrice();
    }
}

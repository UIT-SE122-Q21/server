package edu.uit.se122.server.resource.internal.component;

import java.math.BigDecimal;

public final class CourtPrice {
    private final BigDecimal unitPrice;

    public CourtPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
}

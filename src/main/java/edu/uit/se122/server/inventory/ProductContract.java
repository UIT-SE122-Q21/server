package edu.uit.se122.server.inventory;

import edu.uit.se122.server.common.enums.ProductStatus;
import edu.uit.se122.server.common.enums.SaleType;

import java.math.BigDecimal;
import java.util.List;

public interface ProductContract {
    record Res(
            Integer productId,
            String productName,
            ProductStatus status,
            Integer quantity,
            Integer minQuantity,
            Integer categoryId,
            String categoryName,
            List<DetailRes> details
    ) {}

    record DetailRes(
            Integer productDetailId,
            String barcode,
            Integer capacity,
            String unit,
            BigDecimal unitPrice,
            SaleType saleType
    ) {}

    record CreateReq(
            String productName,
            Integer categoryId,
            DetailReq detail
    ) {}

    record UpdateReq(
            String productName,
            List<DetailReq> details
    ) {}

    record UpdateQuantityReq(
            Integer quantity
    ) {}

    record DetailReq(
            Integer productDetailId,
            String barcode,
            Integer capacity,
            String unit,
            BigDecimal unitPrice,
            SaleType saleType
    ) {}

    record CreatedEvent(
            Integer productId,
            Integer productCategoryId,
            String barcode,
            Integer capacity,
            String name,
            String unit,
            BigDecimal unitPrice
    ) {}
}

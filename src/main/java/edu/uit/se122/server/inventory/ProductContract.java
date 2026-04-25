package edu.uit.se122.server.inventory;

import edu.uit.se122.server.common.enums.ProductStatus;
import edu.uit.se122.server.common.enums.SaleType;

import java.util.List;

public interface ProductContract {
    record Res(
            Integer productId,
            String productName,
            String capacity,
            Integer categoryId,
            String categoryName,
            List<DetailRes> details
    ) {}

    record DetailRes(
            Integer productDetailId,
            String barcode,
            String unit,
            Double unitPrice,
            SaleType saleType,
            Integer quantity,
            Integer minQuantity,
            ProductStatus status
    ) {}

    record Req(
            String productName,
            String capacity,
            Integer categoryId,
            List<DetailReq> details
    ) {}

    record UpdateReq(
            String productName,
            String capacity,
            Integer categoryId,
            List<DetailUpdateReq> details
    ) {}

    record DetailReq(
            String barcode,
            String unit,
            Double unitPrice,
            SaleType saleType
    ) {}

    record DetailUpdateReq(
            Integer productDetailId,
            String barcode,
            String unit,
            Double unitPrice,
            SaleType saleType,
            ProductStatus status
    ) {}

    record CreatedEvent(
            Integer productDetailId,
            String barcode,
            String name,
            Double unitPrice
    ) {}
}

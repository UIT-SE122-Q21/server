package edu.uit.se122.server.inventory;

import edu.uit.se122.server.common.enums.ProductStatus;
import edu.uit.se122.server.common.enums.SaleType;

import java.util.List;

public interface ProductContract {
    record Res(
            Integer productId,
            String productName,
            String capacity,
            ProductStatus status,
            String categoryName,
            List<DetailRes> detailResList
    ) {}

    record DetailRes(
            String productDetailId,
            String barcode,
            String unit,
            Double unitPrice,
            SaleType saleType,
            Integer quantity,
            Integer minQuantity
    ) {}

    record Req(
            String productName,
            String capacity,
            ProductStatus status,
            Integer categoryId,
            List<DetailReq> detailReqs
    ) {}

    record DetailReq(
            String barcode,
            String unit,
            Double unitPrice,
            SaleType saleType,
            Integer quantity,
            Integer minQuantity
    ) {}

    record CreatedEvent(
            Integer productDetailId,
            String barcode,
            String name,
            Double unitPrice
    ) {}
}

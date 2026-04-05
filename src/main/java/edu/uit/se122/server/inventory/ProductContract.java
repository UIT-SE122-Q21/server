package edu.uit.se122.server.inventory;

import edu.uit.se122.server.common.enums.ProductStatus;

public interface ProductContract {
    record Response(
            String productId,
            String barcode,
            String productName,
            Double unitPrice,
            Integer quantity,
            Integer minQuantity,
            ProductStatus status,
            String categoryName
    ) {}

    record Request(
            String barcode,
            String productName,
            Double unitPrice,
            Integer quantity,
            Integer minQuantity,
            ProductStatus status,
            Integer categoryId
    ) {}
}

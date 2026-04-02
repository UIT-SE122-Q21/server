package edu.uit.se122.server.inventory.internal.dto;

import lombok.Data;

@Data
public class ProductCategoryReqDTO {
    private String productCategoryName;
    private String description;
}

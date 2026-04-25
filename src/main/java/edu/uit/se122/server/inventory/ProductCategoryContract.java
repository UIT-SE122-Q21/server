package edu.uit.se122.server.inventory;

public interface ProductCategoryContract {
    record Res(
            Integer productCategoryId,
            String name,
            String description,
            String backgroundColor,
            String textColor
    ) {}

    record Req(
            String name,
            String description,
            String backgroundColor,
            String textColor
    ) {}
}

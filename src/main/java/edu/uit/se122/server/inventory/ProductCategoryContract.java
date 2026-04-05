package edu.uit.se122.server.inventory;

public interface ProductCategoryContract {
    record Response(
            String productCategoryId,
            String name,
            String description
    ) {}

    record Request(
            String name,
            String description
    ) {}
}

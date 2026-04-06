package edu.uit.se122.server.resource;

public interface FacilityCategoryContract {
    record Response(
            String facilityCategoryId,
            String name,
            String description
    ) {}

    record Request(
            String name,
            String description
    ) {}
}

package edu.uit.se122.server.inventory.internal.repository;

import edu.uit.se122.server.inventory.internal.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
}

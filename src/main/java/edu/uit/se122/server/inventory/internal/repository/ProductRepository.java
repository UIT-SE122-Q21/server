package edu.uit.se122.server.inventory.internal.repository;

import edu.uit.se122.server.inventory.internal.entity.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT MAX(p.productId) FROM Product p WHERE p.category.productCategoryId = :categoryId")
    Integer findMaxProductIdByCategoryId(@Param("categoryId") Integer categoryId);
}

package edu.uit.se122.server.inventory.internal.repository;

import edu.uit.se122.server.inventory.internal.entity.ProductDetail;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT MAX(pd.productDetailId) FROM ProductDetail pd WHERE pd.product.category.productCategoryId = :categoryId")
    Integer findMaxProductDetailIdByCategoryId(@Param("categoryId") Integer categoryId);
}

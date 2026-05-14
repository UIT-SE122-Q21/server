package edu.uit.se122.server.inventory.internal.repository;

import edu.uit.se122.server.inventory.internal.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {
}

package edu.uit.se122.server.inventory.internal.component;

import edu.uit.se122.server.inventory.internal.entity.ProductCategory;
import edu.uit.se122.server.inventory.internal.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductCategoryDataSeeder implements CommandLineRunner {

    private final ProductCategoryRepository productCategoryRepository;

    @Override
    public void run(String... args) {
        if (!productCategoryRepository.existsById(1)) {
            ProductCategory racket = new ProductCategory();
            racket.setName("Vợt");
            racket.setDescription(null);
            racket.setBackgroundColor("#E6F1FB");
            racket.setTextColor("#1565C0");

            productCategoryRepository.save(racket);
        }
    }
}

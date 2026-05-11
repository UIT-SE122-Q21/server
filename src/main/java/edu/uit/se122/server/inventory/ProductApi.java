package edu.uit.se122.server.inventory;

import edu.uit.se122.server.booking.ProductOrderContract;

import java.util.List;

public interface ProductApi {
    void DecreaseQuantity(List<ProductOrderContract.DetailReq> list);
}

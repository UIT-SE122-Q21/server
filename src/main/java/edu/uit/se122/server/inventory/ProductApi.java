package edu.uit.se122.server.inventory;

import edu.uit.se122.server.booking.OrderContract;

import java.util.List;

public interface ProductApi {
    void decreaseQuantity(List<OrderContract.ProductOrderDetailReq> list);
    Integer getQuantity(Integer productId);
}

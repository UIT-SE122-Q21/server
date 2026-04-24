package edu.uit.se122.server.booking.internal.service;

import edu.uit.se122.server.booking.ProductOrderContract;
import edu.uit.se122.server.booking.internal.entity.CourtOrder;
import edu.uit.se122.server.booking.internal.entity.ProductCache;
import edu.uit.se122.server.booking.internal.entity.ProductOrderDetail;
import edu.uit.se122.server.booking.internal.entity.ProductOrderInvoice;
import edu.uit.se122.server.booking.internal.repository.CourtOrderRepository;
import edu.uit.se122.server.booking.internal.repository.ProductCacheRepository;
import edu.uit.se122.server.booking.internal.repository.ProductOrderInvoiceRepository;
import edu.uit.se122.server.inventory.ProductApi;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductOrderService {
    private final ProductOrderInvoiceRepository invoiceRepository;
    private final CourtOrderRepository courtOrderRepository;
    private final ProductCacheRepository productCacheRepository;
    private final ProductApi productApi;

    public void calculateInvoiceByAdmin(ProductOrderContract.InvoiceRequest dto) {
        double totalAmount = 0;
        double changeAmount;
        CourtOrder courtOrder = courtOrderRepository.findById(dto.courtOrderId())
                .orElseThrow(() -> new RuntimeException("Court order not found"));

        for (ProductOrderContract.DetailRequest detail : dto.detailRequests()) {
            ProductCache productCache = productCacheRepository.findById(detail.productDetailId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            totalAmount += productCache.getUnitPrice() * detail.quantity();
        }
        productApi.DecreaseQuantity(dto.detailRequests());

        changeAmount = dto.givenAmount() - totalAmount;
        updateEntity(courtOrder, dto);
        ProductOrderInvoice invoice = new ProductOrderInvoice();
        invoice.setTotalAmount(totalAmount);
        invoice.setGivenAmount(dto.givenAmount());
        invoice.setChangeAmount(changeAmount);
        invoice.setCourtOrder(courtOrder);
        invoiceRepository.save(invoice);
    }

    private void updateEntity(CourtOrder entity, ProductOrderContract.InvoiceRequest dto) {
        List<ProductOrderDetail> details = dto.detailRequests().stream().map(detailRequest -> {
            ProductOrderDetail detail = new ProductOrderDetail();
            detail.setProductId(detailRequest.productDetailId());
            detail.setQuantity(detailRequest.quantity());
            detail.setDraft(true);
            detail.setCourtOrder(entity);
            return detail;
        }).toList();
        entity.setProductOrderDetails(details);
    }
}

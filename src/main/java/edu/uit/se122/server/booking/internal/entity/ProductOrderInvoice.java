package edu.uit.se122.server.booking.internal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "ProductOrderInvoice")
@Data
@ToString(exclude = "courtOrder")
@EqualsAndHashCode(exclude = "courtOrder")
public class ProductOrderInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productInvoiceId;

    private Double totalBeforeDiscount;

    private Double totalDiscount;

    private Double totalAmount;

    private Double changeAmount;

    private LocalDateTime createdAt;

    @OneToOne // Thường hóa đơn gắn liền 1-1 với đơn hàng
    @JoinColumn(name = "CourtOrderId")
    @JsonBackReference(value = "productOrderInvoices")
    private CourtOrder courtOrder;
}

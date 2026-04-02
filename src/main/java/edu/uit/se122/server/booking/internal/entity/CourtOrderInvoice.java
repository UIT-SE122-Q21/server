package edu.uit.se122.server.booking.internal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "CourtOrderInvoice")
@Data
@ToString(exclude = "courtOrder")
@EqualsAndHashCode(exclude = "courtOrder")
public class CourtOrderInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courtInvoiceId;

    private Integer quantity;

    private Double totalTime;

    private Double totalAmount;

    private LocalDateTime createdAt;

    @OneToOne // Thường hóa đơn gắn liền 1-1 với đơn hàng
    @JoinColumn(name = "CourtOrderId")
    @JsonBackReference(value = "courtOrderInvoices")
    private CourtOrder courtOrder;
}

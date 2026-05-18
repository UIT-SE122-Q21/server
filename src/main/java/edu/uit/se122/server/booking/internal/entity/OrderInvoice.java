package edu.uit.se122.server.booking.internal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import edu.uit.se122.server.common.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "OrderInvoice")
@Data
@EntityListeners(AuditingEntityListener.class)
@ToString(exclude = "courtOrder")
@EqualsAndHashCode(exclude = "courtOrder")
public class OrderInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productInvoiceId;

    private BigDecimal depositAmount;
    private BigDecimal totalBeforeDiscount;
    private BigDecimal totalDiscount;
    private BigDecimal totalAmount;
    private BigDecimal givenAmount;
    private BigDecimal changeAmount;
    private PaymentMethod paymentMethod;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToOne // Thường hóa đơn gắn liền 1-1 với đơn hàng
    @JoinColumn(name = "CourtOrderId")
    @JsonBackReference(value = "orderInvoices")
    private CourtOrder courtOrder;
}

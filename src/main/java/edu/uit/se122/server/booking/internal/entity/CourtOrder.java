package edu.uit.se122.server.booking.internal.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import edu.uit.se122.server.common.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "CourtOrder")
@Data
@EntityListeners(AuditingEntityListener.class)
@ToString(exclude = {"courtOrderDetails", "productOrderDetails", "courtOrderInvoices", "productOrderInvoices"})
@EqualsAndHashCode(exclude = {"courtOrderDetails", "productOrderDetails", "courtOrderInvoices", "productOrderInvoices"})
public class CourtOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courtOrderId;

    private LocalDate orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private String adminId;

    private String userId;

    private Boolean guest;

    // Navigation: Chi tiết các sân được đặt trong đơn này
    @OneToMany(mappedBy = "courtOrder", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "courtOrderDetails")
    private List<CourtOrderDetail> courtOrderDetails;

    @OneToMany(mappedBy = "courtOrder", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "productOrderDetails")
    private List<ProductOrderDetail> productOrderDetails;

    @OneToOne(mappedBy = "courtOrder", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "courtOrderInvoices")
    private CourtOrderInvoice courtOrderInvoices;

    @OneToOne(mappedBy = "courtOrder", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "productOrderInvoices")
    private ProductOrderInvoice productOrderInvoices;
}

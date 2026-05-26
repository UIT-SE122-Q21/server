package edu.uit.se122.server.booking.internal.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import edu.uit.se122.server.common.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CourtOrder")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CourtOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer courtOrderId;

    private LocalDate orderDate;

    private LocalTime startHour;

    private LocalTime endHour;

    @Setter
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private Integer adminId;

    private Integer memberId;

    private String guestName;

    private String guestEmail;

    private String guestPhoneNumber;

    @Builder.Default
    @OneToMany(mappedBy = "courtOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "courtOrderDetails")
    private List<CourtOrderDetail> courtOrderDetails = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "courtOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "productOrderDetails")
    private List<ProductOrderDetail> productOrderDetails = new ArrayList<>();

    @OneToOne(mappedBy = "courtOrder", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "orderInvoices")
    private OrderInvoice orderInvoice;

    public void addCourtOrderDetail(CourtOrderDetail courtOrderDetail) {
        if (courtOrderDetails != null) {
            this.courtOrderDetails.add(courtOrderDetail);
            courtOrderDetail.setCourtOrder(this);
        }
    }

    public void addProductOrderDetail(ProductOrderDetail productOrderDetail) {
        if (productOrderDetails != null) {
            this.productOrderDetails.add(productOrderDetail);
            productOrderDetail.setCourtOrder(this);
        }
    }

    public void setOrderInvoice(OrderInvoice orderInvoice) {
        if (orderInvoice != null) {
            this.orderInvoice = orderInvoice;
            orderInvoice.setCourtOrder(this);
        }
    }
}

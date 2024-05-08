package com.miri.paymentservice.domain.payment;

import com.miri.coremodule.domain.BaseTimeEntity;
import com.miri.coremodule.dto.kafka.PaymentRequestEventDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "payments")
public class Payment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private Integer amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    public Payment(Long userId, Long orderId, Integer amount, PaymentStatus paymentStatus) {
        this.userId = userId;
        this.orderId = orderId;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
    }

    public static Payment createFromPaymentRequestEventDto(PaymentRequestEventDto dto) {
        return new Payment(dto.getUserId(), dto.getOrderId(), dto.getQuantity() * dto.getGoodsPrice(),
                PaymentStatus.PAYMENT_SCREEN_ENTERED);
    }

    public void changePaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}

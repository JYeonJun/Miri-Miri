package com.miri.orderservice.domain.shipping;

import com.miri.orderservice.util.StringEncryptUniqueConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
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
@Table(name = "shipping")
public class Shipping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipping_id")
    private Long id;

    @Column(nullable = false, length = 36)
    private String trackingNumber;

    @Column(nullable = false)
    private Long orderDetailId;

    @Convert(converter = StringEncryptUniqueConverter.class)
    @Column(nullable = false, length = 255)
    private String address;

    public Shipping(Long orderDetailId, String address) {
        this.orderDetailId = orderDetailId;
        this.trackingNumber = UUID.randomUUID().toString();
        this.address = address;
    }
}
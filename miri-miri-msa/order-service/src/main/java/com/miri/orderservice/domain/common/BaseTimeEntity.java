package com.miri.orderservice.domain.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

@MappedSuperclass
@Getter
public class BaseTimeEntity extends CreatedDateEntity{

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime lastModifiedDate;
}


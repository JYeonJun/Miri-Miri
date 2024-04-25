package com.miri.orderservice.domain.user;

import com.miri.orderservice.domain.common.BaseTimeEntity;
import com.miri.orderservice.util.StringEncryptUniqueConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Convert(converter = StringEncryptUniqueConverter.class)
    @Column(unique = true, nullable = false, length = 255)
    private String email;

    @Convert(converter = StringEncryptUniqueConverter.class)
    @Column(nullable = false, length = 255)
    private String userName;

    @Column(nullable = false, length = 80) // 패스워드 인코딩(BCrypt)
    private String password;

    @Convert(converter = StringEncryptUniqueConverter.class)
    @Column(nullable = false, length = 255)
    private String phoneNumber;

    @Convert(converter = StringEncryptUniqueConverter.class)
    @Column(nullable = false, length = 255)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role; // USER, ADMIN

    public void changeUserProfile(String phoneNumber, String address) {
        if (phoneNumber != null) {
            this.phoneNumber = phoneNumber;
        }
        if (address != null) {
            this.address = address;
        }
    }

    public void changePassword(String password) {
        this.password = password;
    }
}

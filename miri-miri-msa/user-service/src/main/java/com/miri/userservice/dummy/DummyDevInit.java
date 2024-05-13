package com.miri.userservice.dummy;

import com.miri.userservice.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
@RequiredArgsConstructor
public class DummyDevInit extends DummyObject {

    private final UserRepository userRepository;

    @Bean
    @Profile("dev")
    CommandLineRunner devInit() {
        return (args) -> {
            if (userRepository.count() < 1000) {
                for (int i = 0; i < 1000; i++) {
                    String email = "user" + i + "@example.com";
                    String userName = "User" + i;
                    String phoneNumber = "010-1111-1111";
                    String address = "Some Address" + i;
                    userRepository.save(newUser(email, userName, phoneNumber, address));
                }
            }
        };
    }
}

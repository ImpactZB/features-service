package com.impactzb.featuresservice.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration {

    private final PasswordEncoder passwordEncoder;

    @Bean
    protected InMemoryUserDetailsManager userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        UserDetails admin = User.withUsername("admin")
                .password("admin")
                .passwordEncoder(passwordEncoder::encode)
                .roles(UserRole.ADMIN.name())
                .build();
        manager.createUser(admin);

        UserDetails user1 = User.withUsername("user1")
                .password("user1")
                .passwordEncoder(passwordEncoder::encode)
                .roles(UserRole.USER.name())
                .build();
        manager.createUser(user1);

        UserDetails user2 = User.withUsername("user2")
                .password("user2")
                .passwordEncoder(passwordEncoder::encode)
                .roles(UserRole.USER.name())
                .build();
        manager.createUser(user2);

        UserDetails user3 = User.withUsername("user3")
                .password("user3")
                .passwordEncoder(passwordEncoder::encode)
                .roles(UserRole.OBSERVER.name())
                .build();
        manager.createUser(user3);

        return manager;
    }
}
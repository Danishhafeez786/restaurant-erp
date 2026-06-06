package com.devmasters.restaurant_erp.config;

import com.devmasters.restaurant_erp.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class RefreshTokenCleanupScheduler {

    private final RefreshTokenRepository refreshTokenRepository;

    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanup() {

        refreshTokenRepository.findAll()
                .stream()
                .filter(token ->
                        token.getExpiryDate()
                                .isBefore(LocalDateTime.now()))
                .forEach(refreshTokenRepository::delete);
    }
}

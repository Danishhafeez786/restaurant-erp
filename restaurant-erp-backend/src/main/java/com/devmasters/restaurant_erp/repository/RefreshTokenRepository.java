package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository
        extends MongoRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByToken(String token);

    void deleteByUserId(UUID userId);
}

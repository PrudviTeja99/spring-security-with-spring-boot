package com.teja.securedapis.repository;

import com.teja.securedapis.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity,String> {
    Optional<RefreshTokenEntity> findByTokenAndExpiryAfter(String token, Instant expiry);
}

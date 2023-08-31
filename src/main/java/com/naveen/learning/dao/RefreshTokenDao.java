package com.naveen.learning.dao;

import com.naveen.learning.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenDao extends JpaRepository<RefreshToken,Long> {
    RefreshToken findByToken(String refreshToken);
}

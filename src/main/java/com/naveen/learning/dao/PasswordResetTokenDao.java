package com.naveen.learning.dao;

import com.naveen.learning.model.PasswordResetToken;
import com.naveen.learning.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasswordResetTokenDao extends JpaRepository<PasswordResetToken,Long> {
    PasswordResetToken findByToken(String token);

    @Query("SELECT t FROM PasswordResetToken t WHERE t.isActive = true and t.user = :user")
    List<PasswordResetToken> findActiveTokensForUser(User user);
}

package com.naveen.learning.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.naveen.learning.model.audit.Auditable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class PasswordResetToken extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(targetEntity = User.class,fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Column(name = "expiry_date",nullable = false)
    private Instant expiryDate;

    @Column(name = "token",nullable = false)
    private String token;

    @Column(name = "is_claimed",nullable = false)
    private boolean isClaimed;

    @Column(name = "is_active",nullable = false)
    private boolean isActive;

}

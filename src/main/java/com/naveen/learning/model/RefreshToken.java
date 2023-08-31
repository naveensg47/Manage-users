package com.naveen.learning.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "REFRESH_TOKEN")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false,cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "refresh_count")
    private Long refreshCount;

    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;

}

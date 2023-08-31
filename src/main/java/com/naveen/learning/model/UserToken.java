package com.naveen.learning.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
@Entity
@Table(name="User_token")
public class UserToken {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_used")
    private Date lastUsed;

    @Column(name = "secret_key")
    private byte[] secretKey;

    private String token;

    // bi-directional many-to-one association to SlUser
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

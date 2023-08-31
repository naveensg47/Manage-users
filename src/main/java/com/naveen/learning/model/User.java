package com.naveen.learning.model;

import com.naveen.learning.model.audit.Auditable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "USER")
@Audited
@AuditOverride(forClass = Auditable.class)
public class User extends Auditable {

    private static final long serialVersionUID = 2262860108351818088L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", allocationSize = 1)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "password")
    private String password;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @NotAudited
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "USER_ROLE", joinColumns = {
            @JoinColumn(name = "USER_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
            @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")
    })
    private Set<Role> roles = new HashSet<>();

    // bi-directional many-to-one association to SlUserToken
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @NotAudited
    private List<UserToken> userTokens;

    @LastModifiedDate
    @Column(name = "last_login")
    private Timestamp lastLogin;

    @Column(name = "is_email_verified", nullable = false)
    private boolean isEmailVerified;

    @OneToOne(optional = false, mappedBy = "user")
    private RefreshToken refreshToken;

    public User(User user) {
        id = user.getId();
        username = user.getUsername();
        password = user.getPassword();
        email = user.getEmail();
        isActive = user.isActive();
        roles = user.getRoles();
        lastLogin = user.lastLogin;
        isEmailVerified = user.isEmailVerified();
    }

    public void addRole(Role role) {
        roles.add(role);
        role.getUserList().add(this);
    }

    public void addRoles(Set<Role> roles) {
        roles.forEach(this::addRole);
    }

}

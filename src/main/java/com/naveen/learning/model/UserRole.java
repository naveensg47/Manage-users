package com.naveen.learning.model;

import com.naveen.learning.model.audit.Auditable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;

@Setter
@Getter
@Entity
@Table(name = "user_roles")
@Audited
public class UserRole extends Auditable {

    private static final long serialVersionUID = 1075275611926837887L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @NotAudited
    private Role role;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    public UserRole(User user, Role role, boolean deleted) {
        this.setId(id);
        this.setUser(user);
        this.setRole(role);
        this.setDeleted(deleted);
    }

}

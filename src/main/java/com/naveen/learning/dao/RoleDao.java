package com.naveen.learning.dao;

import com.naveen.learning.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleDao extends JpaRepository<Role,Long> {

    @Query(value = "SELECT r FROM Role r WHERE r.id IN (:ids)")
    List<Role> findRoleByIds(List<Long> ids);
}

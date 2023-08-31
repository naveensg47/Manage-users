package com.naveen.learning.dao;

import com.naveen.learning.model.EnumItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EnumItemDao extends JpaRepository<EnumItem,Long> {

    EnumItem findByEnumItemCode(String itemCode);
}

package com.naveen.learning.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "enum_item")
public class EnumItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "enum_type")
    private String enumType;

    @Column(name = "enum_item_code",unique = true)
    private String enumItemCode;

    @Column(name = "enum_item_name")
    private String enumItemName;

}

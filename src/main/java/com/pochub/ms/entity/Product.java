package com.pochub.ms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "DEMO_PRODUCTS")
public class Product {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String description;
    private Double price;
    @Column(updatable = false)
    private Date createDate;
    private Date lastModifiedDate;

    @PrePersist
    public void prePersist() {
        createDate = new Date();
        lastModifiedDate = createDate;
    }

    @PreUpdate
    public void preUpdate() {
        lastModifiedDate = new Date();
    }

}

package com.pochub.ms.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({ "title", "description", "price" })
public class ProductReq {

    private String title;
    private String description;
    private Double price;

}

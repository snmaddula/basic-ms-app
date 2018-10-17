package com.pochub.ms.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({ "id", "title", "description", "price", "createDate", "lastModifiedDate" })
public class ProductRes {

    private Long id;
    private String title;
    private String description;
    private Double price;
    @JsonFormat(shape = Shape.STRING, pattern = "dd-MM-yyyy")
    private Date createDate;
    @JsonFormat(shape = Shape.STRING, pattern = "dd-MM-yyyy")
    private Date lastModifiedDate;

}

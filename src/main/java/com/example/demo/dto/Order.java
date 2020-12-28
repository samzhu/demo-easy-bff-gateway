package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "orderSn", "orderDate", "customerId", "productId", "volumn" })
public class Order {
    @JsonProperty("orderSn")
    private Integer orderSn;
    @JsonProperty("orderDate")
    private String orderDate;
    @JsonProperty("customerId")
    private String customerId;
    @JsonProperty("productId")
    private Integer productId;
    @JsonProperty("volumn")
    private Integer volumn;
}

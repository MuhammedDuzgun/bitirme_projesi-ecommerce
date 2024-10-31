package com.bitirmeprojesi.ecommerce.request;

import lombok.Data;

@Data
public class AddItemRequst {
    private Long productId;
    private String size;
    private int quantity;
}

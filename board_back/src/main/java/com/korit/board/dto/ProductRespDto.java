package com.korit.board.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductRespDto {
    private int productId;
    private String productName;
    private int productPrice;
}

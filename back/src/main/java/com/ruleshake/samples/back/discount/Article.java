package com.ruleshake.samples.back.discount;

import lombok.Data;

@Data
public class Article {

    private String barcode;
    private String label;
    private String category;
    private String image;
    private Double price;

}

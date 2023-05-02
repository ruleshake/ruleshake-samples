package com.ruleshake.samples.back.model.discount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Discount {

    private String label;
    private String description;
    private Double amount;

}

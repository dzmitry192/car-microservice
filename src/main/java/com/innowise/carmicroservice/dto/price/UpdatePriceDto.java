package com.innowise.carmicroservice.dto.price;

import jakarta.validation.constraints.Min;
import lombok.Data;


@Data
public class UpdatePriceDto {
    @Min(value = 1, message = "Hour's price should not be less than 0")
    private Integer hour;
    @Min(value = 1, message = "Day's price should not be less than 0")
    private Integer day;
}

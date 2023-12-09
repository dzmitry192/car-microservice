package com.innowise.carmicroservice.dto.price;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePriceDto {
    @NotNull(message = "Price for hour cannot be empty")
    @Min(value = 1, message = "Hour's price should not be less than 0")
    private int hour;
    @NotNull(message = "Price for day cannot be empty")
    @Min(value = 1, message = "Day's price should not be less than 0")
    private int day;
}

package com.innowise.carmicroservice.dto.price;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceDto {
    @NotNull
    private Long id;
    @NotBlank
    @Min(value = 1, message = "Hour's price should not be less than 0")
    private int hour;
    @NotNull
    @Min(value = 1, message = "Day's price should not be less than 0")
    private int day;
}

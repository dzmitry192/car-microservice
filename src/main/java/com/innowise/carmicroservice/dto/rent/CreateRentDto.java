package com.innowise.carmicroservice.dto.rent;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRentDto {
    @NotNull(message = "Rental start date cannot be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date rentalStartDate;
    @NotNull(message = "Rental end date cannot be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date rentalEndDate;
    @NotNull
    @Min(value = 1, message = "Rental amount should not be less than 0")
    private int rentalAmount;
}

package com.innowise.carmicroservice.dto.car;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCarDto {
    private String band;
    private String model;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date yearOfIssue;
    private String licensePlate;
    private String fuelType;
    @Min(value = 1, message = "Engine capacity cannot by less than 0")
    private int engineCapacity;
    private String color;
}

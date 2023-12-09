package com.innowise.carmicroservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "cars")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Band cannot be empty")
    private String band;
    @NotBlank(message = "Model cannot be empty")
    private String model;
    @NotNull(message = "Year of issue cannot be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date yearOfIssue;
    @NotBlank(message = "License plate cannot be empty")
    private String licensePlate;
    @NotBlank(message = "Fuel type cannot be empty")
    private String fuelType;
    @Min(value = 1, message = "Engine capacity should not be less than 0")
    @NotNull
    private int engineCapacity;
    @NotBlank(message = "Color cannot be empty")
    private String color;
    private boolean isUsed;
    private Long rentId;
}

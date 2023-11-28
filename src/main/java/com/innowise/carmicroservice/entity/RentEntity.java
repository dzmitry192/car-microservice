package com.innowise.carmicroservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "rent")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Rental start date cannot be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date rentalStartDate;
    @NotNull(message = "Rental end date cannot be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date rentalEndDate;
    @NotNull
    @Min(value = 1, message = "Rental amount should not be less than 0")
    private int rentalAmount;
    private Long carId;
    private Long clientId;
    private Long priceId;
    private Long paymentId;
}

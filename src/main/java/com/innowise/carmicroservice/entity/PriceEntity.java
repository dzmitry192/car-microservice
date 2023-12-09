package com.innowise.carmicroservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Entity
@Table(name = "prices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Min(value = 1, message = "Hour's price should not be less than 0")
    private int hour;
    @NotNull
    @Min(value = 1, message = "Day's price should not be less than 0")
    private int day;
}

package com.innowise.carmicroservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "cars")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class CarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String band;
    @NonNull
    private String model;
    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date year_of_issue;
    @NonNull
    private String license_plate;
    @NonNull
    private String fuel_type;
    @NonNull
    private int engine_capacity;
    @NonNull
    private String color;
    private boolean isUsed = false;
    private Long rent_id;
}

package com.innowise.carmicroservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "reservation")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date reservation_start_date;
    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date reservation_end_date;
    @NonNull
    private Long car_id;
    @NonNull
    private Long client_id;
}

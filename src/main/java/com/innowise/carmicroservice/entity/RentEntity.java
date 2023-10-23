package com.innowise.carmicroservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "rent")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class RentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date rental_start_date;
    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date rental_end_date;
    @NonNull
    private int rental_amount;
    @NonNull
    private Long car_id;
    @NonNull
    private Long client_id;
    @NonNull
    private Long price_id;
    private Long payment_id;
}

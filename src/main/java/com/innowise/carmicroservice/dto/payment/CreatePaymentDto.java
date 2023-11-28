package com.innowise.carmicroservice.dto.payment;

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
public class CreatePaymentDto {
    @NotNull
    @Min(value = 1, message = "Amount should not be less than 0")
    private int amount;
    @NotNull(message = "Payment date cannot be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date paymentDate;
}

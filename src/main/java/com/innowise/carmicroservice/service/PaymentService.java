package com.innowise.carmicroservice.service;

import com.innowise.carmicroservice.dto.payment.CreatePaymentDto;
import com.innowise.carmicroservice.dto.payment.PaymentDto;
import com.innowise.carmicroservice.dto.payment.UpdatePaymentDto;
import com.innowise.carmicroservice.exception.BadRequestException;
import com.innowise.carmicroservice.exception.NotFoundException;

import java.util.List;

public interface PaymentService {
    List<PaymentDto> getPayments();

    PaymentDto getPaymentById(Long paymentId) throws NotFoundException;

    PaymentDto createPayment(Long rentId, Long clientId, CreatePaymentDto createPaymentDto) throws NotFoundException, BadRequestException;

    PaymentDto updatePaymentById(Long paymentId, UpdatePaymentDto updatePaymentDto) throws NotFoundException;

    String deletePaymentById(Long paymentId) throws NotFoundException;
}

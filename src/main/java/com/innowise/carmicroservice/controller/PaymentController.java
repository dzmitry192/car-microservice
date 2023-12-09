package com.innowise.carmicroservice.controller;

import com.innowise.carmicroservice.dto.payment.CreatePaymentDto;
import com.innowise.carmicroservice.dto.payment.PaymentDto;
import com.innowise.carmicroservice.dto.payment.UpdatePaymentDto;
import com.innowise.carmicroservice.exception.BadRequestException;
import com.innowise.carmicroservice.exception.NotFoundException;
import com.innowise.carmicroservice.service.impl.PaymentServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentServiceImpl paymentService;

    @GetMapping("/")
    @PreAuthorize("hasAnyAuthority('ROLE_MODERATOR', 'ROLE_ADMIN')")
    public ResponseEntity<List<PaymentDto>> getPayments() {
        return ResponseEntity.ok().body(paymentService.getPayments());
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentDto> getPaymentById(@PathVariable Long paymentId) throws NotFoundException {
        return ResponseEntity.ok().body(paymentService.getPaymentById(paymentId));
    }

    @PostMapping("/{rendId}/{clientId}")
    public ResponseEntity<PaymentDto> createPayment(@PathVariable Long rendId, @PathVariable Long clientId, @Valid @RequestBody CreatePaymentDto createPaymentDto) throws NotFoundException, BadRequestException {
        return ResponseEntity.ok().body(paymentService.createPayment(rendId, clientId, createPaymentDto));
    }

    @PatchMapping("/{paymentId}")
    public ResponseEntity<PaymentDto> updatePaymentById(@PathVariable Long paymentId, @Valid @RequestBody UpdatePaymentDto updatePaymentDto) throws NotFoundException {
        return ResponseEntity.ok().body(paymentService.updatePaymentById(paymentId, updatePaymentDto));
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<String> deletePaymentById(@PathVariable Long paymentId) throws NotFoundException {
        return ResponseEntity.ok().body(paymentService.deletePaymentById(paymentId));
    }

}

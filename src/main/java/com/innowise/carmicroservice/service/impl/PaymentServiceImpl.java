package com.innowise.carmicroservice.service.impl;

import com.innowise.carmicroservice.dto.payment.CreatePaymentDto;
import com.innowise.carmicroservice.dto.payment.PaymentDto;
import com.innowise.carmicroservice.dto.payment.UpdatePaymentDto;
import com.innowise.carmicroservice.entity.PaymentEntity;
import com.innowise.carmicroservice.entity.RentEntity;
import com.innowise.carmicroservice.exception.BadRequestException;
import com.innowise.carmicroservice.exception.NotFoundException;
import com.innowise.carmicroservice.mapper.PaymentMapperImpl;
import com.innowise.carmicroservice.repository.PaymentRepository;
import com.innowise.carmicroservice.repository.RentRepository;
import com.innowise.carmicroservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final RentRepository rentRepository;

    @Override
    public List<PaymentDto> getPayments() {
        return PaymentMapperImpl.INSTANCE.toPaymentDtosList(paymentRepository.findAll());
    }

    @Override
    public PaymentDto getPaymentById(Long paymentId) throws NotFoundException {
        Optional<PaymentEntity> optionalPayment = paymentRepository.findById(paymentId);
        if (optionalPayment.isEmpty()) {
            throw new NotFoundException("Payment with id = " + paymentId + " not found");
        }
        return PaymentMapperImpl.INSTANCE.paymentEntityToPaymentDto(optionalPayment.get());
    }

    @Override
    public PaymentDto createPayment(Long rentId, Long clientId, CreatePaymentDto createPaymentDto) throws NotFoundException, BadRequestException {
        Optional<RentEntity> optionalRent = rentRepository.findById(rentId);
        if (optionalRent.isEmpty()) {
            throw new NotFoundException("Rent with id = " + rentId + " not found");
        }
        RentEntity rent = optionalRent.get();
        rent.setPaymentId(rentId);
        rentRepository.save(rent);

        PaymentEntity payment = PaymentMapperImpl.INSTANCE.createPaymentDtoToPaymentEntity(createPaymentDto);
        payment.setClientId(clientId);
        payment.setRentId(rentId);


        return PaymentMapperImpl.INSTANCE.paymentEntityToPaymentDto(paymentRepository.save(payment));
    }

    @Override
    public PaymentDto updatePaymentById(Long paymentId, UpdatePaymentDto updatePaymentDto) throws NotFoundException {
        Optional<PaymentEntity> optionalPayment = paymentRepository.findById(paymentId);
        if (optionalPayment.isEmpty()) {
            throw new NotFoundException("Payment with id = " + paymentId + " not found");
        }
        PaymentEntity payment = optionalPayment.get();
        return PaymentMapperImpl.INSTANCE.paymentEntityToPaymentDto(paymentRepository.save(PaymentMapperImpl.INSTANCE.updatePaymentDtoToPaymentDto(updatePaymentDto, payment)));
    }

    @Override
    public String deletePaymentById(Long paymentId) throws NotFoundException {
        Optional<PaymentEntity> optionalPayment = paymentRepository.findById(paymentId);
        if (optionalPayment.isEmpty()) {
            throw new NotFoundException("Payment with id = " + paymentId + " not found");
        }
        PaymentEntity payment = optionalPayment.get();

        RentEntity rent = rentRepository.findById(payment.getRentId()).get();
        rent.setPaymentId(null);
        rentRepository.save(rent);

        paymentRepository.delete(payment);
        return "Payment with id = " + paymentId + " was successfully deleted";
    }
}

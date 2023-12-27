package com.innowise.carmicroservice.service.impl;

import avro.ClientActionRequest;
import avro.NotificationRequest;
import com.innowise.carmicroservice.dto.payment.CreatePaymentDto;
import com.innowise.carmicroservice.dto.payment.PaymentDto;
import com.innowise.carmicroservice.dto.payment.UpdatePaymentDto;
import com.innowise.carmicroservice.entity.PaymentEntity;
import com.innowise.carmicroservice.entity.RentEntity;
import com.innowise.carmicroservice.enums.ActionEnum;
import com.innowise.carmicroservice.enums.ActionTypeEnum;
import com.innowise.carmicroservice.exception.BadRequestException;
import com.innowise.carmicroservice.exception.NotFoundException;
import com.innowise.carmicroservice.kafka.KafkaProducers;
import com.innowise.carmicroservice.mapper.PaymentMapperImpl;
import com.innowise.carmicroservice.repository.PaymentRepository;
import com.innowise.carmicroservice.repository.RentRepository;
import com.innowise.carmicroservice.security.service.CustomUserDetails;
import com.innowise.carmicroservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final RentRepository rentRepository;
    private final KafkaProducers kafkaProducers;

    @Override
    public List<PaymentDto> getPayments() {
        List<PaymentDto> paymentDtoList = PaymentMapperImpl.INSTANCE.toPaymentDtosList(paymentRepository.findAll());
        kafkaProducers.sendClientActionRequest(new ClientActionRequest(
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail(),
                ActionEnum.GET_PAYMENTS.getAction(),
                ActionTypeEnum.GET_PAYMENTS_TYPE.name(),
                LocalDateTime.now().toString()
        ));
        return paymentDtoList;
    }

    @Override
    public PaymentDto getPaymentById(Long paymentId) throws NotFoundException {
        Optional<PaymentEntity> optionalPayment = paymentRepository.findById(paymentId);
        if (optionalPayment.isEmpty()) {
            throw new NotFoundException("Payment with id = " + paymentId + " not found");
        }
        PaymentDto paymentDto = PaymentMapperImpl.INSTANCE.paymentEntityToPaymentDto(optionalPayment.get());
        kafkaProducers.sendClientActionRequest(new ClientActionRequest(
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail(),
                ActionEnum.GET_PAYMENT.getAction() + paymentDto.getId(),
                ActionTypeEnum.GET_PAYMENTS_TYPE.name(),
                LocalDateTime.now().toString()
        ));
        return paymentDto;
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

        PaymentDto paymentDto = PaymentMapperImpl.INSTANCE.paymentEntityToPaymentDto(paymentRepository.save(payment));

        kafkaProducers.sendNotificationRequest(new NotificationRequest(
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getEmail(),
                "Оплата аренды",
                "Вы успешно оплати аренду авто на сумму - " + payment.getAmount()
        ));

        kafkaProducers.sendClientActionRequest(new ClientActionRequest(
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail(),
                ActionEnum.CREATE_PAYMENT.getAction() + paymentDto.getId(),
                ActionTypeEnum.CREATE_PAYMENT_TYPE.name(),
                LocalDateTime.now().toString()
        ));

        return paymentDto;
    }

    @Override
    public PaymentDto updatePaymentById(Long paymentId, UpdatePaymentDto updatePaymentDto) throws NotFoundException {
        Optional<PaymentEntity> optionalPayment = paymentRepository.findById(paymentId);
        if (optionalPayment.isEmpty()) {
            throw new NotFoundException("Payment with id = " + paymentId + " not found");
        }
        PaymentEntity payment = optionalPayment.get();
        PaymentDto paymentDto = PaymentMapperImpl.INSTANCE.paymentEntityToPaymentDto(paymentRepository.save(PaymentMapperImpl.INSTANCE.updatePaymentDtoToPaymentDto(updatePaymentDto, payment)));

        kafkaProducers.sendClientActionRequest(new ClientActionRequest(
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail(),
                ActionEnum.UPDATE_PAYMENT.getAction() + paymentDto.getId(),
                ActionTypeEnum.UPDATE_PAYMENT_TYPE.name(),
                LocalDateTime.now().toString()
        ));

        return paymentDto;
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

        kafkaProducers.sendClientActionRequest(new ClientActionRequest(
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail(),
                ActionEnum.DELETE_PAYMENT.getAction() + paymentId,
                ActionTypeEnum.DELETE_PAYMENT_TYPE.name(),
                LocalDateTime.now().toString()
        ));

        return "Payment with id = " + paymentId + " was successfully deleted";
    }
}

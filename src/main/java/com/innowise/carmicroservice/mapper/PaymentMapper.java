package com.innowise.carmicroservice.mapper;

import com.innowise.carmicroservice.dto.payment.CreatePaymentDto;
import com.innowise.carmicroservice.dto.payment.PaymentDto;
import com.innowise.carmicroservice.dto.payment.UpdatePaymentDto;
import com.innowise.carmicroservice.entity.PaymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = PaymentMapper.class)
public interface PaymentMapper {
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    @Mapping(target = "rentId", ignore = true)
    @Mapping(target = "clientId", ignore = true)
    PaymentEntity createPaymentDtoToPaymentEntity(CreatePaymentDto createPaymentDto);
    PaymentEntity updatePaymentDtoToPaymentDto(UpdatePaymentDto updatePaymentDto, @MappingTarget PaymentEntity paymentEntity);
    PaymentDto paymentEntityToPaymentDto(PaymentEntity paymentEntity);
    PaymentEntity paymentDtoToPaymentEntity(PaymentDto paymentDto);
    List<PaymentDto> toPaymentDtosList(List<PaymentEntity> paymentEntities);
    List<PaymentEntity> toPaymentEntitiesList(List<PaymentDto> paymentDtos);
}

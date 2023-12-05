package com.innowise.carmicroservice.kafka;

import avro.DeleteClientRequest;
import avro.DeleteClientResponse;
import avro.UserDetailsResponse;
import com.innowise.carmicroservice.service.impl.RentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Service
@RequiredArgsConstructor
public class KafkaListeners {
    private final CountDownLatch countDownLatch = new CountDownLatch(1);
    private UserDetailsResponse userDetailsResponse;
    private final RentServiceImpl rentService;
    private final KafkaProducers kafkaProducers;

    @KafkaListener(topics = "${kafka.topics.user_details_response}", groupId = "user_details_response_id", containerFactory = "listenerContainerFactory")
    public void userDetailsListener(UserDetailsResponse userDetailsResponse) {
        this.userDetailsResponse = userDetailsResponse;
        countDownLatch.countDown();
    }

    @KafkaListener(topics = "${kafka.topics.delete_client_request}", groupId = "delete_client_request_id", containerFactory = "listenerContainerFactory")
    public void deleteClientListener(DeleteClientRequest deleteClientRequest) {
        kafkaProducers.sendDeleteClientResponse(new DeleteClientResponse(rentService.isHasRentOrReservation(deleteClientRequest.getClientId())));
    }
    public UserDetailsResponse waitForUserDetailsResponse() throws InterruptedException {
        countDownLatch.await();
        return userDetailsResponse;
    }
}
package com.innowise.carmicroservice.kafka;

import avro.UserDetailsResponse;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Service
public class KafkaListeners {
    private final CountDownLatch countDownLatch = new CountDownLatch(1);
    private UserDetailsResponse userDetailsResponse;

    @KafkaListener(topics = "${kafka.topics.user_details_response}", groupId = "user_details_response_id", containerFactory = "listenerContainerFactory")
    public void userDetailsListener(UserDetailsResponse userDetailsResponse) {
        this.userDetailsResponse = userDetailsResponse;
        countDownLatch.countDown();
    }

    public UserDetailsResponse waitForUserDetailsResponse() throws InterruptedException {
        countDownLatch.await();
        return userDetailsResponse;
    }
}
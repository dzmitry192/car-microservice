package com.innowise.carmicroservice.kafka;

import avro.UserDetailsRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducers {

    @Value(value = "${kafka.topics.user_details_request}")
    private String topicUserDetailsRequest;
    @NonNull
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUserDetailsRequest(UserDetailsRequest userDetailsRequest) {
        kafkaTemplate.send(topicUserDetailsRequest, userDetailsRequest);
        System.out.println("UserDetailsRequest was sent");
    }
}

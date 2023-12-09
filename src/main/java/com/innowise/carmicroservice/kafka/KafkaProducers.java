package com.innowise.carmicroservice.kafka;

import avro.DeleteClientResponse;
import avro.NotificationRequest;
import avro.UserDetailsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducers {

    @Value(value = "${kafka.topics.user_details_request}")
    private String topicUserDetailsRequest;
    @Value(value = "${kafka.topics.delete_client_response}")
    private String topicDeleteClientResponse;
    @Value(value = "${kafka.topics.notification_request}")
    private String topicNotificationRequest;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUserDetailsRequest(UserDetailsRequest userDetailsRequest) {
        kafkaTemplate.send(topicUserDetailsRequest, userDetailsRequest);
    }

    public void sendDeleteClientResponse(DeleteClientResponse deleteClientResponse) {
        kafkaTemplate.send(topicDeleteClientResponse, deleteClientResponse);
    }

    public void sendNotificationRequest(NotificationRequest notificationRequest) {
        kafkaTemplate.send(topicNotificationRequest, notificationRequest);
    }
}

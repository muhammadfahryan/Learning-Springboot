package com.example.springboot.kafka;

import com.example.User;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaAvroProducer {

    @Autowired
    private KafkaTemplate<String, User> kafkaTemplate;

    public void send(String topic) {
        // Contoh data user
        User user = User.newBuilder()
                .setName("John Doe")
                .setAge(30)
                .build();

        ProducerRecord<String, User> record = new ProducerRecord<>(topic, user);
        kafkaTemplate.send(record);
        System.out.println("Produced User: " + user.getName() + ", Age: " + user.getAge());
    }
}

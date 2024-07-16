package com.example.springboot.kafka;

import com.example.User;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaAvroConsumer {

    @KafkaListener(topics = "${spring.kafka.topic}", containerFactory = "kafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, User> record) {
        User user = record.value();
        String name = user.get("name").toString();
        int age = (int) user.get("age");
        System.out.println("Consumed User: " + name + ", Age: " + age);
    }
}
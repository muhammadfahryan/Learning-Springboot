package com.example.springboot.config;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Configuration
public class KafkaAdminClient {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public AdminClient adminClient() {
        return AdminClient.create(kafkaAdmin().getConfigurationProperties());
    }


    public void createNewTopic(String topicName, int numPartitions, short replicationFactor) {
        try (AdminClient adminClient = adminClient()) {
            NewTopic newTopic = new NewTopic(topicName, numPartitions, replicationFactor);
            CreateTopicsResult createTopicsResult = adminClient.createTopics(Collections.singletonList(newTopic));
            createTopicsResult.all().get();
            System.out.println("Topic " + topicName + " created successfully.");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void deleteTopic(String topicName) {
        try (AdminClient adminClient = adminClient()) {
            DeleteTopicsResult deleteTopicsResult = adminClient.deleteTopics(Collections.singletonList(topicName));
            KafkaFuture<Void> future = deleteTopicsResult.all();
            future.get();
            System.out.println("Topic " + topicName + " deleted successfully.");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public Set<String> listTopics() {
        try (AdminClient adminClient = adminClient()) {
            ListTopicsResult listTopicsResult = adminClient.listTopics();
            KafkaFuture<Set<String>> names = listTopicsResult.names();
            Set<String> topicNames = names.get();
            System.out.println("Topics :");
            topicNames.forEach(System.out::println);
            return topicNames;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return Collections.emptySet();
        }
    }
}
package com.example.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.springboot.config.*;
import com.example.springboot.kafka.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

import java.util.Scanner;

@SpringBootApplication
@EnableKafka
public class SpringbootApplication implements CommandLineRunner {

	@Autowired
	private KafkaAvroProducer kafkaAvroProducer;

	@Autowired
	private KafkaAvroConsumer kafkaAvroConsumer;

	@Autowired
	private KafkaAdminClient kafkaAdminClient;

	public static void main(String[] args) {
		SpringApplication.run(SpringbootApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Enter action (create, delete, list, produce, consume): ");
		String action = scanner.nextLine();

		switch (action) {
			case "create":
				System.out.println("Enter topic name: ");
				String topicName = scanner.nextLine();
				System.out.println("Enter number of partitions: ");
				int partitions = scanner.nextInt();
				System.out.println("Enter replication factor: ");
				short replicationFactor = scanner.nextShort();
				kafkaAdminClient.createNewTopic(topicName, partitions, replicationFactor);
				break;
			case "delete":
				System.out.println("Enter topic name: ");
				String deleteTopicName = scanner.nextLine();
				kafkaAdminClient.deleteTopic(deleteTopicName);
				break;
			case "list":
				kafkaAdminClient.listTopics();
				break;
			case "produce":
				System.out.println("Enter topic name: ");
				String produceTopic = scanner.nextLine();
				kafkaAvroProducer.send(produceTopic);
				break;
			default:
				System.out.println("Unknown action: " + action);
		}
	}
}

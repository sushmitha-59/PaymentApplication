package com.example.user.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Properties;

@Configuration
public class kafkaConfig {
    //this kafka config is responsible for configuring how the application will send the events to kafka producer topic
    //first get connection to the kafka as a producer
    //User here is a producer as it will push/publish events to the kafka

    @Value("${kafka.bootstrap.servers.config}")
    public String bootstrapServerConfig;

    @Bean
    public ProducerFactory getproducerFactory() { //ProducerFactory class is responsible for instantiating an producer object to push the events to topic
        //we need properties object to set the properties to the kafka , it ensures organization
        //kafka expects a configuration to connect to it and send the data correctly, without this object it doesnt know where to connect and how to serialize
        try {
            Properties properties = new Properties();
            properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServerConfig); //for connecting to the kafka server
            properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            return new DefaultKafkaProducerFactory(properties);
        } catch (Exception e) {
            throw new RuntimeException("Unable to connect to kafka");
        }
        //it will create kafka producer instance with provided properties
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() { //this template will be having all apis methods to interact with the kafka
        System.out.println("Getting kafka connection");
        return new KafkaTemplate<>(getproducerFactory()); //to interact easily with kafka we need this template , it has every method already defined
    }
}

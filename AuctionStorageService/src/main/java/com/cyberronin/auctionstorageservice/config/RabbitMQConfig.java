package com.cyberronin.auctionstorageservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig
{
    // Spring auto config will automatically make these following beans:
    // ConnectionFactory
    // RabbitTemplate
    // RabbitAdmin

    // Setup the converter for sending and receiving JSON messages to RabbitMQ
    @Bean
    public MessageConverter converter(){
        return new JacksonJsonMessageConverter();
    }

    // Set the JSON converter in the RabbitTemplate
    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory){
        RabbitTemplate  rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}

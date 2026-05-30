package com.cyberronin.auctionservice.config;

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
    @Value("${rabbitmq.queue.name}")
    private String QUEUE_NAME;

    @Value("${rabbitmq.exchange.name}")
    private String EXCHANGE_NAME;

    @Value("${rabbitmq.routing-key}")
    private String ROUTING_KEY;

    // Bean for exchange (only 1 beans serves multiple queues with different routing keys)
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(EXCHANGE_NAME);
    }

    // Spring auto config will automatically make these following beans:
    // ConnectionFactory
    // RabbitTemplate
    // RabbitAdmin

    // Setup the converter for sending JSON messages to RabbitMQ
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

    // ---------------QUEUE and BINDING CONFIGURATION-------------------------------

    // Spring bean for queue
    @Bean
    public Queue queue(){
        return new Queue(QUEUE_NAME);
    }

    // bind btw queue and exchange using routing key
    @Bean
    public Binding binding(){
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(ROUTING_KEY);
    }

}

package com.example.RabbitMQReceiver.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {


    // Bean que cria a fila
    @Bean
    public Queue queueUpdate() {
        return new Queue("payment.v1.payement-update");
    }

    @Bean
    public Queue queuePayment() {
        return new Queue("payment.v1.payement-created");
    }

    @Bean
    public Binding bindingCreated(){
        Queue queue = new Queue("payment.v1.payement-created");
        FanoutExchange fanoutExchange = new FanoutExchange("payment.v1.exchange");
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }

    @Bean
    public Binding bindingUpdate(){
        Queue queue = new Queue("payment.v1.payement-update");
        FanoutExchange fanoutExchange = new FanoutExchange("payment.v1.exchange");
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }

    // chama o método de criação da fila ao iniciar o sistema
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
        return new RabbitAdmin(connectionFactory);
    }


    // ao inciar o sistema, chama o rabbitAdmin, que por sua vez chama o queue e cria a nossa fila
    @Bean
    public ApplicationListener<ApplicationReadyEvent> applicationReadyEventApplicationListener(RabbitAdmin rabbitAdmin){
        return event -> rabbitAdmin.initialize();
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}

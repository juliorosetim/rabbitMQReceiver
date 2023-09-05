package com.example.RabbitMQReceiver.configuration;

import com.example.RabbitMQReceiver.entity.PaymentEntity;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {

    @RabbitListener (queues = "payment.v1.payement-created")
    public void onOrderCreated(PaymentEntity paymentEntity){
        System.out.println("Id recebido " + paymentEntity);
    }
}

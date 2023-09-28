package br.com.veiga.shoporder.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static br.com.veiga.shoporder.utils.QueueUtils.EXCHANGE_NAME;
import static br.com.veiga.shoporder.utils.QueueUtils.QUEUE_NAME;

@Component
public class MQConfig {

    private final AmqpAdmin amqpAdmin;

    public MQConfig(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }

    private Queue queue(String queueName) {
        return new Queue(queueName, true, false, false);
    }

    private DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    private Binding relate(Queue queue, DirectExchange exchange) {
        return new Binding(queue.getName(), Binding.DestinationType.QUEUE,
                directExchange().getName(), queue.getName(), null);
    }

    @PostConstruct
    private void create() {
        Queue queue = queue(QUEUE_NAME);
        DirectExchange directExchange = directExchange();
        Binding relate = relate(queue, directExchange);

        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareExchange(directExchange);
        amqpAdmin.declareBinding(relate);
    }
}

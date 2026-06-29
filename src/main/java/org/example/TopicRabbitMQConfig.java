package org.example;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicRabbitMQConfig {

    public static final String TOPIC_EXCHANGE  = "topic.exchange";
    public static final String TOPIC_QUEUE_1   = "topic.queue.orders";
    public static final String TOPIC_QUEUE_2   = "topic.queue.all";

    // "order.created", "order.updated" — попадут в queue.orders
    public static final String ROUTING_KEY_ORDERS = "order.#";

    // любые сообщения — попадут в queue.all
    public static final String ROUTING_KEY_ALL    = "#";

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public Queue topicQueueOrders() {
        return new Queue(TOPIC_QUEUE_1, true);
    }

    @Bean
    public Queue topicQueueAll() {
        return new Queue(TOPIC_QUEUE_2, true);
    }

    @Bean
    public Binding topicBindingOrders(Queue topicQueueOrders, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueueOrders).to(topicExchange).with(ROUTING_KEY_ORDERS);
    }

    @Bean
    public Binding topicBindingAll(Queue topicQueueAll, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueueAll).to(topicExchange).with(ROUTING_KEY_ALL);
    }
}
package org.example;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class HeadersRabbitMQConfig {

    public static final String HEADERS_EXCHANGE = "headers.exchange";
    public static final String HEADERS_QUEUE_1  = "headers.queue.pdf";
    public static final String HEADERS_QUEUE_2  = "headers.queue.xml";

    @Bean
    public HeadersExchange headersExchange() {
        return new HeadersExchange(HEADERS_EXCHANGE);
    }

    @Bean
    public Queue headersQueuePdf() {
        return new Queue(HEADERS_QUEUE_1, true);
    }

    @Bean
    public Queue headersQueueXml() {
        return new Queue(HEADERS_QUEUE_2, true);
    }

    // whereAll — ВСЕ указанные заголовки должны совпасть (x-match=all)
    @Bean
    public Binding headersBindingPdf(Queue headersQueuePdf, HeadersExchange headersExchange) {
        return BindingBuilder.bind(headersQueuePdf)
                .to(headersExchange)
                .whereAll(Map.of("format", "pdf", "type", "report"))
                .match();
    }

    // whereAny — достаточно совпадения ХОТЯ БЫ одного заголовка (x-match=any)
    @Bean
    public Binding headersBindingXml(Queue headersQueueXml, HeadersExchange headersExchange) {
        return BindingBuilder.bind(headersQueueXml)
                .to(headersExchange)
                .whereAny(Map.of("format", "xml"))
                .match();
    }
}
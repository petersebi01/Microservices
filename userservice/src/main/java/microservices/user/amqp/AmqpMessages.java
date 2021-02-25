package microservices.user.amqp;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile({"userdata", "pub-sub", "publish-subscribe"})
@Configuration
public class AmqpMessages {

    static final String exchangeName = "user-exchange";
    static final String queueName = "user-Queue";
    //static final String routingKey = "user.user";

    //private RabbitTemplate rabbitTemplate;

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(exchangeName);
    }

    @Profile("receiver")
    private static class ReceiverConfig {
        @Bean
        public Queue fanoutQueue() {
            return new AnonymousQueue();
        }

        @Bean
        public Binding binding(FanoutExchange fanoutExchange, Queue queue) {
            return BindingBuilder.bind(queue).to(fanoutExchange);
        }

        @Bean
        public Receiver receiver(){
            return new Receiver();
        }
    }

    @Profile("sender")
    @Bean
    public Sender sender() {
        return new Sender();
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        RabbitTemplate  rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());

        return rabbitTemplate;
    }

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);

        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    /*public void produceMessage(String message) {
        rabbitTemplate.convertAndSend();
    }*/

}

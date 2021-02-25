package microservices.user.amqp;

import org.slf4j.Logger;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Sender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /*@Autowired
    private FanoutExchange fanoutExchange;

    @Autowired
    private Logger logger;

    public void sender(String message){
        rabbitTemplate.convertAndSend(fanoutExchange.getName(), "", message);
        logger.info("Sent: " + message);
    }*/

}

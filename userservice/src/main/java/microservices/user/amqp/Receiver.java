package microservices.user.amqp;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class Receiver {

    /*@RabbitListener(queues = "#{fanoutqueue.name}")
    public void receiver(String message) {
        messageReceiver(message);
    }

    public void messageReceiver(String message) {
        System.out.println(message);
    }*/
}

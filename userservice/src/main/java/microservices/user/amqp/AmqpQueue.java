package microservices.user.amqp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import microservices.user.models.User;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class AmqpQueue {

    private List messageList;

    public AmqpQueue(List messageList){
        this.messageList = messageList;
    }

    public void sendtoQueue() {

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            ConnectionFactory connectionFactory = new ConnectionFactory();
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare("userdata", true, false, false, null);

            for (User user : (Iterable<User>) messageList) {
                String json = mapper.writeValueAsString(user);
                channel.basicPublish("", "userdata", MessageProperties.PERSISTENT_TEXT_PLAIN, json.getBytes("UTF-8"));
            }

        } catch (IOException | TimeoutException e) {
            e.getCause();
        }
    }
}

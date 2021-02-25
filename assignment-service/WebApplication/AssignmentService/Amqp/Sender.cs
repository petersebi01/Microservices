using System;
using System.Text;
using Newtonsoft.Json;
using RabbitMQ.Client;

namespace AssignmentService.Amqp
{
    public class Sender
    {
        public void SendMessage(String message)
        {
            var connectionFactory = new ConnectionFactory
            {
                HostName = "localhost",
                Port = 5672,
                UserName = "guest",
                Password = "guest",
            };

            using var connection = connectionFactory.CreateConnection();
            using var channel = connection.CreateModel();
            channel.ExchangeDeclare("exchange", ExchangeType.Topic, false, true);
            channel.QueueDeclare("assignments", true, false, true);
            var json = JsonConvert.SerializeObject(message);
            var body = Encoding.UTF8.GetBytes(json);
            channel.BasicPublish("employees", "assignments", null, body);
        }
    }
}
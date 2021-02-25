const amqp = require('amqplib');

const http = require('http');

const chatDataBase = require('../database/messages');

let connection = null;
let channel = null;
let subscription = null;

//létrehozza a kapcsolatot
exports.createConnection = async () => {
  if (connection == null && channel == null) {
    console.log("Creating connection and channel ")
    connection = await amqp.connect();
    channel = await connection.createChannel();
    //await this.subscribeToTopic();
  }
  console.log(`connection: ${connection}, channel: ${channel}`);
}

// értesítések
exports.sendToQueue = (notification) => {
  http.get('http://localhost:8080/api/users/userdata', (response) => {
    channel.consume('userdata', (message) => {
      let body = JSON.parse(message.content);
      const nameOfQueue = `user-${body.username}`;
      console.log(nameOfQueue);
      channel.sendToQueue(nameOfQueue, Buffer.from(notification));
    }, {noAck: true});
  });
};

// hírfolyam
exports.subscribeToFanout = () => {
  channel.assertQueue('newsQueue');
  channel.bindQueue('newsQueue', 'news');
  channel.consume('newsQueue', (message) => {
    console.log(message);
    const content = JSON.parse(message.content);
    chatDataBase.insertNews(content);
    publishToFanout(message);
    console.log(content);
  }, {noAck: true});
};

// hírfolyam
exports.publishToFanout = (message) => {
  console.log('Sending to fanout')
  const messageToSend = JSON.stringify(message);
  console.log(messageToSend);
  channel.assertExchange('news', 'fanout', { durable: true });
  channel.publish('news', '', Buffer.from(messageToSend));
};

// chat
exports.subscribeToTopic = (id) => {
  console.log('subscribeing to chat');
  const routingKey = `assignment.${id}`;

  console.log(`creating subs, routing key: ${routingKey}, chatroom: chatroom-${id}`);
  channel.assertQueue(`chatroom-${id}`);
  channel.bindQueue(`chatroom-${id}`, 'amq.topic', routingKey);
  channel.consume(`chatroom-${id}`, (message) => {
    const content = JSON.parse(message.content);
    //console.log(`\n${content.clientId} : ${content.text}`);
    chatDataBase.insertMessage(content);
    //chatDataBase.insertMember(content);
    const notification = {
      username: content.username,
      date: content.date,
      to: content.assignmentName,
      message: content.message,
    };
    const json = JSON.stringify({
      notification: notification,
      userID: id,
    });
    this.sendToQueue(json); // minden felhasználónak

    const messageToSend = JSON.stringify(content);
    console.log(routingKey);
    console.log(messageToSend);
    channel.assertExchange('chatroom', 'topic', { durable: false });
    channel.publish('chatroom', routingKey, Buffer.from(messageToSend));

  }, {noAck: true});
};

exports.close = () => {
  connection.close();
};

// chat
/*exports.publishToTopic = (message) => {
  amqp.connect().then((connect) => connect.createChannel().then((channel) => {
    const routingKey = `api.${message.type}`;
    const messageToSend = JSON.stringify(message);
    console.log(routingKey);
    console.log(messageToSend);
    channel.assertExchange('chatroom', 'topic', { durable: false });
    channel.publish('chatroom', routingKey, Buffer.from(messageToSend));
  }));
}};
*/


/* exports.sendToClientQueue = (request, notification) => {
  userDataBase.usersOnService(request.params.id).then((users) => {
    users.forEach((user) => {
      const nameOfQueue = `user-${user.userID}`;
      console.log(nameOfQueue);
      channel.sendToQueue(nameOfQueue, Buffer.from(notification));
    });
  });
}; */
/*exports.subscribeToFanout = () => {
  channel.assertQueue('newsQueue');
  channel.bindQueue('newsQueue', 'news');
  channel.consume('newsQueue', (message) => {
    console.log(JSON.parse(message));
    return JSON.parse(message.content);
  }, { noAck: true });
};

// hírfolyam
exports.publishToFanout = (message) => {
  // const routingKey = `api.${message.type}`;
  const messageToSend = JSON.stringify(message);
  // console.log(routingKey);
  console.log(messageToSend);
  channel.assertExchange('news', 'fanout', { durable: true });
  channel.publish('news', '', Buffer.from(messageToSend));
};*/
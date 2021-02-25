// A felhasználó entitáshoz tartozó CRUD műveletekhez ad meg endpointokat.

const express = require('express');

const router = express.Router();

const chatDataBase = require('../database/messages');

const verifyUser = require('../middleware/login');

const broker = require('../middleware/broker');

// felhasználók chat elözményeinek lekérése
router.post('/chatroom', (request, response) => {
  console.log('hello in chatroom');
  broker.subscribeToTopic(request.body.assignmentId);
  chatDataBase.findChatHistory(request.body).then((results) => {
    console.log(results);
    //chatDataBase.findChatMembers(request.body);
    response.json(results);
  }).catch((error) => {
    console.log(`Woops!!! Something went wrong. ${error.message}`);
    response.status(500).json({ error: { message: 'Something went wrong.' }, reason: error.message });
  });
});

router.get('/chatroom/members', (request, response) => {
  chatDataBase.findChatMembers(request.body).then((results) => {
    const notification = JSON.stringify({
      message: 'valaki belépett a chatre'
    });
    //broker.publishToFanout(message);
    broker.sendToQueue(notification);
    response.json(results);
  }).catch((error) => {
    console.log(`Woops!!! Something went wrong. ${error.message}`);
    response.status(500).json({ error: { message: 'Something went wrong.' }, reason: error.message });
  });
});

// Hírfolyam lekérése
router.get('/news', (request, response) => {
  chatDataBase.findNews().then((results) => {
    if (results) {
      response.json(results);
    } else {
      response.json({ message: 'No news' });
    }
    broker.subscribeToFanout();
  }).catch((error) => {
    console.log(`Woops!!! Something went wrong. ${error.message}`);
    response.status(500).json({ error: { message: 'Something went wrong' }, reason: error.message });
  });
});

// Hír lekérése
router.get('/news/:id', (request, response) => {
  chatDataBase.findNewsById(request.params.id).then((result) => {
    if (result) {
      response.json(result);
    } else {
      response.status(404);
      response.json({ message: 'News does not exists' });
    }
  }).catch((error) => {
    console.log(`Woops!!! Something went wrong. ${error.message}`);
    response.status(500).json({ error: { message: 'Something went wrong' }, reason: error.message });
  });
});

// DELETE news
router.delete('/news/:id', /*verifyUser,*/ (request, response) => {
  chatDataBase.deleteNews(request.params.id).then((result) => {
    if (result.affectedRows > 0) {
      response.sendStatus(204);
      // response.json({ message: 'User deleted' });
      console.log('Delete completed');
    } else {
      response.status(404);
      response.json({ message: 'News does not exists' });
      console.log('News doesnt exists.');
    }
  }).catch((error) => {
    if (error.code === 'ER_ROW_IS_REFERENCED_2') {
      response.status(409);
      response.json({ error: { message: `News with ID: ${request.params.id} was not deleted. Please delete the referenced reservations first.` }, reason: error.sqlMessage });
    } else {
      response.status(500);
      response.json({ error: { message: 'Something went wrong! Delete was not succesfull.' }, reason: error.message });
    }
  });
});


// UPDATE news
router.put('/news/:id', (request, response) => {
  chatDataBase.findNewsById(request.params.id).then((findResult) => {
    if (findResult) {
      chatDataBase.updateNews(request.params.id, request.body).then((result) => {
        const date = new Date().toJSON();
        const message = {
          sender: request.body.username, message: request.body.message, date,
        };
        const notification = JSON.stringify({
          username: request.body.sender,
          date: date,
          to: request.body.assignmentName,
          message: request.body.message,
        });
        //broker.publishToFanout(message);
        broker.sendToQueue(notification);
        response.status(204);
        response.json(result);
      }).catch((error) => {
        if (error.code === 'ER_NO_DEFAULT_FOR_FIELD') {
          response.status(400);
          response.json({ error: { message: 'Field is missing. Updating news was not succesfull.' }, reason: error.sqlMessage });
        } else {
          console.log(`Woops!!! Something went wrong. ${error.message}`);
          response.status(500).json({ error: { message: 'Something went wrong! Updating news was not succesfull.' }, reason: error.message });
        }
      });
    } else {
      chatDataBase.insertNews(request.body).then((result) => {
        response.status(201);
        response.location(`${request.originalUrl}/${result.newsID}`);
        response.json(result);
        const date = new Date().toJSON();
        const message = {
          sender: request.body.sender, message: request.body.message, date,
        };
        const notification = JSON.stringify({
          username: request.body.sender,
          date: date,
          to: request.body.assignmentName,
          message: request.body.message,
        });
        broker.publishToFanout(message);
        broker.sendToQueue(notification);
      }).catch((error) => {
        if (error.code === 'ER_NO_DEFAULT_FOR_FIELD') {
          response.status(400);
          response.json({ error: { message: 'Field is missing. Creating news was not succesfull.' }, reason: error.sqlMessage });
        } else {
          response.status(500);
          response.json({ error: { message: 'Something went wrong! Creating news was not succesfull.' }, reason: error.message });
        }
      });
    }
  });
});

module.exports = router;

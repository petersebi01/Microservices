const express = require('express');
const cors = require('cors');

const broker = require('../middleware/broker');

const router = express.Router();

const messages = require('./messages');

broker.createConnection();

router.use(cors());

router.use('/messages', messages);

module.exports = router;

// MongoDB kapcsolat.
const mysql = require('mysql');

const connection = mysql.createConnection({

  host: 'localhost',
  user: process.env.SECRET_USERNAME || 'chatUser',
  password: process.env.SECRET_PASSWORD || 'userofChat',
  database: 'ChatService',

});

connection.connect();

const dbQuery = (queryString) => new Promise((resolve, reject) => {
  connection.query(queryString, (error, results) => {
    if (error) {
      console.log(`Hiba lépett fel itt: ${error.sql}`);
      reject(error);
    }
    resolve(results);
  });
});

module.exports = dbQuery;

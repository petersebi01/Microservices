const express = require('express');

const app = express();
const bodyParser = require('body-parser');
const morgan = require('morgan');
const routes = require('./api');

const port = process.env.PORT || 8000;

app.use(morgan('tiny'));

app.use(bodyParser.json());

app.use('/api', routes);

app.use((error, req, res, next) => {
  res.status(error.status || 500);
  res.json({
    error: {
      message: error.message,
      request: req.body,
    },
  });
  next();
});

app.listen(port, () => {
  console.log(`Server is listening on port ${port}`);
});

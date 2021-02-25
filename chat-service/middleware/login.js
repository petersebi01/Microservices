const jwt = require('jsonwebtoken');

const secret = 'th1515th3S3cr3tK3y0f4llU53r5';

const verifyUser = (request, response, next) => {
  console.log(request.headers);
  if (request.headers.authorization) {
    const authorization = request.headers.authorization.split(' ')[1];
    console.log(authorization);
    jwt.verify(authorization, secret, (error, decoded) => {
      if (decoded) {
        request.username = decoded.username;
        console.log(decoded.username);
        next();
      } else {
        response.status(401);
        response.json({ error: error.message, message: 'Authorization failed' });
      }
    });
  } else {
    console.log('Please login first');
    response.status(403);
    response.json({ message: 'Please log in first' });
  }
};

module.exports = verifyUser;

// Felhasználó entitásokhoz tartozó lekérdezéseket kezel.

const jsonSql = require('json-sql')({ separatedValues: false });
const connection = require('./connect');

jsonSql.setDialect('mysql');

// Összes entitás lekérése
exports.findChatHistory = (jsonFile) => {
  const sql = jsonSql.build({
    type: 'select',
    table: 'message',
    fields: {
      messageId: { table: 'message' },
      nickname: { table: 'chatmembers' },
      sentAt: { table: 'message' },
      content: { table: 'message' },
    },
    join: [{
      type: 'inner',
      table: 'chatmembers',
      on: { 'message.cmberID': 'chatmembers.cmberID' },
    }],
    condition: {nickname: { $eq: jsonFile.nickname }, assignmentId: { $eq: Number(jsonFile.assignmentId)} },
  });

  return connection(sql.query);
};

exports.findChatMembers = (jsonFile) => {
  const sql = jsonSql.build({
    type: 'select',
    table: 'chatmembers',
    fields: {
      cmberID: { table: 'chatmembers' },
      nickname: { table: 'chatmembers' },
    },
    condition: {assignmentId: { $eq: Number(jsonFile.assignmentId)} },
  });

  return connection(sql.query);
};

// Entitás lekérése ID alapján
exports.findNewsById = (id) => {
  const sql = jsonSql.build({
    type: 'select',
    table: 'news',
    condition: { newsId: { $eq: Number(id) } },
  });

  return connection(sql.query).then((result) => result[0]);
};

exports.findNews = () => {
  const sql = jsonSql.build({
    type: 'select',
    table: 'news',
  });

  return connection(sql.query);
};

// INSERT
exports.insertMessage = (jsonFile) => {
  const sql = jsonSql.build({
    type: 'insert',
    table: 'message',
    values: {
      content: jsonFile.content,
      sentAt: jsonFile.sentAt,
      cmberId: jsonFile.cmberId,
    },
  });

  return connection(sql.query);
};

// INSERT
exports.insertNews = (jsonFile) => {
  const sql = jsonSql.build({
    type: 'insert',
    table: 'news',
    values: jsonFile,
  });

  return connection(sql.query);
};

// DELETE
exports.deleteNews = (id) => {
  const sql = jsonSql.build({
    type: 'remove',
    table: 'news',
    condition: { newsID: { $eq: Number(id) } },
  });

  return connection(sql.query);
};

// UPDATE
exports.updateNews = (id, jsonFile) => {
  const sql = jsonSql.build({
    type: 'update',
    table: 'news',
    modifier: jsonFile,
    condition: { newsId: { $eq: Number(id) } },
  });

  return connection(sql.query);
};

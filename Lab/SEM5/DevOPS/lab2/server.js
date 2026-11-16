const express = require("express");
const os = require("os");

const app = express();
const port = process.env.PORT || 3000;
const greeting = process.env.GREETING || "Hi";

app.get("/", (req, res) => {
  res.send(`${greeting} from container ${os.hostname()}\n`);
});

app.listen(port, () => {
  console.log(`Listening on port ${port}`);
});

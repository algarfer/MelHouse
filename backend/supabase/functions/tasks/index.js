import { serve } from "https://deno.land/std@0.177.1/http/server.ts";
import serviceAccount from "../service-account.json" assert { type: "json" };
import admin from "npm:firebase-admin";

console.log("Initializing Firebase Admin");
if (admin.apps.length === 0) {
  console.log("Firebase Admin not initialized yet");
  admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
  });
} else {
  console.log("Firebase Admin already initialized");
}
console.log("Firebase Admin initialized successfully");

serve(async (req) => {
  console.log("Checking valid request");
  if (req.method !== "POST")
    return new Response("Method not allowed", {
      status: 405,
      headers: { "Content-Type": "application/json" },
    });

  if (req.headers.get("host") !== "functions:9000")
    return new Response("Unauthorized", {
      status: 401,
      headers: { "Content-Type": "application/json" },
    });

  const mode = new URL(req.url).pathname.split("/").pop();
  const payload = await req.json();
  const { title, body } = payload;

  let message = {
    notification: {
      title,
      body,
    },
  };

  console.log("Sending notifications");

  let  response = null
  switch (mode) {
    case "assignment": {
      const { fcm_token: token } = payload;
      message = { ...message, token };
      response = await admin.messaging().send(message);
    }
    case "update": {
      const { fcm_tokens: tokens } = payload;
      message = { ...message, tokens };
      response = await admin.messaging().sendEachForMulticast(message);
    }
  }
  console.log("Notification sent", response);

  if (typeof response !== "string")
    return new Response("Error", {
      status: 500,
      headers: { "Content-Type": "application/json" },
    });

  return new Response("Done", {
    headers: { "Content-Type": "application/json" },
  });
});

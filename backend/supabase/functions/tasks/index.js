import { serve } from "https://deno.land/std@0.177.1/http/server.ts";
import serviceAccount from "../service-account.json" assert { type: "json" };
import admin from "npm:firebase-admin";

if (admin.apps.length === 0) {
  admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
  });
}

serve(async (req) => {
  if (req.method !== "POST") {
    console.error("Method not allowed");
    return new Response("Method not allowed", {
      status: 405,
      headers: { "Content-Type": "application/json" },
    });
  }

  if (req.headers.get("host") !== "functions:9000") {
    console.error("Unauthorized");
    return new Response("Unauthorized", {
      status: 401,
      headers: { "Content-Type": "application/json" },
    });
  }

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

  let response = null;
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

  if (typeof response !== "string") {
    console.error("Error", response);
    return new Response("Error", {
      status: 500,
      headers: { "Content-Type": "application/json" },
    });
  }

  console.log("Notification sent", response);
  return new Response("Done", {
    headers: { "Content-Type": "application/json" },
  });
});

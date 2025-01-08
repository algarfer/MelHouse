import { serve } from "https://deno.land/std@0.177.1/http/server.ts";
import { createClient } from "npm:@supabase/supabase-js";
import serviceAccount from "../service-account.json" assert { type: "json" };
import admin from "npm:firebase-admin";

const supabase = createClient(
  Deno.env.get("SUPABASE_URL") ?? "",
  Deno.env.get("SUPABASE_SERVICE_ROLE_KEY") ?? ""
);

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

  const payload = await req.json();
  const { name, admin_id } = payload.record;
  const { data } = await supabase
    .from("users")
    .select()
    .eq("id", admin_id);
  const { fcm_token, name: username } = data[0];

  if (!fcm_token) {
    console.error("User without FCM token");
    return new Response("No FCM token", {
      status: 400,
      headers: { "Content-Type": "application/json" },
    });
  }

  const message = {
    token: fcm_token,
    notification: {
      title: `Enhorabuena por tu ascenso ${username}`,
      body: `A partir de ahora eres el administrador del piso \"${name}\"`,
    },
  };

  console.log("Sending notification");
  const response = await admin.messaging().send(message);

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

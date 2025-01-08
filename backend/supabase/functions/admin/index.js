import { serve } from "https://deno.land/std@0.177.1/http/server.ts";
import { createClient } from "npm:@supabase/supabase-js";
import serviceAccount from "../service-account.json" assert { type: "json" };
import admin from "npm:firebase-admin";

console.log("Creating supabase client")
const supabase = createClient(
  Deno.env.get("SUPABASE_URL") ?? "",
  Deno.env.get("SUPABASE_SERVICE_ROLE_KEY") ?? ""
);
console.log("Supabase client created successfully")

console.log("Initializing Firebase Admin")
if(admin.apps.length === 0) {
  console.log("Firebase Admin not initialized yet")
  admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
  });
} else {
  console.log("Firebase Admin already initialized")
}
console.log("Firebase Admin initialized successfully")

serve(async (req) => {
  console.log("Checking valid request")
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

  const payload = await req.json();

  const { name, admin_id } = payload.record;
  const { data } = await supabase
    .from("users")
    .select()
    .eq("id", admin_id);
  const { fcm_token, name: username } = data[0];

  if (!fcm_token) {
    console.log("User without FCM token")
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

  console.log("Sending notification")
  const response = await admin.messaging().send(message);
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

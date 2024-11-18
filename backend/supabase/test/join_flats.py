
import os
from dotenv import load_dotenv
from supabase import create_client
from uuid import uuid4

load_dotenv()

s = create_client(
    os.getenv("SUPABASE_URL"),
    os.getenv("SUPABASE_SERVICE_KEY")
)

def join_flat(email, password, flat_id):
    s.auth.sign_in_with_password({
        "email": email,
        "password": password
    })
    try:
        s.rpc("join_flat", {
            "p_code": flat_id
        }).execute()
    except Exception as e:
        print(e)
    finally:
        s.auth.sign_out()

users_without_flat = (
    s
        .table("users")
        .select("*")
        .is_("flat_id", None)
        .execute()
)

flats = (
    s
        .table("flats")
        .select("*")
        .execute()
)

max = min(len(users_without_flat.data), len(flats.data))

for i in range(max):
    n = users_without_flat.data[i]["name"].replace("User ", "").strip()
    join_flat(
        users_without_flat.data[i]["email"],
        f"userPassword{n}",
        flats.data[i]["invitation_code"]
    )
    
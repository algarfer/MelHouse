
import os
from dotenv import load_dotenv
from supabase import create_client
from uuid import uuid4

load_dotenv()

supabase = create_client(
    os.getenv("SUPABASE_URL"),
    os.getenv("SUPABASE_ANON_KEY")
)

def create_flat(n: int) -> None:
    supabase.auth.sign_in_with_password({
        # TODO - Recover users from database with service key
        "email": f"user{n}@email.com",
        "password": f"userPassword{n}"
    })

    try:
        supabase.rpc("create_flat", {
            "p_name": f"Test Flat {n}",
            "p_address": f"C/Testing {n}"
        }).execute()
    except Exception as e:
        print(e)
    finally:
        supabase.auth.sign_out()

for i in range(3):
    create_flat(i)

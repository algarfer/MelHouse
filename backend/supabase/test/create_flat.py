
import os
from dotenv import load_dotenv
from supabase import create_client
from uuid import uuid4

load_dotenv()

supabase = create_client(
    os.getenv("SUPABASE_URL"),
    os.getenv("SUPABASE_ANON_KEY")
)

supabase.auth.sign_in_with_password(
    {
        "email": "foo@bar.com",
        "password": "foo-bar"
    }
)

try:
    supabase.rpc("create_flat", {
        "p_id": str(uuid4()),
        "p_name": "Test flat",
        "p_address": "C/Testing 123"
    }).execute()
finally:
    supabase.auth.sign_out()

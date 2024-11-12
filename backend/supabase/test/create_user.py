
import os
from supabase import create_client
from dotenv import load_dotenv

load_dotenv()

supabase = create_client(
    os.getenv("SUPABASE_URL"),
    os.getenv("SUPABASE_ANON_KEY")
)

supabase.auth.sign_up({
    "email": "foo@bar.com",
    "password": "foo-bar",
    "options": {
        "data": {
            "name": "Foo Bar"
        }
    }
})

supabase.auth.sign_out()


import os
from supabase import create_client
from dotenv import load_dotenv

load_dotenv()

supabase = create_client(
    os.getenv("SUPABASE_URL"),
    os.getenv("SUPABASE_SERVICE_KEY")
)

for i in range(10):
    response = supabase.auth.admin.create_user({
        "email": f"user{i}@email.com",
        "password": f"userPassword{i}",
        "user_metadata": { "name": f"User {i}" },
        "email_confirm": True
    })
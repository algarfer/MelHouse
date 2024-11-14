
import os, multiprocessing
from dotenv import load_dotenv
from supabase import create_client
from uuid import uuid4

load_dotenv()

def create_flat(n: int) -> None:
    supabase = create_client(
        os.getenv("SUPABASE_URL"),
        os.getenv("SUPABASE_ANON_KEY")
    )

    supabase.auth.sign_in_with_password({
        "email": f"user{n}@email.com",
        "password": f"user-{n}-password"
    })

    try:
        supabase.rpc("create_flat", {
            "p_id": str(uuid4()),
            "p_name": f"Test Flat {n}",
            "p_address": f"C/Testing {n}"
        }).execute()
    finally:
        supabase.auth.sign_out()

if __name__ == "__main__":
    tasks = []

    for i in range(5):
        p = multiprocessing.Process(target=create_flat, args=(i,))
        tasks.append(p)
        p.start()

    for task in tasks: task.join()

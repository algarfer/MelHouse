
import os, multiprocessing
from supabase import create_client
from dotenv import load_dotenv

load_dotenv()

def create_user(n: int) -> None:
    supabase = create_client(
        os.getenv("SUPABASE_URL"),
        os.getenv("SUPABASE_ANON_KEY")
    )

    supabase.auth.sign_up({
        "email": f"user{n}@email.com",
        "password": f"user-{n}-password",
        "options": {
            "data": {
                "name": f"User {n}"
            }
        }
    })

    supabase.auth.sign_out()

if __name__ == "__main__":
    tasks = []

    for i in range (10):
        p = multiprocessing.Process(target=create_user, args=(i,))
        tasks.append(p)
        p.start()

    for task in tasks:
        task.join()

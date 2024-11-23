#!/usr/bin/env python3

# -*- coding: utf-8 -*-

import os
from supabase import create_client
from dotenv import load_dotenv
from uuid import uuid4
from random import choice, randrange, sample
from datetime import timedelta, date

load_dotenv()

s = create_client(
    os.getenv("SUPABASE_URL"),
    os.getenv("SUPABASE_SERVICE_KEY")
)

EMAIL_TEMPLATE = "user{0}@email.com"
USER_NAME_TEMPLATE = "User {0}"
PASSWORD_TEMPLATE = "userPassword{0}"
FLAT_NAME_TEMPLATE = "Flat {0}"
STREET_TEMPLATE = "Street {0}"

# Create users
for i in range(20):
    response = s.auth.admin.create_user({
        "email": EMAIL_TEMPLATE.format(i),
        "password": PASSWORD_TEMPLATE.format(i),
        "email_confirm": True,
        "user_metadata": {
            "name": USER_NAME_TEMPLATE.format(i)
        }
    }).user

# Create flats
for i in range(6):
    s.auth.sign_in_with_password({
        "email": EMAIL_TEMPLATE.format(i),
        "password": PASSWORD_TEMPLATE.format(i)
    })

    try:
        s.rpc("create_flat", {
            "p_name": FLAT_NAME_TEMPLATE.format(i),
            "p_address": STREET_TEMPLATE.format(i),
        }).execute()
    except Exception as e:
        print(e)
    finally:
        s.auth.sign_out()

# Join flats
flats = s.table("flats").select("name", "id").execute().data

def update_flat(n_user, flat_id):
    (
        s
            .table("users")
            .update({
                "flat_id": flat_id
            })
            .eq("name", USER_NAME_TEMPLATE.format(n_user))
            .execute()
    )

get_flat_id = lambda x1: next(filter(lambda x2: x2["name"] == FLAT_NAME_TEMPLATE.format(x1), flats))["id"]

update_flat(6, get_flat_id(0))
update_flat(7, get_flat_id(0))
update_flat(8, get_flat_id(0))

update_flat(9, get_flat_id(1))
update_flat(10, get_flat_id(1))
update_flat(11, get_flat_id(1))

update_flat(12, get_flat_id(2))
update_flat(13, get_flat_id(2))

update_flat(14, get_flat_id(3))
update_flat(15, get_flat_id(3))

update_flat(16, get_flat_id(4))

# Create tasks
today = date.today()
delta = timedelta(days=31)
start_date = today - delta
end_date = today + delta

def random_date():
    return start_date + timedelta(
        days=randrange((end_date - start_date).days)
    )

generate_task = lambda name, description, flat_id, date: {"id": str(uuid4()), "name": name, "description": description, "status": choice([0, 1, 2, 3]), "priority": choice([0, 1, 2]), "start_date": str(date - timedelta(days=randrange(1, 30))), "end_date": str(date), "flat_id": flat_id}

for flat in flats:
    tasks = []
    for i in range(1, 11):
        tasks.append(generate_task(f"Task {i}", f"Task {i} description", flat["id"], random_date()))
    s.table("tasks").insert(tasks).execute()

# Assign tasks
for flat in flats:
    tasks = s.table("tasks").select("id").eq("flat_id", flat["id"]).execute().data
    users = s.table("users").select("id").eq("flat_id", flat["id"]).execute().data

    for task in tasks:
        num_users = randrange(1, len(users) + 1)
        assigned_users = sample(users, num_users)
        data = []
        for user in assigned_users:
            data.append({
                "task_id": task["id"],
                "user_id": user["id"]
            })
        s.table("tasks_users").insert(data).execute()


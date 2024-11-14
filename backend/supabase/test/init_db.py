#!/usr/bin/env python3

# -*- coding: utf-8 -*-

import os
from supabase import create_client
from dotenv import load_dotenv
from uuid import uuid4

load_dotenv()

s = create_client(
    os.getenv("SUPABASE_URL"),
    os.getenv("SUPABASE_SERVICE_KEY")
)

# users = []
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

    # users.append(response.user)

# Create flats
for i in range(6):
    s.auth.sign_in_with_password({
        "email": EMAIL_TEMPLATE.format(i),
        "password": PASSWORD_TEMPLATE.format(i)
    })

    try:
        s.rpc("create_flat", {
            "p_id": str(uuid4()),
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

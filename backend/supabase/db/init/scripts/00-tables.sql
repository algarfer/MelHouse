create table public.flats(
    id uuid not null,
    name text not null,
    address text not null,
    floor int4,
    door text,
    stair text,
    invitation_code text not null,
    admin_id uuid not null,
    constraint pk_flats primary key (id),
    constraint uq_flats_invitation_code unique (invitation_code),
    constraint uq_admin_id unique(admin_id)
);

create table public.users(
    id uuid not null,
    name text not null,
    email text not null,
    flat_id uuid,
    constraint pk_users primary key (id),
    constraint uq_users_email unique (email),
    constraint fk_user_id foreign key (id) references auth.users (id),
    constraint fk_users_flats foreign key (flat_id) references public.flats (id)
);

create table public.tasks(
    id uuid not null,
    name text not null,
    description text,
    status int4,
    priority int4,
    start_date date,
    end_date date,
    flat_id uuid not null,
    constraint pk_tasks primary key (id),
    constraint fk_tasks_flats foreign key (flat_id) references public.flats (id),
    constraint ck_tasks_status check (status in (0, 1, 2, 3)),
    constraint ck_tasks_priority check (priority in (0, 1, 2)),
    constraint ck_tasks_date_order check (start_date <= end_date)
);


create table public.tasks_users(
    user_id uuid not null,
    task_id uuid not null,
    constraint pk_tasks_users primary key (user_id, task_id),
    constraint fk_tasks_users_tasks foreign key (task_id) references public.tasks (id),
    constraint fk_tasks_users_users foreign key (user_id) references public.users (id)
);

-- Add circular foreign key constraints
alter table public.flats
add constraint fk_flats_user foreign key (admin_id) references public.users (id);

-- Add permissions for the postgres user
grant select, insert, update, delete, truncate, references, trigger on public.users to postgres;
grant select, insert, update, delete, truncate, references, trigger on public.flats to postgres;
grant select, insert, update, delete, truncate, references, trigger on public.tasks to postgres;
grant select, insert, update, delete, truncate, references, trigger on public.tasks_users to postgres;
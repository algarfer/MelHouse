create table public.flats(
    id uuid not null default uuid_generate_v4(),
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
    id uuid not null default uuid_generate_v4(),
    name text not null,
    email text not null,
    flat_id uuid,
    constraint pk_users primary key (id),
    constraint uq_users_email unique (email),
    constraint fk_user_id foreign key (id) references auth.users (id) on delete cascade,
    constraint fk_users_flats foreign key (flat_id) references public.flats (id) on delete set null
);

create table public.tasks(
    id uuid not null default uuid_generate_v4(),
    name text not null,
    description text,
    status int4,
    priority int4,
    start_date date,
    end_date date,
    flat_id uuid not null,
    constraint pk_tasks primary key (id),
    constraint fk_tasks_flats foreign key (flat_id) references public.flats (id) on delete cascade,
    constraint ck_tasks_status check (status in (0, 1, 2, 3)),
    constraint ck_tasks_priority check (priority in (0, 1, 2)),
    constraint ck_tasks_date_order check (start_date <= end_date)
);


create table public.tasks_users(
    user_id uuid not null,
    task_id uuid not null,
    constraint pk_tasks_users primary key (user_id, task_id),
    constraint fk_tasks_users_tasks foreign key (task_id) references public.tasks (id) on delete cascade,
    constraint fk_tasks_users_users foreign key (user_id) references public.users (id) on delete cascade
);

-- Add circular foreign key constraints
alter table public.flats
add constraint fk_flats_user foreign key (admin_id) references public.users (id);

-- Add permissions for the postgres user
grant select, insert, update, delete, truncate, references, trigger on public.users to postgres;
grant select, insert, update, delete, truncate, references, trigger on public.flats to postgres;
grant select, insert, update, delete, truncate, references, trigger on public.tasks to postgres;
grant select, insert, update, delete, truncate, references, trigger on public.tasks_users to postgres;

create schema private;

grant usage on schema private to service_role;
grant usage on schema private to anon;
grant usage on schema private to authenticated;

alter default privileges for role postgres in schema private grant all on tables to service_role;
alter default privileges for role postgres in schema private grant all on routines to service_role;
alter default privileges for role postgres in schema private grant all on sequences to service_role;
alter default privileges for role postgres in schema private grant all on functions to service_role;

alter default privileges for role postgres in schema private grant select on tables to anon, authenticated;
alter default privileges for role postgres in schema private grant execute on routines to anon, authenticated;
alter default privileges for role postgres in schema private grant select on sequences to anon, authenticated;
alter default privileges for role postgres in schema private grant execute on functions to anon, authenticated;

alter publication supabase_realtime add table flats;
alter publication supabase_realtime add table users;
alter publication supabase_realtime add table tasks;
alter publication supabase_realtime add table tasks_users;

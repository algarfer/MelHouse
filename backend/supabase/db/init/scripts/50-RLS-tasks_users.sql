alter table public.tasks_users enable row level security;

create policy "Allow user to manage asignees of his flat tasks"
on public.tasks_users
as PERMISSIVE
for ALL
to authenticated
using (
    (select flat_id
    from public.tasks
    where id = task_id) =
    (select flat_id
    from public.users
    where id = (select auth.uid()))
)
with check (
    (select flat_id
    from public.tasks
    where id = task_id) =
    (select flat_id
    from public.users
    where id = (select auth.uid()))
);
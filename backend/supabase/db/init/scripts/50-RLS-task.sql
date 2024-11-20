alter table public.tasks enable row level security;

create policy "Allow user to manage tasks of his flat"
on public.tasks
as PERMISSIVE
for ALL
to authenticated
using (
    flat_id = (select flat_id
    from public.users
    where id = (select auth.uid())))
with check (
    flat_id = (select flat_id
    from public.users
    where id = (select auth.uid()))
);

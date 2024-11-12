alter table public.flats enable row level security;

create policy "Enable insert for users if they are the admin of the flat"
on public.flats
as PERMISSIVE
for INSERT
to authenticated
with check (
  (select auth.uid()) = admin_id
);

create policy "Only select the flat the user is a member of"
on public.flats
as PERMISSIVE
for SELECT
to authenticated
using (
    (select flat_id
    from public.users
    where id = auth.uid()) = id
);
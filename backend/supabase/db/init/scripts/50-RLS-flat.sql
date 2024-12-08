alter table public.flats enable row level security;

create policy "Enable admin users if they are the admin of the flat"
on public.flats
as PERMISSIVE
for ALL
to authenticated
using(
  (select auth.uid()) = admin_id
)
with check (
  (select auth.uid()) = admin_id
);

create policy "Enable admin to edit the flat"
on public.flats
as PERMISSIVE
for UPDATE
to authenticated
using(
  (select auth.uid()) = admin_id
)
with check (
  id = (select flat_id
    from public.users
    where id = admin_id)
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
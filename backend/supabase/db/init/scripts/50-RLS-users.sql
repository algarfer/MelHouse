alter table public.users enable row level security;

create policy "Allow users to modify their own profile"
on public.users
as PERMISSIVE
for UPDATE
to authenticated
using (
 (select auth.uid()) = id   
)
with check (
  (select auth.uid()) = id
);

create policy "A user can see their flat mates"
on public.users
as PERMISSIVE
for SELECT
to authenticated
using (
  flat_id = private.get_user_flat_id((select auth.uid()))
);

create policy "An admin can see users without flat"
on public.users
as PERMISSIVE
for SELECT
to authenticated
using (
  (flat_id IS NULL)
  and
  (select public.is_admin())
);

create policy "A user can see their own profile"
on public.users
as PERMISSIVE
for SELECT
to authenticated
using (
  (select auth.uid()) = id
);

create policy "An admin user can kick a user"
on public.users
as PERMISSIVE
for UPDATE
to authenticated
using (
  (flat_id = private.get_user_flat_id((select auth.uid())))
  and
  (select public.is_admin())
  and
  (id <> (select auth.uid()))
)
with check (
  flat_id = private.get_user_flat_id((select auth.uid())) 
  or flat_id is null
);
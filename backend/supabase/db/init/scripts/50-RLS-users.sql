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
  flat_id = get_user_flat_id((select auth.uid()))
);

create policy "A user can see their own profile"
on public.users
as PERMISSIVE
for SELECT
to authenticated
using (
  (select auth.uid()) = id
);
alter table public.flats enable row level security;

create policy "Enable insert for users if they are the admin of the flat"
on public.flats
as PERMISSIVE
for INSERT
to public
with check (
  (select auth.uid()) = admin_id
);
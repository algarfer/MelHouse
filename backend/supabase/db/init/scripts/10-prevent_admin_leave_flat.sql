create or replace function private.prevent_admin_leave_flat()
returns trigger
language plpgsql
security definer set search_path = ''
as $$
declare
  a_id uuid;
  qty int4;
begin
  select admin_id into a_id
  from public.flats
  where id = old.flat_id;

  select count(id) into qty
  from public.users
  where flat_id = old.flat_id;

  if a_id = old.id and new.flat_id is null and qty > 1 then
    raise exception'cannot_leave_empty_flat_admin';
  end if;

  return new;
end;
$$;

create or replace trigger tgr_prevent_admin_leave_flat
before update on public.users
for each row execute procedure private.prevent_admin_leave_flat();
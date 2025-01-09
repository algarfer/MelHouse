create or replace function private.limit_users_flat()
returns trigger
language plpgsql
security definer set search_path = ''
as $$
declare
  qty int4;
begin
  if (TG_OP = 'UPDATE' and new.flat_id = old.flat_id) then
    return new;
  end if;

  if new.flat_id is null then
    return new;
  end if;

  select count(*) into qty
  from public.users
  where flat_id = new.flat_id;

  assert qty + 1 <= 4, 'max_user_flat';

  return new;
end;
$$;

create or replace trigger tgr_limit_users_flat
before insert or update on public.users
for each row execute procedure private.limit_users_flat();

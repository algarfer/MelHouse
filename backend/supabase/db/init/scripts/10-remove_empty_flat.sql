create or replace function private.remove_empty_flat()
returns trigger
language plpgsql
security definer set search_path = ''
as $$
declare
  qty int4;
begin
  select count(id) into qty
  from public.users
  where flat_id = old.flat_id;

    if qty = 0 then
        delete from public.flats
        where id = old.flat_id;
    end if;

  return new;
end;
$$;

create or replace trigger tgr_remove_empty_flat
after update on public.users
for each row execute procedure private.remove_empty_flat();
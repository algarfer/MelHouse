create or replace function private.check_admin_in_flat()
returns trigger
language plpgsql
security definer set search_path = ''
as $$
declare
    f_id uuid;
begin
    select flat_id into f_id
    from public.users
    where id = new.admin_id;

    assert f_id = new.id or f_id is null, 'admin_not_in_flat';

    return new;
end;
$$;

create or replace trigger trg_check_admin_in_flat
before insert or update on public.flats
for each row execute function private.check_admin_in_flat();

create or replace function private.check_user_flat()
returns trigger
language plpgsql
security definer set search_path = ''
as $$
declare
    f_id uuid;
begin
    select flat_id into f_id
    from public.users
    where id = old.id;

    if f_id is not null then
        raise exception 'Cannot delete user: user is associated with a flat';
    end if;
    return old;
end;
$$;

create or replace trigger trg_check_user_flat
before delete on auth.users
for each row execute function private.check_user_flat();

create or replace function private.remove_asigments()
returns trigger
language plpgsql
security definer set search_path = ''
as $$
begin
    if old.flat_id <> new.flat_id or new.flat_id is null then
        delete from public.tasks_users where user_id = old.id;
    end if;
    return new;
end;
$$;

create or replace trigger trg_remove_asigments
after update on public.users
for each row execute procedure private.remove_asigments();
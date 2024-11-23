create or replace function private.check_user_email_consistency()
returns trigger
language plpgsql
security definer set search_path = ''
as $$
begin
    if new.email <> (select email from auth.users where id = new.id) then
        raise exception 'email is not correct';
    end if;
    return new;
end;
$$;

create or replace trigger trg_check_user_email_consistency
before insert or update on public.users
for each row execute function private.check_user_email_consistency();

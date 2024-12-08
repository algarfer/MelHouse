create or replace function public.is_admin()
returns bool
language plpgsql
security definer set search_path = ''
as $$
declare
    f_id uuid;
    a_id uuid;
    u_id uuid;
begin
    u_id := (select auth.uid());

    select flat_id into f_id
    from public.users
    where id = u_id;

    if f_id is null then
        return false;
    end if;

    select admin_id into a_id
    from public.flats
    where id = f_id;

    return a_id = u_id;
end;
$$;

revoke execute on function public.is_admin from public;
revoke execute on function public.is_admin from anon;
revoke execute on function public.is_admin from postgres;
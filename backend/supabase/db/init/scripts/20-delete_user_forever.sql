create or replace function public.delete_user_forever()
returns void
language plpgsql
security definer set search_path = ''
as $$
declare
    u_id uuid;
begin
    u_id := (select auth.uid());

    delete from auth.users
    where id = u_id;
end;
$$;

revoke execute on function public.delete_user_forever from public;
revoke execute on function public.delete_user_forever from anon;
revoke execute on function public.delete_user_forever from postgres;
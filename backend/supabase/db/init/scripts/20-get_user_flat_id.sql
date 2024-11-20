create or replace function public.get_user_flat_id(
    user_id uuid
)
returns uuid
language plpgsql
security definer set search_path = ''
as $$
declare
    flat uuid;
begin
    return (select flat_id from public.users where id = user_id);
end;
$$;

revoke execute on function public.get_user_flat_id from public;
revoke execute on function public.get_user_flat_id from anon;
revoke execute on function public.get_user_flat_id from postgres;
create or replace function public.join_flat(
    p_code text
)
returns void
language plpgsql
security definer set search_path = ''
as $$
declare
    flat uuid;
begin
    select id into flat
    from public.flats
    where invitation_code = p_code;

    update public.users
    set flat_id = flat
    where id = (select auth.uid());
end;
$$;

revoke execute on function public.join_flat from public;
revoke execute on function public.join_flat from anon;
revoke execute on function public.join_flat from postgres;
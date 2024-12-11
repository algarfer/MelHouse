create or replace function public.join_flat(
    p_code text
)
returns public.flats
language plpgsql
security definer set search_path = ''
as $$
declare
    flat public.flats%rowtype;
begin
    select * into flat
    from public.flats
    where invitation_code = p_code;

    assert flat.id is not null, 'flat_not_found';

    update public.users
    set flat_id = flat.id
    where id = (select auth.uid());

    return flat;
end;
$$;

revoke execute on function public.join_flat from public;
revoke execute on function public.join_flat from anon;
revoke execute on function public.join_flat from postgres;
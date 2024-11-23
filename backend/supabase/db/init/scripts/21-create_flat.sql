create or replace function public.create_flat(
    p_name text,
    p_address text,
    p_floor int4 default null,
    p_door text default null,
    p_stair text default null
)
returns public.flats
language plpgsql
security invoker set search_path = ''
as $$
declare
    code text;
    p_id uuid;
    new_flat public.flats%rowtype;
begin
    code := private.generate_flat_code();
    p_id := (select extensions.uuid_generate_v4());

    insert into public.flats (id, name, address, floor, door, stair, invitation_code, admin_id)
    values (p_id, p_name, p_address, p_floor, p_door, p_stair, code, auth.uid());

    update public.users
    set flat_id = p_id
    where id = auth.uid();

    select * into new_flat
    from public.flats
    where id = p_id;

    return new_flat;
end;
$$;

revoke execute on function public.create_flat from public;
revoke execute on function public.create_flat from anon;
revoke execute on function public.create_flat from postgres;
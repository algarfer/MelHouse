create or replace function public.create_flat(id uuid, name text, address text, floor int4 default null, door text default null, stair text default null)
returns void
language plpgsql
security invoker set search_path = ''
as $$
declare
    code text;
begin
    code := public.generate_flat_code();

    insert into public.flats (id, name, address, floor, door, stair, invitation_code, admin_id) VALUES (id, name, address, floor, door, stair, code, auth.uid());
end;
$$;

revoke execute on function public.create_flat from public;
revoke execute on function public.create_flat from anon;
revoke execute on function public.create_flat from postgres;
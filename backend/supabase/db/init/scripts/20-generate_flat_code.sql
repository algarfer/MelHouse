create or replace function private.generate_flat_code()
returns text
language plpgsql
security invoker set search_path = ''
as $$
declare
    numbers text;
    letters text;
    code text;
    exists boolean;
begin
    loop
        numbers := lpad(cast(floor(random() * 1000) as text), 3, '0');

        letters := '';
        for i in 1..3 loop
            letters := letters || chr(floor(random() * 26)::integer + 65);
        end loop;

        code := numbers || '-' || letters;

        select exists (select 1 from public.flats where invitation_code = code) into exists;

        if not exists then
            exit;
        end if;
    end loop;

    return code;
end;
$$;
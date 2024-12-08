create or replace function public.get_user_tasks(
    p_date date default null
)
returns setof public.tasks
language plpgsql
security invoker set search_path = ''
as $$
declare
    v_flat_id uuid;
begin
    select into v_flat_id private.get_user_flat_id((select auth.uid()));

    assert v_flat_id is not null, 'user_no_flat';

    return query
        select *
        from public.tasks
        where flat_id = (v_flat_id)
        and (p_date is null or end_date = p_date)
        and (status = 1 or status = 2)
        and id in (
          select task_id
          from public.tasks_users
          where user_id = (select auth.uid())
        )
    ;

end;
$$;

revoke execute on function public.get_user_tasks from public;
revoke execute on function public.get_user_tasks from anon;
revoke execute on function public.get_user_tasks from postgres;
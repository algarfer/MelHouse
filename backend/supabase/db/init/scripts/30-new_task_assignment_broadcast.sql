create or replace function private.new_task_assignment_broadcast()
returns trigger
language plpgsql
security definer set search_path = ''
as $$
declare
  actor text;
  token text;
  u_id uuid;
begin
  u_id := (select auth.uid());

  if u_id is null or u_id = new.user_id then
    return new;
  end if;

  select name into actor
  from public.users
  where id = u_id;
  
  select fcm_token into token
  from public.users
  where id = new.user_id;

  if token is null then
    return new;
  end if;
  
  perform (net.http_post(
    'http://functions:9000/tasks/assignment',
    jsonb_build_object(
      'title', 'Nueva tarea asignada',
      'body', actor || ' te ha asignado una tarea.',
      'fcm_token', token
    )
  ));

  return new;
end;
$$;

create or replace trigger new_task_assignment_broadcast
  after insert or update on public.tasks_users
  for each row execute procedure private.new_task_assignment_broadcast();

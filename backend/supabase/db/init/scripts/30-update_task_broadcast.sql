create or replace function private.update_task_broadcast()
returns trigger
language plpgsql
security definer set search_path = ''
as $$
declare
  title text;
  body text;
  actor text;
  u_id uuid;
  tokens text[];
begin
  u_id := (select auth.uid());

  if u_id is null then
    return new;
  end if;

  select name into actor
  from public.users
  where id = u_id;

  select array_agg(fcm_token) into tokens
  from public.users
  where id in (
    select user_id
    from public.tasks_users
    where task_id = new.id
  )
  and id <> u_id
  and fcm_token is not null;

  if tokens is null or (select array_length(tokens, 1)) = 0 then
    return new;
  end if;

  if new.status = 0 then
    title := 'Tarea terminada';
    body := actor || ' ha terminado la tarea ' || new.name;
  else
    title := 'Tarea actualizada';
    body := actor || ' ha actualizado la tarea ' || new.name;
  end if;

  perform (net.http_post(
    'http://functions:9000/tasks/update',
    jsonb_build_object(
      'title', title,
      'body', body,
      'fcm_tokens', tokens
    )
  ));

  return new;
end;
$$;

create or replace trigger update_task_broadcast
  after update on public.tasks
  for each row execute procedure private.update_task_broadcast();

create or replace trigger admin_promotion_broadcast after update
on public.flats for each row
execute function supabase_functions.http_request(
  'http://host.docker.internal:8000/functions/v1/admin',
  'POST',
  '{}',
  '{}',
  '1000'
);

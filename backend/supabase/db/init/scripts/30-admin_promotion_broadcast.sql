create or replace trigger admin_promotion_broadcast after update
on public.flats for each row
execute function supabase_functions.http_request(
  'http://functions:9000/admin',
  'POST'
);

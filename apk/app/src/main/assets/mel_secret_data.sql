INSERT INTO users (id, name, email) VALUES ('2c023e9e-fbfc-4df4-a417-8ad6dddd6ac4', 'Juan Ramón', 'jrpp@example.com');
INSERT INTO users (id, name, email) VALUES ('67fa41e2-85ee-4407-880c-589ef46f576a', 'Pepe Mel', 'pepemel@example.com');
INSERT INTO users (id, name, email, flat_id) VALUES ('eb06c7e1-8674-4280-8413-6883cc4c5776', 'Jose', 'jose@example.com', '986255a0-57e8-4d1f-b16e-eb1efdd17407');
INSERT INTO users (id, name, email) VALUES ('a170ceee-f292-4265-b980-ecd154f2783b', 'Ana', 'ana@example.com');
INSERT INTO users (id, name, email, flat_id) VALUES ('bf66312e-5c9d-425a-9e6e-e8fb76a03d11', 'Claudia', 'claudia@example.com', '59f35c62-c24b-4991-984c-08b064abd34f');
INSERT INTO users (id, name, email, flat_id) VALUES ('226235dc-bf9a-400e-9e67-d559012b1f2b', 'Alberto', 'alberto@example.com', '986255a0-57e8-4d1f-b16e-eb1efdd17407');
INSERT INTO users (id, name, email, flat_id) VALUES ('72aff8fc-45ac-4359-8832-2d8e1ac46d38', 'Marco', 'marco@example.com', 'c06a7eeb-51d0-4b14-811a-b48048f94125');
INSERT INTO users (id, name, email, flat_id) VALUES ('d0ba2e96-9725-4c39-b0cd-09d863665355', 'Sandra', 'sandra@example.com', 'c06a7eeb-51d0-4b14-811a-b48048f94125');
INSERT INTO users (id, name, email, flat_id) VALUES ('a61ab5ee-6812-45a2-9a42-6cef87a912f7', 'Cati', 'cati@example.com', '59f35c62-c24b-4991-984c-08b064abd34f');
INSERT INTO users (id, name, email, flat_id) VALUES ('b8b19dbb-2cc8-4deb-9078-c99f3ec1e480', 'Jorge', 'jorge@example.com', '986255a0-57e8-4d1f-b16e-eb1efdd17407');

INSERT INTO flats (id, name, address, floor, door, stair, invitation_code, admin_id) VALUES ('c06a7eeb-51d0-4b14-811a-b48048f94125', 'El habitaculo', 'Calle de la piruleta', 1, 'A', 'A', 'ABC-123', '72aff8fc-45ac-4359-8832-2d8e1ac46d38');
INSERT INTO flats (id, name, address, floor, door, invitation_code, admin_id) VALUES ('59f35c62-c24b-4991-984c-08b064abd34f', 'Habitacion del panico', 'Calle del terror', 2, 'B', 'XYZ-987', 'bf66312e-5c9d-425a-9e6e-e8fb76a03d11');
INSERT INTO flats (id, name, address, invitation_code, admin_id) VALUES ('986255a0-57e8-4d1f-b16e-eb1efdd17407', 'Narco Piso', 'Smoking street', 'CDE-456', 'eb06c7e1-8674-4280-8413-6883cc4c5776');

INSERT INTO tasks (id, name, description, status, priority, start_date, end_date, flat_id) VALUES ('0f1b6714-1810-400b-9b96-205d8e8825fe', 'Tarea 1', 'Descripcion de la tarea 1', 1, 2, '2024-09-21', '2024-09-22', 'c06a7eeb-51d0-4b14-811a-b48048f94125');
INSERT INTO tasks (id, name, description, status, priority, start_date, end_date, flat_id) VALUES ('65cb899a-60a8-4832-b31d-313c69e736a5', 'Tarea 2', 'Descripcion de la tarea 2', 2, 0, '2024-11-01', '2024-11-20', '59f35c62-c24b-4991-984c-08b064abd34f');
INSERT INTO tasks (id, name, description, status, priority, start_date, end_date, flat_id) VALUES ('65cb899a-60a8-4832-b31d-313c69e736a6', 'Tarea 2.1', 'Descripcion de la tarea 2.1', 0, 1, '2024-11-01', '2024-11-20', '59f35c62-c24b-4991-984c-08b064abd34f');
INSERT INTO tasks (id, name, description, status, priority, start_date, end_date, flat_id) VALUES ('65cb899a-60a8-4832-b31d-313c69e736a7', 'Tarea 2,2', 'Descripcion de la tarea 2.2', 1, 2, '2024-11-03', '2024-11-04', '59f35c62-c24b-4991-984c-08b064abd34f');
INSERT INTO tasks (id, name, flat_id) VALUES ('ecc15944-f59a-419a-be90-673cadfbaa7c', 'Tarea 3', '59f35c62-c24b-4991-984c-08b064abd34f');
INSERT INTO tasks (id, name, description, status, priority, start_date, end_date, flat_id) VALUES ('218dc2db-c932-413a-a24e-8c71a0d62679', 'Tarea 4', 'Descripcion de la tarea 4', 0, 1, '2024-10-08', '2024-10-30', 'c06a7eeb-51d0-4b14-811a-b48048f94125');
INSERT INTO tasks (id, name, description, status, priority, end_date, flat_id) VALUES ('b502efec-0185-4ad4-be36-6192e65143a6', 'Tarea 5', 'Descripcion de la tarea 5', 3, 0, '2024-10-20', 'b8b19dbb-2cc8-4deb-9078-c99f3ec1e480');
INSERT INTO tasks (id, name, status, priority, end_date, flat_id) VALUES ('b502efec-0185-4ad4-be36-6192e65143a7', 'Tarea 6', 3, 0, '2024-10-20', 'b8b19dbb-2cc8-4deb-9078-c99f3ec1e480');

INSERT INTO tasks_users (user_id, task_id) VALUES ('bf66312e-5c9d-425a-9e6e-e8fb76a03d11', '65cb899a-60a8-4832-b31d-313c69e736a5');
INSERT INTO tasks_users (user_id, task_id) VALUES ('a61ab5ee-6812-45a2-9a42-6cef87a912f7', '65cb899a-60a8-4832-b31d-313c69e736a5');
INSERT INTO tasks_users (user_id, task_id) VALUES ('a61ab5ee-6812-45a2-9a42-6cef87a912f7', '65cb899a-60a8-4832-b31d-313c69e736a6');
INSERT INTO tasks_users (user_id, task_id) VALUES ('bf66312e-5c9d-425a-9e6e-e8fb76a03d11', '65cb899a-60a8-4832-b31d-313c69e736a7');

CREATE TABLE users (
    id TEXT NOT NULL,
    name TEXT NOT NULL,
    email TEXT NOT NULL,
    flat_id TEXT,
    CONSTRAINT "PK_USERS" PRIMARY KEY (id),
    CONSTRAINT "FK_USERS_FLATS" FOREIGN KEY (flat_id) REFERENCES flats (id)
);

CREATE TABLE tasks (
    id TEXT NOT NULL,
    name TEXT NOT NULL,
    description TEXT,
    status INTEGER,
    priority INTEGER,
    start_date DATE,
    end_date DATE,
    flat_id TEXT NOT NULL,
    CONSTRAINT "PK_TASKS" PRIMARY KEY (id),
    CONSTRAINT "FK_TASKS_FLATS" FOREIGN KEY (flat_id) REFERENCES flats (id),
    CONSTRAINT "CK_TASKS_STATUS" CHECK (status IN (0, 1, 2, 3)),
    CONSTRAINT "CK_TASKS_PRIORITY" CHECK (priority IN (0, 1, 2)),
    CONSTRAINT "CK_TASKS_DATE_ORDER" CHECK (start_date <= end_date)
);

CREATE TABLE flats (
    id TEXT NOT NULL,
    name TEXT NOT NULL,
    address TEXT NOT NULL,
    floor INTEGER,
    door TEXT,
    stair TEXT,
    invitation_code TEXT NOT NULL,
    admin_id TEXT NOT NULL,
    CONSTRAINT "PK_FLATS" PRIMARY KEY (id),
    CONSTRAINT "FK_FLATS_USER" FOREIGN KEY (admin_id) REFERENCES users (id),
    CONSTRAINT "UQ_FLATS_INVITATION_CODE" UNIQUE (invitation_code)
);

CREATE TABLE tasks_users (
    user_id TEXT NOT NULL,
    task_id TEXT NOT NULL,
    CONSTRAINT "PK_TASKS_USERS" PRIMARY KEY (user_id, task_id),
    CONSTRAINT "FK_TASKS_USERS_TASKS" FOREIGN KEY (task_id) REFERENCES tasks (id),
    CONSTRAINT "FK_TASKS_USERS_USERS" FOREIGN KEY (user_id) REFERENCES users (id)
);

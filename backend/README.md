<h1 align="center">MelHouse Backend</h1>

<div align="center">
    <img src="https://img.shields.io/badge/penpot-%23000000.svg?style=for-the-badge&logo=penpot&logoColor=white" alt="Penpot" />
    <img src="https://img.shields.io/badge/Supabase-3ECF8E?style=for-the-badge&logo=supabase&logoColor=white" alt="Supabase" />
</div>

## Overview
This folder contains the backend infrastructure for the MelHouse application.

It includes the following services:

- **Penpot**: An open-source design and prototyping tool.
- **Supabase**: A back-end service providing authentication, database, and real-time APIs.

The provided `Makefile` allows you to manage these services with simple commands for initialization, startup, shutdown, and restarts.

## Setup

Before starting, ensure you have the necessary dependencies installed:

- [Make](https://www.gnu.org/software/make/)
- [Docker](https://docs.docker.com/engine/install/)
- [Docker compose](https://docs.docker.com/compose/install/)

You can use the following commands to initialize the required directories and services.

### Initialize Directories
This command sets up necessary directories for all services:

```bash
make init_dirs
```

### Initialize Environment Files
This command create the `.env.service` files based on the `.env.service.example` files.

```bash
make generate_envs
```

After the environment files are generated, add the required data (commented fields with _!MISSING_ value) so the services can work as expected.

## Managing Services
You can manage all the services at once or control individual services. Below are the commands to handle each scenario.

### Start All Services
```bash
make start_all
```

### Stop All Services
```bash
make stop_all
```

### Restart All Services
```bash
make restart_all
```

## Penpot
Penpot is an open-source tool used for designing wireframes and prototypes. It provides a collaborative design platform for working on vector graphics and interactive UX flows.

In this project, all wireframes and design prototypes are managed using Penpot.

You can control the Penpot service using the following commands:

### Generate Environment File
```bash
make .env.penpot
```

After creating the environment file, overwrite at least these fields:
- `PENPOT_PUBLIC_URI`
- `PENPOT_BACKEND_SECRET_KEY`
- `PENPOT_POSTGRES_PASSWORD`

### Start Penpot
```bash
make start_penpot
```

### Stop Penpot
```bash
make stop_penpot
```

### Restart Penpot
```bash
make restart_penpot
```

## Supabase
Supabase is an open-source backend-as-a-service platform that provides authentication, real-time databases, and APIs. It is used in this project to manage user data, application state, and serverless functions.

You can control the Supabase service using the following commands:

### Generate Environment File
```bash
make .env.supabase
```

After creating the environment file, overwrite at least these fields:
- `POSTGRES_PASSWORD`
- `JWT_SECRET`
- `ANON_KEY`
- `SERVICE_ROLE_KEY`
- `DASHBOARD_USERNAME`
- `DASHBOARD_PASSWORD`
- `SITE_URL`
- `API_EXTERNAL_URL`
- `SUPABASE_PUBLIC_URL`
- `LOGFLARE_LOGGER_BACKEND_API_KEY`
- `LOGFLARE_API_KEY`
- `GOOGLE_PROJECT_ID`
- `GOOGLE_PROJECT_NUMBER`

### Start Supabase
```bash
make start_supabase
```

### Stop Supabase
```bash
make stop_supabase
```

### Restart Supabase
```bash
make restart_supabase
```

---

> [!NOTE]
> - Ensure that you have all required dependencies installed before starting the services.
> - For detailed information on configuring each service, refer to their respective documentation:
>   - [Penpot Documentation](https://help.penpot.app/)
>   - [Supabase Documentation](https://supabase.com/docs)
<h1 align="center">Backend</h1>

<div align="center">
    <img src="https://img.shields.io/badge/nginx-%23009639.svg?style=for-the-badge&logo=nginx&logoColor=white" alt="Nginx" />
    <img src="https://img.shields.io/badge/penpot-%23000000.svg?style=for-the-badge&logo=penpot&logoColor=white" alt="Penpot" />
    <img src="https://img.shields.io/badge/Supabase-3ECF8E?style=for-the-badge&logo=supabase&logoColor=white" alt="Supabase" />
</div>

## Overview
This folder contains the backend infrastructure for the MelHouse application.

It includes the following services:

- **Nginx**: A high-performance reverse proxy server.
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

## Reverse Proxy (Nginx)
Nginx is used as a reverse proxy to handle traffic and direct it to the appropriate services. You can control the reverse proxy using the following commands:

### Start Nginx Reverse Proxy
```bash
make start_proxy
```

### Stop Nginx Reverse Proxy
```bash
make stop_proxy
```

### Restart Nginx Reverse Proxy
```bash
make restart_proxy
```

## Penpot
Penpot is an open-source tool used for designing wireframes and prototypes. It provides a collaborative design platform for working on vector graphics and interactive UX flows.

In this project, all wireframes and design prototypes are managed using Penpot.

You can control the Penpot service using the following commands:

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

> [!NOTE]
> - Ensure that you have all required dependencies installed before starting the services.
> - For detailed information on configuring each service, refer to their respective documentation:
>   - [Nginx Documentation](https://docs.nginx.com/)
>   - [Penpot Documentation](https://help.penpot.app/)
>   - [Supabase Documentation](https://supabase.com/docs)
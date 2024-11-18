start_penpot: .env.penpot
	@echo "Starting penpot server..."
	docker compose --env-file .env.penpot -f docker-compose.penpot.yml up -d

stop_penpot:
	@echo "Stopping penpot server..."
	docker compose -f docker-compose.penpot.yml down

restart_penpot: stop_penpot start_penpot

start_supabase: .env.supabase
	@echo "Starting supabase server..."
	docker compose --env-file .env.supabase -f docker-compose.supabase.yml up -d

stop_supabase:
	@echo "Stopping supabase server..."
	docker compose -f docker-compose.supabase.yml down

restart_supabase: stop_supabase start_supabase

start_all: start_penpot start_supabase

stop_all: stop_supabase stop_penpot

restart_all: restart_penpot restart_supabase

.env.penpot: .env.penpot.example
	@echo "Creating penpot env file..."
	cp .env.penpot.example .env.penpot

.env.supabase: .env.supabase.example
	@echo "Creating supabase env file..."
	cp .env.supabase.example .env.supabase

generate_envs: .env.penpot .env.supabase

init_dirs:
	@echo "Creating directories..."
	mkdir -p ~/docker/penpot
	mkdir -p ~/docker/penpot/assets
	mkdir -p ~/docker/penpot/postgres
	sudo chown -R 1001:1001 ~/docker/penpot/assets
	sudo chown -R 1001:1001 ~/docker/penpot/postgres
	sudo chmod -R +755 ~/docker/penpot/assets
	sudo chmod -R +755 ~/docker/penpot/postgres
start_penpot:
	@echo "Starting penpot server..."
	docker compose -f docker-compose.penpot.yml up -d

stop_penpot:
	@echo "Stopping penpot server..."
	docker compose -f docker-compose.penpot.yml down

restart_penpot: stop_penpot start_penpot

start_all: start_penpot

stop_all: stop_penpot

restart_all: restart_penpot

init_dirs:
	@echo "Creating directories..."
	mkdir -p ~/docker/penpot
	mkdir -p ~/docker/penpot/assets
	mkdir -p ~/docker/penpot/postgres
	sudo chown -R 1001:1001 ~/docker/penpot/assets
	sudo chown -R 1001:1001 ~/docker/penpot/postgres
	sudo chmod -R +755 ~/docker/penpot/assets
	sudo chmod -R +755 ~/docker/penpot/postgres
# ──────────────────────────────────────────────────────────
# La Poste Modular — Developer Makefile
# ──────────────────────────────────────────────────────────
.PHONY: help dev down build clean logs test db-reset prod-up prod-down

help: ## Show this help
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | \
		awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-15s\033[0m %s\n", $$1, $$2}'

dev: ## Start dev stack (DB + pgAdmin + Mailpit + Redis + App)
	docker compose up -d --build

dev-infra: ## Start infra only (DB + pgAdmin + Mailpit + Redis + Keycloak) — run app on host
	docker compose up -d postgres pgadmin mailpit redis keycloak

down: ## Stop all containers
	docker compose down

build: ## Build app with Maven (skip tests)
	./mvnw package -DskipTests -q

clean: ## Maven clean
	./mvnw clean -q

logs: ## Tail app logs
	docker compose logs -f app

db-reset: ## Destroy DB volume and restart (full reset)
	docker compose down -v
	docker compose up -d postgres
	@echo "⏳ Waiting for DB..."
	@sleep 5
	docker compose up -d

test: ## Run tests
	./mvnw test

compile: ## Compile only (fast feedback)
	./mvnw compile -q

prod-up: ## Start production stack
	docker compose -f compose.prod.yaml --env-file .env.prod up -d --build

prod-down: ## Stop production stack
	docker compose -f compose.prod.yaml --env-file .env.prod down


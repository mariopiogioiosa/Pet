# Cross-platform Maven Wrapper
ifeq ($(OS),Windows_NT)
	MVN := mvnw.cmd
	SHELL := powershell.exe
	.SHELLFLAGS := -NoProfile -Command
else
	MVN := ./mvnw
endif

.DEFAULT_GOAL := help

.PHONY: help
help: ## Show this help
	@echo "Local development targets:"
	@echo "  make setup       - Ensure wrapper exec (unix) and prefetch deps"
	@echo "  make clean       - Clean build artifacts"
	@echo "  make build       - Compile and package (skip tests)"
	@echo "  make test        - Run tests"
	@echo "  make verify      - Full verify (tests + checks)"
	@echo "  make run         - Run Spring Boot app"
	@echo "  make fmt         - Apply code formatting (Spotless)"
	@echo "  make fmt-check   - Check formatting only"
	@echo "  make lint        - Run Checkstyle (if configured)"
	@echo "  make ci          - Formatting check + lint + tests (local CI)"
	@echo "  make clean-all   - Clean target and some wrapper caches"

.PHONY: setup
setup: ## Make wrapper executable (unix) and download dependencies
ifeq ($(OS),Windows_NT)
	$(MVN) -B -q -DskipTests dependency:go-offline
else
	chmod +x mvnw || true
	$(MVN) -B -q -DskipTests dependency:go-offline
endif

.PHONY: clean
clean: ## Clean build outputs
	$(MVN) -q clean

.PHONY: build
build: ## Compile and package (skip tests for speed)
	$(MVN) -q -DskipTests package

.PHONY: test
test: ## Run unit/integration tests
	$(MVN) -q test

.PHONY: verify
verify: ## Run full verification (tests + plugin checks)
	$(MVN) -q verify

.PHONY: run
run: ## Run Spring Boot app locally
	$(MVN) spring-boot:run

.PHONY: fmt
fmt: ## Auto-format code with Spotless (if configured)
	$(MVN) -q spotless:apply

.PHONY: fmt-check
fmt-check: ## Check formatting only
	$(MVN) -q spotless:check

#.PHONY: lint
#lint: ## Run Checkstyle (if configured)
#	$(MVN) -q checkstyle:check

.PHONY: ci
ci: ## Run formatting check, lint, and tests (simulate CI locally)
	$(MVN) -q spotless:check
	#$(MVN) -q checkstyle:check
	$(MVN) -q test

.PHONY: clean-all
clean-all: clean ## Clean target and attempt to free wrapper caches
	@echo "Removing cached wrapper zips (if any)..."
	@if [ -d ".mvn/wrapper" ]; then rm -f .mvn/wrapper/*.zip 2>/dev/null || true; fi
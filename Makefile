export SPRING_PROFILES_ACTIVE=local
export AWS_REGION=eu-west-1
export AWS_ACCOUNT_ID=000000000000
export LOCALSTACK_ENDPOINT=http://127.0.0.1:4566

start-localstack-interactive:
	@echo 'starting localstack'
	docker-compose --project-name library_service --file ./local/docker-compose.yaml up

start-localstack:
	@echo 'starting localstack'
	docker-compose --project-name library_service --file ./local/docker-compose.yaml up -d

stop-localstack:
	@echo 'stopping localstack'
	docker-compose --project-name library_service --file ./local/docker-compose.yaml down --volumes

run-local: format
	./mvnw spring-boot:run

clean-build:
	./mvnw clean
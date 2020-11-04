build:
	./mvnw clean install -DskipTests

test:
	./mvnw clean install

build-image:
	./mvnw clean install -DskipTests
	docker build -t sell-easy .
	docker tag sell-easy lucascoelhocs/sell-easy
	docker push lucascoelhocs/sell-easy

build:
	@mvn -q compile
run: build
	@mvn mvn -q exec:java
javafx:
	@mvn clean javafx:run

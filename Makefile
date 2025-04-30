build:
	@mvn -q compile
run: build
	@mvn clean javafx:run
	# @mvn mvn -q exec:java
javafx:
	@mvn clean javafx:run

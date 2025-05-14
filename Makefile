# groupId="apr"
groupId="apr.reflection"
mainClass="App"

build:
	@mvn -q compile
run: build
	@mvn -q exec:java -Dexec.mainClass="$(groupId).$(mainClass)"
javafx:
	@mvn clean javafx:run
clean:
	@mvn clean

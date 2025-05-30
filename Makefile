groupId="apr"
mainClass="App"
args="data/map.osm"

build:
	@mvn -q compile
run: build
	@mvn -q exec:java -Dexec.mainClass="$(groupId).$(mainClass)" -Dexec.args="$(args)"
javafx:
	@mvn clean javafx:run
clean:
	@mvn clean

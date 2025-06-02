groupId="apr"

# DEFAULT
# mainClass="App"

# CONCURRENCY
# mainClass="concurrency.opgave_3.Main"
mainClass="concurrency.opgave_4.Main"

# BACKTRACKING
mainClass="backtracking.Main"

build:
	@mvn -q compile
run: build
	@mvn -q exec:java -Dexec.mainClass="$(groupId).$(mainClass)" -Dexec.args="$(args)"
javafx:
	@mvn clean compile javafx:run
clean:
	@mvn clean

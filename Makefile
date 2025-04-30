build:
	mvn -q compile
	
run:
	mvn -q compile && mvn -q exec:exec

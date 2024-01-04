build:
	@mvn package
	@echo "Build complete"

clean:
	@mvn clean
	@echo "Clean complete"

run:
	@mvn javafx:run
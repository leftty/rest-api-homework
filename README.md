# rest-api-homework

Prerequisites:
1. Java 11 JDK
2. Maven 3.5

Note: The locations for the java and maven executables should be added to the PATH evironment variable

For the required dependencies please check the pom.xml file

For executing the ui tests and generating the report run following command in CLI:
mvn clean verify

The previous command will generate the test report file called index.html in target/site/serenity/ in the root folder of the project

For running test in docker container we use the Dockerfile in the root folder
The commands to be executed are as follows:
1. Build the image from Dockerfile: docker build -t rest-api-homework
2. Start a container using the image build in step 1: docker run --name=rest-api-homework -it rest-api-homework /bin/bash
3. Go to the project directory: cd rest-api-homework
4. Run maven command to execute tests once in the project root folder: mvn clean verify
5. Press Ctrl+P and then Ctrl+Q to quit container without stopping it, or run `exit` command to quit the container and stop it at the same time
6. Copy the target directory containing the test report from the container to the a folder on the local machine: docker cp rest-api:/home/remoteuser/serenity-project/rest-api-homework/target /Users/<user>/Desktop/serenity-report-rest-api/
7. Go in the location where the target folder from the container as copied and open the index.html report file in target/site/serenity
8. Stop the container if it is not already stopped: docker stop rest-api-homework
9. Clean up the containers: docker container prune
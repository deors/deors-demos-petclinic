# deors-demos-petclinic

The 'classic' Spring Pet Clinic application, updated to work with Tomcat 7+ and Spring 3,
with exemplar lifecycle automation configuration.

This fork include:

- Packaging as a Docker image.
- Pipeline as code with Jenkins.
- Maven profiles and configuration to provision local test environments with Cargo and
multiple target containers: Tomcat, WildFly and Jetty.
- Maven profiles and configuration for deployment to OpenShift and Heroku.
- Docker image lifecycle with Spotify Maven plug-in.
- Surefire configured to gather test coverage per test with SonarQube and JaCoCo.
- Mutation tests with Pitest.
- Integration tests with Selenium, which can be executed wither manually or via Failsafe.
A Maven profile is also provided to show how these tests can be binded into the Maven
lifecycle during verify.
- Integration test coverage with JaCoCo.
- Load tests with JMeter. A Maven profile is also provided to show how these tests can be
binded into the Maven lifecycle during verify.
- Dependency vulnerability scan with OWASP Dependency Check.
- Quality analysis with SonarQube, including gathering results from the other tools.

## Set up in Jenkins

The continuous integration pipeline requires that a credential with id `deors-docker-hub`
is configured in Jenkins. The `deors` prefix in the credential id refers to the `deors`
org namespace which is targeted to push Docker images to Docker Hub.

## Useful scripts

The folder `src/scripts` contains several useful scripts to build and test the application
locally:

- `jacocodump-test-container`: Connects to an already running application and dumps code
coverage information being gathered by JaCoCo.
- `mvn-package`: Compiles, run unit tests and package the application.
- `mvn-verify-cargo`: Runs the application using Cargo and and embedded Tomcat runtime,
and runs Selenium integration tests and JMeter performance tests.
- `mvn-verify-heroku`: Runs Selenium integration tests and JMeter performance tests on the
application deployed to Heroku.
- `mvn-verify-heroku`: Runs Selenium integration tests and JMeter performance tests on the
application deployed to Heroku.
- `mvn-verify-local`: Runs Selenium integration tests and JMeter performance tests on the
application running locally (e.g. from IDE, from CLI with Cargo, as a Docker container,
manually by any other means)
- `mvn-verify-openshift`: Runs Selenium integration tests and JMeter performance tests on the
application deployed to OpenShift Online.
- `run-test-container`: Runs the application as a Docker container.
- `ssh-test-container`: Connects to an already running application container via SSH.
- `stop-test-container`: Stops (and removes) an already running application container.

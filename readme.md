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

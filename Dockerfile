FROM tomcat:9.0-jdk25-temurin
COPY target/dependency/jacocoagent.jar /usr/local/tomcat/jacocoagent.jar
COPY target/petclinic.war /usr/local/tomcat/webapps/petclinic.war

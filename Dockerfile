FROM tomcat:8.5.50-jdk8-openjdk
COPY target/dependency/jacocoagent.jar /usr/local/tomcat/jacocoagent.jar
COPY target/petclinic.war /usr/local/tomcat/webapps/petclinic.war

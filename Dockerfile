FROM tomcat:8.5.34-jre8
COPY target/dependency/jacocoagent.jar /usr/local/tomcat/jacocoagent.jar
COPY target/petclinic.war /usr/local/tomcat/webapps/petclinic.war

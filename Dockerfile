FROM tomcat:8.5.32-jre8-alpine
COPY target/petclinic.war /usr/local/tomcat/webapps/petclinic.war

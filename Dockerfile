FROM tomcat:8.5.34-jre8-alpine
COPY target/petclinic.war /usr/local/tomcat/webapps/petclinic.war

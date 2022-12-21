FROM tomcat:9-jdk17-corretto


COPY ./target/trading-platform.war /usr/local/tomcat/webapps/

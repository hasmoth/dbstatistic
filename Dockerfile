# Dockerfile

FROM openjdk:8
MAINTAINER  Author atoms.h <mr.plow@posteo.de>
COPY . /usr/src/dbcrawler
WORKDIR /usr/src/dbcrawler
RUN javac -classpath ./lib/jsoup-1.8.1.jar:./lib/sqlite-jdbc-3.21.0.jar:.lib/mysql-connector-java-8.0.18.jar:./resources/* ./src/db/*.java
#CMD ["java", "-cp", "./bin:./lib/jsoup-1.8.1.jar:./lib/sqlite-jdbc-3.21.0.jar", "db.DbCrawler"]
CMD java -cp ./bin:./lib/jsoup-1.8.1.jar:./lib/sqlite-jdbc-3.21.0.jar:./lib/mysql-connector-java-8.0.18.jar db.DbCrawler

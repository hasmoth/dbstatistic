# Dockerfile

FROM openjdk:8
MAINTAINER  Author atoms.h <mr.plow@posteo.de>
COPY . /usr/src/dbcrawler
COPY ./resources /usr/src/dbcrawler/bin
WORKDIR /usr/src/dbcrawler
ENV TZ=Europe/Berlin
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
RUN javac -d ./bin -classpath ./lib/jsoup-1.8.1.jar:./lib/sqlite-jdbc-3.21.0.jar:.lib/mysql-connector-java-8.0.18.jar:./resources/* ./src/db/*.java
CMD java -cp ./bin:./lib/jsoup-1.8.1.jar:./lib/sqlite-jdbc-3.21.0.jar:./lib/mysql-connector-java-8.0.18.jar db.DbCrawler

# dbstatistic
Simple jsoup application to pull train data

The intention of this project is to crawl the DB website due to the lack of a propper API and collect data about individual connections
to be stored in database for further analysis.

For ease of use a Dockerfile is included so the whole application can be deployed on a remote system.

* `docker build -t dbcrawler .`
* `docker run -it -P -v <hostpath>:/usr/src/dbcrawler/data --rm dbcrawler`

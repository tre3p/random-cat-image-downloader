# Random JPG Downloader

Service which main purpose is to start images downloading process on application startup, store them in database and log
information about downloaded files in another table.

## How to launch

Using Docker:

```bash
git clone https://github.com/tre3p/randomized-jpg-downloader 
cd randomized-jpg-downloader
docker compose up -d
```

Alternatively, application can be launched without docker, but in that case you need to have working database (
PostgreSQL) instance. JDBC connection URL and credentials can be provided using environment
variables `JDBC_URL`, `JDBC_USERNAME`, `JDBC_PASSWORD`.

If you don't have working database instance - you can use `docker-compose-pg.yml` in order to launch working PostgreSQL
instance.

```bash
git clone https://github.com/tre3p/randomized-jpg-downloader 
cd randomized-jpg-downloader
mvn clean install
cd target
java -jar randomized-jpg-downloader-1.0.0.jar
```

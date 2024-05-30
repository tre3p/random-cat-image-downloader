# Random Cat Image Downloader

Service which main purpose is to start cats images downloading process on application startup, store them in database and log
information about downloaded cats pictures (statistics) in another table. Images are downloaded with random resolutions from [LoremFlickr](https://loremflickr.com/). 

## Environment variables

You can configure environment variables inside `docker-compose.yml`. Available environments variables:

1) `IMAGE_SAVE_DIRECTORY` - directory to which images of cats will be saved. (default: `cats/`)
2) `IMAGE_DOWNLOAD_MAX_COUNT` - amount of images to download. (default: `-1`, which means downloading won't stop and you will receive infinite amount of cats images)

## How to launch

Using Docker:

```bash
git clone https://github.com/tre3p/random-cat-image-downloader && cd random-cat-image-downloader
docker compose up -d
```

If you run application using Docker - two folders will be mounted to directory from where docker-compose command was launched:

1) `pg-volume` - contains PostgreSQL files
2) `cats` - contains cats images, so you can enjoy them (you better do)

Alternatively, application can be launched without docker, but in that case you need to have working database (
PostgreSQL) instance. JDBC connection URL and credentials can be provided using environment
variables `JDBC_URL`, `JDBC_USERNAME`, `JDBC_PASSWORD`.

If you don't have working database instance - you can use `docker-compose-pg.yml` in order to launch working PostgreSQL
instance. No more configuration required in that case.

```bash
git clone https://github.com/tre3p/random-cat-image-downloader && cd random-cat-image-downloader
mvn clean package
java -jar target/random-cat-image-downloader-1.1.0.jar
```

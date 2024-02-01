CREATE TABLE IF NOT EXISTS files(
    id bigint generated always as identity,
    url varchar(50),
    size double precision,
    content_type varchar(50),
    content bytea
);

CREATE TABLE IF NOT EXISTS summary(
    id bigint generated always as identity,
    files_count bigint,
    files_size double precision,
    stat_timestamp timestamp
);
CREATE TABLE IF NOT EXISTS files(
    id serial primary key,
    url varchar(50),
    size integer,
    content_type varchar(50),
    content bytea
);

CREATE TABLE IF NOT EXISTS summary(
    id serial primary key,
    files_count integer,
    files_size integer,
    updated_at timestamp
);
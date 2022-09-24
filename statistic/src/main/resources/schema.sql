CREATE TABLE IF NOT EXISTS statistics
(
    statistic_id integer NOT NULL GENERATED ALWAYS AS IDENTITY,
    app          varchar NOT NULL,
    url          varchar NOT NULL,
    ip           varchar NOT NULL,
    time         timestamp,
    PRIMARY KEY (statistic_id)
);


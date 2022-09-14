CREATE TABLE IF NOT EXISTS categories
(
    category_id integer NOT NULL GENERATED ALWAYS AS IDENTITY,
    name        varchar NOT NULL,
    PRIMARY KEY (category_id)
);
CREATE TABLE IF NOT EXISTS users
(
    user_id integer NOT NULL GENERATED ALWAYS AS IDENTITY,
    name    varchar NOT NULL,
    email   varchar NOT NULL,
    PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS events
(
    event_id           integer NOT NULL GENERATED ALWAYS AS IDENTITY,
    annotation         text    NOT NULL,
    category_id        integer,
    created            timestamp DEFAULT now(),
    description        text,
    event_date         timestamp,
    initiator_id       integer,
    latitude           real,
    longitude          real,
    paid               boolean,
    participant_limit  integer   DEFAULT 0,
    published          timestamp,
    request_moderation boolean   DEFAULT true,
    state              varchar(10),
    title              text    NOT NULL,
    PRIMARY KEY (event_id),
    CONSTRAINT "events_categories_FK" FOREIGN KEY (category_id)
        REFERENCES categories (category_id)
        ON DELETE CASCADE,
    CONSTRAINT "events_initiator_FK" FOREIGN KEY (initiator_id)
        REFERENCES users (user_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS participation_request
(
    participation_request_id integer NOT NULL GENERATED ALWAYS AS IDENTITY,
    event_id                 integer,
    requester_id             integer,
    status                   varchar(10),
    created                  timestamp,
    PRIMARY KEY (participation_request_id),
    CONSTRAINT participation_request_event FOREIGN KEY (event_id)
        REFERENCES events (event_id)
        ON DELETE CASCADE,
    CONSTRAINT participation_request_requester FOREIGN KEY (requester_id)
        REFERENCES users (user_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS compilations
(
    compilation_id integer NOT NULL GENERATED ALWAYS AS IDENTITY,
    title          text,
    pinned         boolean NOT NULL,
    PRIMARY KEY (compilation_id)
);

CREATE TABLE IF NOT EXISTS compilations_events
(
    event_id       integer,
    compilation_id integer,
    PRIMARY KEY (event_id, compilation_id),
    CONSTRAINT "events_FK" FOREIGN KEY (event_id)
        REFERENCES events (event_id)
        ON DELETE CASCADE,
    CONSTRAINT compilations_FK FOREIGN KEY (compilation_id)
        REFERENCES compilations (compilation_id)
        ON DELETE CASCADE
);

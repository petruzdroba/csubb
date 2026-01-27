--intelliJ search ctrl+shift+f

CREATE TABLE friend_requests
(
    id           BIGSERIAL PRIMARY KEY,
    from_user_id BIGINT      NOT NULL,
    to_user_id   BIGINT      NOT NULL,
    status       VARCHAR(10) NOT NULL DEFAULT 'PENDING',
    request_date TIMESTAMP   NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_from_user
        FOREIGN KEY (from_user_id) REFERENCES users (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_to_user
        FOREIGN KEY (to_user_id) REFERENCES users (id)
            ON DELETE CASCADE
);


CREATE TABLE example
(
    id      BIGSERIAL PRIMARY KEY,
    message TEXT NOT NULL
);


CREATE TABLE messages
(
    id           BIGSERIAL PRIMARY KEY,
    from_user_id BIGINT    NOT NULL,
    message      TEXT      NOT NULL,
    data         TIMESTAMP NOT NULL,
    reply_id     BIGINT,
    FOREIGN KEY (from_user_id) REFERENCES users (id),
    FOREIGN KEY (reply_id) REFERENCES messages (id)
);
CREATE TABLE message_to
(
    message_id BIGINT NOT NULL,
    user_id    BIGINT NOT NULL,
    PRIMARY KEY (message_id, user_id),
    FOREIGN KEY (message_id) REFERENCES messages (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- CREATE TABLE orders (
--     id BIGSERIAL PRIMARY KEY,
--     driver_id BIGINT,
--     status VARCHAR(15) NOT NULL DEFAULT 'PENDING',
--     start_date TIMESTAMP NOT NULL,
--     end_date TIMESTAMP NOT NULL,
--     pickup_address VARCHAR(50),
--     destination_address VARCHAR(50),
--     client_name VARCHAR(50),

--     FOREIGN KEY (driver_id) REFERENCES drivers (id) ON DELETE CASCADE
-- );

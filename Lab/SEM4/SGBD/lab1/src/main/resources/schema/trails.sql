CREATE TABLE trails
(
    id      BIGSERIAL PRIMARY KEY,
    name    VARCHAR(255)     NOT NULL,
    length  DOUBLE PRECISION NOT NULL,
    park_id BIGINT           NOT NULL,
    FOREIGN KEY (park_id) REFERENCES parks (id)
);
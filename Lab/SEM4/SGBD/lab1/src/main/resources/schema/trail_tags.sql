CREATE TABLE trail_tags
(
    trail_id BIGINT NOT NULL,
    tag_id   BIGINT NOT NULL,
    PRIMARY KEY (trail_id, tag_id),
    FOREIGN KEY (trail_id) REFERENCES trails (id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tags (id) ON DELETE CASCADE
);
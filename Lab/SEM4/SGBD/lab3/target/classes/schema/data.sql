TRUNCATE trail_tags, trails, tags, parks RESTART IDENTITY CASCADE;

INSERT INTO parks (name, country)
VALUES ('Yellowstone', 'USA'),
       ('Grand Canyon', 'USA'),
       ('Yosemite', 'USA');

INSERT INTO tags (name)
VALUES ('Scenic'),
       ('Difficult'),
       ('Family Friendly');

INSERT INTO trails (name, length, park_id)
VALUES ('Old Faithful Trail', 3.5, 1),
       ('Geyser Loop', 5.2, 1),
       ('South Rim Trail', 9.8, 2),
       ('Bright Angel', 12.4, 2),
       ('Half Dome', 14.2, 3),
       ('Valley Loop', 6.1, 3);

INSERT INTO trail_tags (trail_id, tag_id)
VALUES (1, 1),
       (1, 3),
       (2, 1),
       (2, 2),
       (3, 1),
       (3, 2),
       (4, 2),
       (5, 2),
       (6, 1),
       (6, 3);
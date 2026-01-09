SET enable_seqscan = off;

-- view faciliteis trail
CREATE INDEX idx_facilities_id ON facilities(id);
CREATE INDEX idx_facilities_name ON facilities(name);

CREATE INDEX idx_trailfacilities_trail_id ON trailfacilities(trail_id);
CREATE INDEX idx_trailfacilities_facility_id ON trailfacilities(facility_id);

CREATE INDEX idx_trail_id ON trail(id);
CREATE INDEX idx_trail_name ON trail(name);

EXPLAIN SELECT facility_id, facility_name
FROM view_facilities_trails
WHERE trail_id = 1;

-- view trail tags
CREATE INDEX idx_trailtags_trail_id ON trailtags(trail_id);
CREATE INDEX idx_trailtags_tag_id ON trailtags(tag_id);

CREATE INDEX idx_tags_id ON tags(id);
CREATE INDEX idx_tags_name ON tags(name);


EXPLAIN ANALYZE SELECT trail_id, trail_name, tag_name
FROM view_trails_tags
WHERE tag_name = 'Loop';

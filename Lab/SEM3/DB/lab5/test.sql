-- CREATE / INSERT

CALL InsereazaTrail(11, 1, 5.6, 'Mountain Loop');
CALL InsereazaFacility(11, 'Water Fountain');
CALL InsereazaTrailFacility(11, 11, 11);
CALL InsereazaTag(11, 'Scenic');
CALL InsereazaTrailTag(11, 11, 11);
CALL InsereazaHazard(11, 'Steep Cliff');
CALL InsereazaTrailHazard(11, 11, 11);

-- INSERT FAILURE (non-existent references)
CALL InsereazaTrailHazard(1001, 1001, 1001);

-- READ (existing)
SELECT * FROM ReadFacility(11);
SELECT * FROM ReadTrail(11);
SELECT * FROM ReadTag(11);
SELECT * FROM ReadHazard(11);
SELECT * FROM ReadTrailFacilities(11);
SELECT * FROM ReadTrailTags(11);
SELECT * FROM ReadTrailHazards(11);

-- READ (non-existent)
SELECT * FROM ReadFacility(999);
SELECT * FROM ReadTrail(999);
SELECT * FROM ReadTag(999);
SELECT * FROM ReadHazard(999);

-- UPDATE (existing)
CALL UpdateFacility(11, 'Water Fountain Updated');
CALL UpdateTrailFacility(11, 11, 11);
CALL UpdateTrail(11, 'Mountain Loop Updated', 1, 6.5);
CALL UpdateTag(11, 'Scenic Updated');
CALL UpdateTrailTag(11, 11, 11);
CALL UpdateHazard(11, 'Steep Cliff Updated');
CALL UpdateTrailHazard(11, 11, 11);

-- READ AFTER UPDATE
SELECT * FROM ReadFacility(11);
SELECT * FROM ReadTrailFacilities(11);
SELECT * FROM ReadTrail(11);
SELECT * FROM ReadTrailTags(11);
SELECT * FROM ReadTrailHazards(11);
SELECT * FROM ReadTag(11);
SELECT * FROM ReadHazard(11);

-- UPDATE FAILURE (non-existent)
CALL UpdateFacility(999, 'Nonexistent Facility');
CALL UpdateTrailFacility(999, 11, 11);
CALL UpdateTrail(999, 'Nonexistent Trail', 1, 6.5);
CALL UpdateTag(999, 'Nonexistent Tag');
CALL UpdateTrailTag(999, 11, 1);
CALL UpdateHazard(999, 'Nonexistent Hazard');
CALL UpdateTrailHazard(999, 11, 11);

-- DELETE (existing)
CALL DeleteTrailFacility(11);
CALL DeleteTrailTag(11);
CALL DeleteTrailHazard(11);
CALL DeleteTrail(11);
CALL DeleteFacility(11);
CALL DeleteTag(11);
CALL DeleteHazard(11);

-- DELETE FAILURE (non-existent)
CALL DeleteTrailFacility(999);
CALL DeleteTrailTag(999);
CALL DeleteTrailHazard(999);
CALL DeleteTrail(999);
CALL DeleteFacility(999);
CALL DeleteTag(999);
CALL DeleteHazard(999);
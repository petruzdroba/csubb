CREATE [UNIQUE] [CLUSTERED | NONCLUSTERED] INDEX IndexName
ON EntityName (Param1, Param2, ..., ParamN)
[INCLUDE (Extra1, Extra2, ..., ExtraM)];


CREATE OR ALTER VIEW ViewName
AS
SELECT Column1, Column2, ..., ColumnN
FROM EntityName
[WHERE condition]
[GROUP BY ColumnX, ...]
[HAVING condition];

CREATE OR ALTER FUNCTION FunctionName
(
    @Param1 Type,
    ...
)
RETURNS TABLE
AS
RETURN
(
    SELECT Column1, Column2, ...
    FROM EntityName
    WHERE condition
);

CREATE OR ALTER TRIGGER TriggerName
ON EntityName
[AFTER | INSTEAD OF] [INSERT | UPDATE | DELETE]
AS
BEGIN
    -- logic using inserted / deleted
END;

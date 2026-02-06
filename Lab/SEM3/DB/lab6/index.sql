DROP TABLE IF EXISTS dbo.TestIndex;
GO

CREATE TABLE dbo.TestIndex
(
    id INT IDENTITY PRIMARY KEY,
    value INT NOT NULL,
    filler CHAR(200) NOT NULL
);
GO

-- insert 100,000 rows
INSERT INTO dbo.TestIndex (value, filler)
SELECT TOP (100000)
       ROW_NUMBER() OVER (ORDER BY (SELECT NULL)),
       REPLICATE('X', 200)
FROM sys.all_objects a
CROSS JOIN sys.all_objects b;
GO

CREATE NONCLUSTERED INDEX IX_TestIndex_value
ON dbo.TestIndex (value);
GO


SELECT *
FROM dbo.TestIndex
WHERE value = 50000;
GO

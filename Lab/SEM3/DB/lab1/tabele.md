### Trail Management Database Relationships

| Type | From | To | Description |
|------|------|----|--------------|
| 1:N | Park | Trails | A park has many trails |
| 1:N | Trail | Checkpoints | A trail has many checkpoints |
| 1:N | Trail | Maintenance Logs | Maintenance records per trail |
| 1:N | Trail | Photos | Photos tied to trails |
| 1:N | Trail | Elevation Profile | Points for elevation graph |
| M:N | Trail | Tags | TrailTags |
| M:N | Trail | Hazards | TrailHazards |
| M:N | Trail | Facilities |  TrailInfrastructure |


1:N Reference the park id, so each trail has a park id, but the park dosent hold
all the trail ids

https://dbdiagram.io/ for better generated diagrams
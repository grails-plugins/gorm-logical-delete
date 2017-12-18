# gorm-logical-delete
GORM Logical Delete For Grails 3.

(Rough Summary)
The plugin will provide logical delete functionality on Domain objects. Logical deletes are defined as marking an object
`deleted` without actually removing it from the database. Subsequently, queries searching for data will exclude the marks items.
This is useful for retaining data without having it clutter the current scope of used data.

## Docs

See [grails-plugins.github.io/gorm-logical-delete/](https://grails-plugins.github.io/gorm-logical-delete/).

## RoadMap

- override Gorm delete functionality so it marks a `deleted` fields `true` without actually removing the data
- provide a mechanism to hard delete
- all queries by default should exclude logically deleted items from it's return set
    - dynamic finders
    - detached criteria builders
    - projections
    - get methods
    - load
    - proxy methods
- queries should be able to find "logically deleted" items when specified. For example, by sending an attribute `includeDeleted: true`,
queries (which are by default going to hide logically deleted entities) would be able to include the entities
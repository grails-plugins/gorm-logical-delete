# gorm-logical-delete
GORM Logical Delete For Grails 3.
(aka Soft Delete)

(Rough Summary)
The plugin will provide logical delete functionality on Domain objects. Logical deletes are defined as marking an object
`deleted` without actually removing it from the database. Subsequently, queries searching for data will exclude the marked items.
This is useful for retaining data without having it clutter the current scope of used data.

Once a domain has implemented `LogicalDelete` it becomes the default for that domain. What that means is by default
all logically deleted items will be ignored by `get`, `load`, `read`, `proxy`, `findAll`, and any of the dynamic finder methods.
While it is still possibly to use all the mentioned methods to return deleted records it is not the default. It is also
still possible to perform an actual `hard` delete.

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

## How to add to your domain

To implement logical deletes for the domain it is is quite simple

```
class Person implements LocicalDelete {
    String userName
}
```

By implementing `LogicalDelete` a `deleted` property with the default value of `false` will automatically be added to
your domain class to keep track of logically deleted records. Standard default values for the `deleted` property are 
`true` or `false`.

If you wish to use a non default column name or a different default value you can do so by leveraging the standard GORM
mapping closure.

```
class Person implements LocicalDelete {
    String userName
    
    static mapping = {
        deleted column:"nonStdDeleteCol", sqlType: "SMALLINT"
    }
}
```

The above will result in your table having a `NON_STD_DELETE_COL` in the table and instead of `true` or `false` it will
contain `0` and `1`.

## Performing a 'Hard' delete
```
p.delete(hard: true)
```

## Get 'deleted' records
```
Person.withDeleted { Person.get(1) }
```

## Search 'deleted records'
```
Person.withDeleted { Person.findAll() }
```
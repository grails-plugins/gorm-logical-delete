package gorm.logical.delete.test

// tag::person_class[]
import gorm.logical.delete.LogicalDelete

// end::person_class[]
import grails.gorm.annotation.Entity

@Entity
// tag::person_class[]
class Person implements LogicalDelete<Person> {
    String userName

    static mapping = {
        // the deleted property may be configured
        // like any other persistent property...
        deleted column:"delFlag"
    }
}
// end::person_class[]

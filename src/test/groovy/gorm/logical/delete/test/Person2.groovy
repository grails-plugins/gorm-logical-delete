package gorm.logical.delete.test

import gorm.logical.delete.typetrait.DateLogicalDelete
import grails.gorm.annotation.Entity

@Entity
class Person2 implements DateLogicalDelete<Person2> {
    String userName

    static mapping = {
        // the deleted property may be configured
        // like any other persistent property...
        deleted column:"delFlag"
    }

    static constraints = {
        deleted nullable: true
    }
}

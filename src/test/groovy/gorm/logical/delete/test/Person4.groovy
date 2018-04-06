package gorm.logical.delete.test

import gorm.logical.delete.typetrait.BooleanLogicalDelete
import grails.gorm.annotation.Entity

@Entity
class Person4 implements BooleanLogicalDelete<Person4> {
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
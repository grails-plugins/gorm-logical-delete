package gorm.logical.delete.test

import gorm.logical.delete.typetrait.StringLogicalDelete
import grails.gorm.annotation.Entity

@Entity
class Person3 implements StringLogicalDelete<Person3> {
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

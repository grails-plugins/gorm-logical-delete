package gorm.logical.delete.test

import gorm.logical.delete.LogicalDelete
import grails.gorm.annotation.Entity

@Entity
class Person implements LogicalDelete<Person> {
    String userName

    static mapping = {
        deleted column:"delFlag"
    }
}

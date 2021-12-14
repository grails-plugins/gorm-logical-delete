package gorm.logical.delete

import gorm.logical.delete.typetrait.StringLogicalDelete
import grails.gorm.annotation.Entity

class Book implements StringLogicalDelete<Book> {

    String title

    static constraints = {
        title blank:false, unique: true
        deleted nullable: true
    }
}

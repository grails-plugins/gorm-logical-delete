package gorm.logical.delete

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import spock.lang.Specification

@Integration
@Rollback
class GormLogicalDeleteIntSpec extends Specification {

    void setup() {
        new Book(title: "NiravBook").save(failOnError: true, flush: true)
        new Book(title: "RobBook").save(failOnError: true, flush: true)
    }

    void "test logical delete on sample domain Book"() {
        when:
        Book niravBook = Book.findByTitle('NiravBook')
        niravBook.delete()

        niravBook.save(flush: true)
        List<Book> books = Book.list()
        def whereQuery = Book.where {
            title == "NiravBook" || title == "RobBook"
        }.list()
        Book dynamicFinderAfterDeleted = Book.findByTitle('NiravBook')
        Book withDeletedAfterDeleted = Book.withDeleted {
            Book.findByTitle('NiravBook')
        }


        then:
        assert books.size() == 2
        assert whereQuery.size() == 1
        dynamicFinderAfterDeleted == null
        withDeletedAfterDeleted
    }

}

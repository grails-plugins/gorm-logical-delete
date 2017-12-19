package gorm.logical.delete

import grails.gorm.DetachedCriteria
import grails.gorm.transactions.Rollback
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

/**
 * This test suite focuses on the withDeleted implementation so the api can retrieve deleted items in queries
 */
class WithDeletedSpec extends Specification implements DomainUnitTest<Person> {

    Closure doWithSpring() {
        { ->
            queryListener PreQueryListener
        }
    }

    /******************* test with delete ***********************************/

    @Rollback
    void 'test withDeleted findAll - logical deleted items'() {
        given:
        Person.createUsers()

        when:
        assert Person.count() == 3
        Person.findByUserName("Ben").delete()
        Person.findByUserName("Nirav").delete()
        def results = Person.findAll()

        then: "we should get only deleted=false items"
        results.size() == 1

        when: "We should get all items - included deleted"
        results = Person.withDeleted { Person.findAll() }

        then:
        results.size() == 3
    }

    @Rollback
    void 'test withDeleted detached criteria'() {
        given:
        Person.createUsers()

        when:
        assert Person.count() == 3
        Person.findByUserName("Ben").delete()
        Person.findByUserName("Nirav").delete()
        DetachedCriteria<Person> query = Person.where {
            userName == "Ben" || userName == "Nirav"
        }
        def results = Person.withDeleted {
            query.list()
        }

        then: "we should get deleted items"
        results.size() == 2
    }

    @Rollback
    void 'test withDeleted criteria'() {
        given:
        Person.createUsers()

        when:
        assert Person.count() == 3
        Person.findByUserName("Ben").delete()
        Person.findByUserName("Nirav").delete()
        def criteria = Person.createCriteria()
        def results = Person.withDeleted {
            criteria {
                or {
                    eq("userName", "Ben")
                    eq("userName", "Nirav")
                }
            }
        }

        then: "we should get deleted items"
        results.size() == 2

    }
}
package gorm.logical.delete

import gorm.logical.delete.test.Person
import gorm.logical.delete.test.PersonTestData
import grails.gorm.DetachedCriteria
import grails.gorm.transactions.Rollback
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

/**
 * This test suite focuses on the withDeleted implementation so the api can retrieve deleted items in queries
 */
class WithDeletedSpec extends Specification implements DomainUnitTest<Person>, PersonTestData {

    /******************* test with delete ***********************************/

    @Rollback
    void 'test withDeleted findAll - logical deleted items'() {
        when:
        assert Person.count() == 3
        Person.findByUserName("Ben").delete()
        Person.findByUserName("Nirav").delete()
        def results = Person.findAll()

        then: "we should get only deleted=false items"
        results.size() == 1

        when: "We should get all items - included deleted"

        // tag::find_all_with_deleted[]
        results = Person.withDeleted { Person.findAll() }
        // end::find_all_with_deleted[]

        then:
        results.size() == 3
    }

    @Rollback
    void 'test withDeleted detached criteria'() {
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

    void 'test that the thread local is restored to false even if the closure throws an exception'() {
        when:
        Person.withDeleted {
            throw new IllegalStateException()
        }

        then:
        thrown IllegalStateException

        and:
        !PreQueryListener.IGNORE_DELETED_FILTER.get()
    }

    void 'test that nested .withDeleted calls work as expected'() {
        // One wouldn't directly nest calls to withDeleted intentionally
        // but a service method could use withDeleted and invoke another service
        // method which also invokes with deleted, and that could cause a problem
        when:
        assert Person.count() == 3
        Person.findByUserName("Ben").delete()
        Person.findByUserName("Nirav").delete()
        def results = Person.findAll()

        then: "we should get only deleted=false items"
        results.size() == 1

        when: "We should get all items - included deleted"
        results = Person.withDeleted {
            Person.withDeleted {}

            // make sure the filter is still working after the previous call
            // to withDeletedl...
            Person.findAll()
        }

        then:
        results.size() == 3

    }
}
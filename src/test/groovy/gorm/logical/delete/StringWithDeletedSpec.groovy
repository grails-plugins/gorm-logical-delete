package gorm.logical.delete

import gorm.logical.delete.test.Person
import gorm.logical.delete.test.Person3
import gorm.logical.delete.test.Person3TestData
import grails.gorm.DetachedCriteria
import grails.gorm.transactions.Rollback
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class StringWithDeletedSpec extends Specification implements DomainUnitTest<Person3>, Person3TestData {

    /******************* test with delete ***********************************/

    @Rollback
    void 'test withDeleted findAll - logical deleted items'() {
        when:
        assert Person3.count() == 3
        Person3.findByUserName("Ben").delete('test')
        Person3.findByUserName("Nirav").delete('test')
        def results = Person3.findAll()

        then: "we should get only deleted=false items"
        results.size() == 1

        when: "We should get all items - included deleted"

        // tag::find_all_with_deleted[]
        results = Person3.withDeleted { Person3.findAll() }
        // end::find_all_with_deleted[]

        then:
        results.size() == 3
    }

    @Rollback
    void 'test withDeleted detached criteria'() {
        when:
        assert Person3.count() == 3
        Person3.findByUserName("Ben").delete('test')
        Person3.findByUserName("Nirav").delete('test')
        DetachedCriteria<Person3> query = Person3.where {
            userName == "Ben" || userName == "Nirav"
        }
        def results = Person3.withDeleted {
            query.list()
        }

        then: "we should get deleted items"
        results.size() == 2
    }

    @Rollback
    void 'test withDeleted criteria'() {
        when:
        assert Person3.count() == 3
        Person3.findByUserName("Ben").delete('test')
        Person3.findByUserName("Nirav").delete('test')
        def criteria = Person3.createCriteria()
        def results = Person3.withDeleted {
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
        Person3.withDeleted {
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
        assert Person3.count() == 3
        Person3.findByUserName("Ben").delete('test')
        Person3.findByUserName("Nirav").delete('test')
        def results = Person3.findAll()

        then: "we should get only deleted=false items"
        results.size() == 1

        when: "We should get all items - included deleted"
        results = Person3.withDeleted {
            Person3.withDeleted {}

            // make sure the filter is still working after the previous call
            // to withDeletedl...
            Person3.findAll()
        }

        then:
        results.size() == 3

    }
}

package gorm.logical.delete

import gorm.logical.delete.test.Person4
import gorm.logical.delete.test.Person4TestData
import grails.gorm.DetachedCriteria
import grails.gorm.transactions.Rollback
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class BooleanWithDeletedSpec extends Specification implements DomainUnitTest<Person4>, Person4TestData {

    /******************* test with delete ***********************************/

    @Rollback
    void 'test withDeleted findAll - logical deleted items'() {
        when:
        assert Person4.count() == 3
        Person4.findByUserName("Ben").delete()
        Person4.findByUserName("Nirav").delete()
        def results = Person4.findAll()

        then: "we should get only deleted=false items"
        results.size() == 1

        when: "We should get all items - included deleted"

        // tag::find_all_with_deleted[]
        results = Person4.withDeleted { Person4.findAll() }
        // end::find_all_with_deleted[]

        then:
        results.size() == 3
    }

    @Rollback
    void 'test withDeleted detached criteria'() {
        when:
        assert Person4.count() == 3
        Person4.findByUserName("Ben").delete()
        Person4.findByUserName("Nirav").delete()
        DetachedCriteria<Person4> query = Person4.where {
            userName == "Ben" || userName == "Nirav"
        }
        def results = Person4.withDeleted {
            query.list()
        }

        then: "we should get deleted items"
        results.size() == 2
    }

    @Rollback
    void 'test withDeleted criteria'() {
        when:
        assert Person4.count() == 3
        Person4.findByUserName("Ben").delete()
        Person4.findByUserName("Nirav").delete()
        def criteria = Person4.createCriteria()
        def results = Person4.withDeleted {
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
        Person4.withDeleted {
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
        assert Person4.count() == 3
        Person4.findByUserName("Ben").delete()
        Person4.findByUserName("Nirav").delete()
        def results = Person4.findAll()

        then: "we should get only deleted=false items"
        results.size() == 1

        when: "We should get all items - included deleted"
        results = Person4.withDeleted {
            Person4.withDeleted {}

            // make sure the filter is still working after the previous call
            // to withDeletedl...
            Person4.findAll()
        }

        then:
        results.size() == 3

    }
}

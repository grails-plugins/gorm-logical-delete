package gorm.logical.delete

import gorm.logical.delete.test.Person3
import gorm.logical.delete.test.Person3TestData
import grails.gorm.transactions.Rollback
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class StringCriteriaSpec extends Specification implements DomainUnitTest<Person3>, Person3TestData {

    /******************* test criteria ***********************************/

    @Rollback
    void 'test criteria - logical deleted items'() {
        // where detachedCriteria Call
        when:
        assert Person3.count() == 3
        Person3.findByUserName("Ben").delete('test')
        Person3.findByUserName("Nirav").delete('test')
        // tag::criteria_query[]
        def criteria = Person3.createCriteria()
        def results = criteria {
            or {
                eq("userName", "Ben")
                eq("userName", "Nirav")
            }
        }
        // end::criteria_query[]

        then: "we should not get anything bc they were deleted"
        !results

        when:
        results = criteria {
            eq("userName", "Jeff")
        }

        then:
        results
        results[0].userName == 'Jeff'
    }

    /******************* test criteria with projection ***********************************/

    @Rollback
    void 'test criteria with projection - logical deleted items'() {
        // projection Call
        when:
        assert Person3.count() == 3
        Person3.findByUserName("Ben").delete('test')
        Person3.findByUserName("Nirav").delete('test')
        def criteria = Person3.createCriteria()
        def results = criteria.get {
            projections {
                count()
            }
        }

        then: "we should not get the deleted items"
        results == 1
    }
}

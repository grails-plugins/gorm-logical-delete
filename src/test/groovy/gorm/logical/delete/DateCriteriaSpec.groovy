package gorm.logical.delete

import gorm.logical.delete.test.Person
import gorm.logical.delete.test.Person2
import gorm.logical.delete.test.Person2TestData
import grails.gorm.transactions.Rollback
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class DateCriteriaSpec extends Specification implements DomainUnitTest<Person2>, Person2TestData {

    /******************* test criteria ***********************************/

    @Rollback
    void 'test criteria - logical deleted items'() {
        // where detachedCriteria Call
        when:
        assert Person2.count() == 3
        Person2.findByUserName("Ben").delete()
        Person2.findByUserName("Nirav").delete()
        // tag::criteria_query[]
        def criteria = Person2.createCriteria()
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
        assert Person2.count() == 3
        Person2.findByUserName("Ben").delete()
        Person2.findByUserName("Nirav").delete()
        def criteria = Person2.createCriteria()
        def results = criteria.get {
            projections {
                count()
            }
        }

        then: "we should not get the deleted items"
        results == 1
    }
}

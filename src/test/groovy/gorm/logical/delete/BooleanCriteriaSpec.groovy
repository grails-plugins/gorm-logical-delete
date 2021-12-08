package gorm.logical.delete

import gorm.logical.delete.test.Person4
import gorm.logical.delete.test.Person4TestData
import grails.gorm.transactions.Rollback
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class BooleanCriteriaSpec extends Specification implements DomainUnitTest<Person4>, Person4TestData {

    /******************* test criteria ***********************************/

    @Rollback
    void 'test criteria - logical deleted items'() {
        // where detachedCriteria Call
        when:
        assert Person4.count() == 3
        Person4.findByUserName("Ben").delete()
        Person4.findByUserName("Nirav").delete()
        // tag::criteria_query[]
        def criteria = Person4.createCriteria()
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
        assert Person4.count() == 3
        Person4.findByUserName("Ben").delete()
        Person4.findByUserName("Nirav").delete()
        def criteria = Person4.createCriteria()
        def results = criteria.get {
            projections {
                count()
            }
        }

        then: "we should not get the deleted items"
        results == 1
    }
}

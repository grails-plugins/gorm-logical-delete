package gorm.logical.delete

import grails.gorm.transactions.Rollback
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

/**
 * This test suite focuses on the behavior of criteria API in collaboration with the PreQuery Listener
 */
class CriteriaSpec extends Specification implements DomainUnitTest<Person> {

    Closure doWithSpring() { { ->
            queryListener PreQueryListener
        }
    }

    /******************* test criteria ***********************************/

    @Rollback
    void 'test criteria - logical deleted items'() {
        given:
        Person.createUsers()

        // where detachedCriteria Call
        when:
        assert Person.count() == 3
        Person.findByUserName("Ben").delete()
        Person.findByUserName("Nirav").delete()
        def criteria = Person.createCriteria()
        def results = criteria {
            or {
                eq("userName", "Ben")
                eq("userName", "Nirav")
            }
        }

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
        given:
        Person.createUsers()

        // projection Call
        when:
        assert Person.count() == 3
        Person.findByUserName("Ben").delete()
        Person.findByUserName("Nirav").delete()
        def criteria = Person.createCriteria()
        def results = criteria.get {
            projections {
                count()
            }
        }

        then: "we should not get the deleted items"
        results == 1
    }

}
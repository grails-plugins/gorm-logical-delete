package gorm.logical.delete

import gorm.logical.delete.test.Person
import gorm.logical.delete.test.PersonTestData
import grails.gorm.DetachedCriteria
import grails.gorm.transactions.Rollback
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

/**
 * This test suite focuses on the behavior of detached criteria in collaboration with the PreQuery Listener
 */
class DetachedCriteriaSpec extends Specification implements DomainUnitTest<Person>, PersonTestData {

    /******************* test where ***********************************/

    @Rollback
    void 'test detached criteria where - logical deleted items'() {
        // where detachedCriteria Call
        when:
        assert Person.count() == 3
        Person.findByUserName("Ben").delete()
        Person.findByUserName("Nirav").delete()
        // tag::detachedCriteria_query[]
        DetachedCriteria<Person> query = Person.where {
            userName == "Ben" || userName == "Nirav"
        }
        def results = query.list()
        // end::detachedCriteria_query[]
        then: "we should not get anything bc they were deleted"
        !results

        when:
        query = Person.where {
            userName == "Jeff"
        }
        results = query.find()

        then:
        results
        results.userName == 'Jeff'
    }

    /******************* test findall ***********************************/

    @Rollback
    void 'test detached criteria findAll - logical deleted items'() {
        // findAll detachedCriteria Call
        when:
        assert Person.count() == 3
        Person.findByUserName("Ben").delete()
        Person.findByUserName("Nirav").delete()
        def results = Person.findAll {
            userName == "Ben" || userName == "Nirav"
        }

        then: "we should not get anything bc they were deleted"
        !results

        when:
        results = Person.findAll {
            userName == "Jeff"
        }

        then:
        results
        results[0].userName == 'Jeff'
    }

    /********************* setup *****************************/
}




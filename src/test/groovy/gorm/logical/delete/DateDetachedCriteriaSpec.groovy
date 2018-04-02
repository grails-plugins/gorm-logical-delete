package gorm.logical.delete

import gorm.logical.delete.test.Person2
import gorm.logical.delete.test.Person2TestData
import grails.gorm.DetachedCriteria
import grails.gorm.transactions.Rollback
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class DateDetachedCriteriaSpec extends Specification implements DomainUnitTest<Person2>, Person2TestData {

    /******************* test where ***********************************/

    @Rollback
    void 'test detached criteria where - logical deleted items'() {
        // where detachedCriteria Call
        when:
        assert Person2.count() == 3
        Person2.findByUserName("Ben").delete()
        Person2.findByUserName("Nirav").delete()
        // tag::detachedCriteria_query[]
        DetachedCriteria<Person2> query = Person2.where {
            userName == "Ben" || userName == "Nirav"
        }
        def results = query.list()
        // end::detachedCriteria_query[]
        then: "we should not get anything bc they were deleted"
        !results

        when:
        query = Person2.where {
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
        assert Person2.count() == 3
        Person2.findByUserName("Ben").delete()
        Person2.findByUserName("Nirav").delete()
        def results = Person2.findAll {
            userName == "Ben" || userName == "Nirav"
        }

        then: "we should not get anything bc they were deleted"
        !results

        when:
        results = Person2.findAll {
            userName == "Jeff"
        }

        then:
        results
        results[0].userName == 'Jeff'
    }

    /********************* setup *****************************/
}

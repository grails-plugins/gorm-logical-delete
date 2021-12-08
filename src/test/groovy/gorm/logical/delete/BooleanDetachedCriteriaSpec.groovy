package gorm.logical.delete

import gorm.logical.delete.test.Person4
import gorm.logical.delete.test.Person4TestData
import grails.gorm.DetachedCriteria
import grails.gorm.transactions.Rollback
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class BooleanDetachedCriteriaSpec extends Specification implements DomainUnitTest<Person4>, Person4TestData {

    /******************* test where ***********************************/

    @Rollback
    void 'test detached criteria where - logical deleted items'() {
        // where detachedCriteria Call
        when:
        assert Person4.count() == 3
        Person4.findByUserName("Ben").delete()
        Person4.findByUserName("Nirav").delete()
        // tag::detachedCriteria_query[]
        DetachedCriteria<Person4> query = Person4.where {
            userName == "Ben" || userName == "Nirav"
        }
        def results = query.list()
        // end::detachedCriteria_query[]
        then: "we should not get anything bc they were deleted"
        !results

        when:
        query = Person4.where {
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
        assert Person4.count() == 3
        Person4.findByUserName("Ben").delete()
        Person4.findByUserName("Nirav").delete()
        def results = Person4.findAll {
            userName == "Ben" || userName == "Nirav"
        }

        then: "we should not get anything bc they were deleted"
        !results

        when:
        results = Person4.findAll {
            userName == "Jeff"
        }

        then:
        results
        results[0].userName == 'Jeff'
    }

    /********************* setup *****************************/
}

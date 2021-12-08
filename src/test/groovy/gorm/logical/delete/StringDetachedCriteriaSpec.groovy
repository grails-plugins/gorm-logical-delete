package gorm.logical.delete

import gorm.logical.delete.test.Person3
import gorm.logical.delete.test.Person3TestData
import grails.gorm.DetachedCriteria
import grails.gorm.transactions.Rollback
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class StringDetachedCriteriaSpec extends Specification implements DomainUnitTest<Person3>, Person3TestData {

    /******************* test where ***********************************/

    @Rollback
    void 'test detached criteria where - logical deleted items'() {
        // where detachedCriteria Call
        when:
        assert Person3.count() == 3
        Person3.findByUserName("Ben").delete('test')
        Person3.findByUserName("Nirav").delete('test')
        // tag::detachedCriteria_query[]
        DetachedCriteria<Person3> query = Person3.where {
            userName == "Ben" || userName == "Nirav"
        }
        def results = query.list()
        // end::detachedCriteria_query[]
        then: "we should not get anything bc they were deleted"
        !results

        when:
        query = Person3.where {
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
        assert Person3.count() == 3
        Person3.findByUserName("Ben").delete('test')
        Person3.findByUserName("Nirav").delete('test')
        def results = Person3.findAll {
            userName == "Ben" || userName == "Nirav"
        }

        then: "we should not get anything bc they were deleted"
        !results

        when:
        results = Person3.findAll {
            userName == "Jeff"
        }

        then:
        results
        results[0].userName == 'Jeff'
    }

    /********************* setup *****************************/
}

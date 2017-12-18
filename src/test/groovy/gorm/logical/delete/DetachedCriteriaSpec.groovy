package gorm.logical.delete

import grails.gorm.DetachedCriteria
import grails.gorm.annotation.Entity
import grails.gorm.transactions.Rollback
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

/**
 * This test suite focuses on the behavior of detached criteria in collaboration with the PreQuery Listener
 */
class DetachedCriteriaSpec extends Specification implements DomainUnitTest<PersonC> {

    Closure doWithSpring() { { ->
            queryListener PreQueryListener
        }
    }

    /******************* test where ***********************************/

    @Rollback
    void 'test detached criteria where - logical deleted items'() {
        given:
        createUsers()

        // where detachedCriteria Call
        when:
        assert PersonC.count() == 3
        PersonC.findByUserName("Ben").delete()
        PersonC.findByUserName("Nirav").delete()
        DetachedCriteria<PersonC> query = PersonC.where {
            userName == "Ben" || userName == "Nirav"
        }
        def results = query.list()

        then: "we should not get anything bc they were deleted"
        !results

        when:
        query = PersonC.where {
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
        given:
        createUsers()

        // findAll detachedCriteria Call
        when:
        assert PersonC.count() == 3
        PersonC.findByUserName("Ben").delete()
        PersonC.findByUserName("Nirav").delete()
        def results = PersonC.findAll {
            userName == "Ben" || userName == "Nirav"
        }

        then: "we should not get anything bc they were deleted"
        !results

        when:
        results = PersonC.findAll {
            userName == "Jeff"
        }

        then:
        results
        results[0].userName == 'Jeff'
    }


    /********************* setup *****************************/

    private List<PersonC> createUsers() {
        def ben = new PersonC(userName: "Ben").save(flush: true)
        def nirav = new PersonC(userName: "Nirav").save(flush: true)
        def jeff = new PersonC(userName: "Jeff").save(flush: true)
        [ben, nirav, jeff]
    }
}

/**************** GORM Entity *****************************/

@Entity
class PersonC implements LogicalDelete {
    String userName

    String toString() {
        "$userName"
    }
}
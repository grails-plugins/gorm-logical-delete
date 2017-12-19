package gorm.logical.delete

import grails.gorm.DetachedCriteria
import grails.gorm.annotation.Entity
import grails.gorm.transactions.Rollback
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

/**
 * This test suite focuses on the behavior of detached criteria in collaboration with the PreQuery Listener
 */
class DetachedCriteriaSpec extends Specification implements DomainUnitTest<Person> {

    Closure doWithSpring() { { ->
            queryListener PreQueryListener
        }
    }

    /******************* test where ***********************************/

    @Rollback
    void 'test detached criteria where - logical deleted items'() {
        given:
        Person.createUsers()

        // where detachedCriteria Call
        when:
        assert Person.count() == 3
        Person.findByUserName("Ben").delete()
        Person.findByUserName("Nirav").delete()
        DetachedCriteria<Person> query = Person.where {
            userName == "Ben" || userName == "Nirav"
        }
        def results = query.list()

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
        given:
        Person.createUsers()

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
}

/**************** GORM Entity *****************************/

@Entity
class Person implements LogicalDelete {
    String userName

    String toString() {
        "$userName"
    }

    static mapping = {
        deleted column:"delFlag"
    }

    /********************* setup *****************************/

    static List<Person> createUsers() {
        def ben = new Person(userName: "Ben").save(flush: true)
        def nirav = new Person(userName: "Nirav").save(flush: true)
        def jeff = new Person(userName: "Jeff").save(flush: true)
        [ben, nirav, jeff]
    }
}
package gorm.logical.delete

import grails.gorm.annotation.Entity
import grails.gorm.transactions.Rollback
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

/**
 * This test suite focuses on the behavior of dynamic finders in collaboration with the PreQuery Listener
 */
class DynamicFindersSpec extends Specification implements DomainUnitTest<PersonC> {

    Closure doWithSpring() { { ->
            queryListener PreQueryListener
        }
    }

    /******************* test FindAll ***********************************/

    @Rollback
    void 'test dynamic findAll hide logical deleted items'() {
        given:
        createUsers()

        // findAll() Call
        when:
        assert PersonC.count() == 3
        PersonC.findByUserName("Ben").delete()
        PersonC.findByUserName("Nirav").delete()
        List<PersonC> results = PersonC.findAll()

        then: "we should only get those not logically deleted"
        results.size() == 1
        results[0].userName == 'Jeff'

        // list() calll
        when:
        results.clear()
        results = PersonC.list()

        then:
        results.size() == 1
        results[0].userName == 'Jeff'
    }

    /***************** test findBy ***************************/

    @Rollback
    void 'test dynamic findByUserName hide logical deleted items'() {
        given:
        createUsers()

        // findByUserName() Call
        when:
        assert PersonC.count() == 3
        PersonC.findByUserName("Ben").delete()
        PersonC.findByUserName("Nirav").delete()
        PersonC result1 = PersonC.findByUserName("Ben")
        PersonC result2 = PersonC.findByUserName("Nirav")

        then:  "we shouldn't get any bc it was deleted"
        !result1
        !result2
    }

    /***************** test findByDeleted ***************************/

    @Rollback
    void 'test dynamic findByDeleted hide logical deleted items'() {
        given:
        createUsers()

        // findByDeleted() Call
        when:
        assert PersonC.count() == 3
        PersonC.findByUserName("Ben").delete()
        PersonC.findByUserName("Nirav").delete()
        List<PersonC> results = PersonC.findAllByDeleted(true)

        then: "we should not get any because these are logically deleted"
        results.size() == 0
        results.clear()

        when:
        results = PersonC.findAllByDeleted(false)

        then: "we should find the entity because it is not logically deleted"
        results.size() == 1
        results[0].userName == 'Jeff'
    }

    /***************** test get() ***************************/

    @Rollback
    void 'test dynamic get() finds logical deleted items'() {
        given:
        createUsers()

        when: "when 'get()' is used, we can access logically deleted entities"
        assert PersonC.count() == 3
        PersonC.findByUserName("Ben").delete()
        PersonC.findByUserName("Nirav").delete()
        def ben = PersonC.get(1)
        def nirav = PersonC.get(2)

        then:
        nirav.userName == "Nirav" && nirav.deleted
        ben.userName == "Ben" && ben.deleted
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
class PersonB implements LogicalDelete {
    String userName

    String toString() {
        "$userName"
    }
}
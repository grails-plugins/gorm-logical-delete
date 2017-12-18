package gorm.logical.delete

import grails.gorm.transactions.Rollback
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

/**
 * This test suite focuses on the behavior of dynamic finders in collaboration with the PreQuery Listener
 */
class DynamicFindersSpec extends Specification implements DomainUnitTest<Person> {

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
        assert Person.count() == 3
        Person.findByUserName("Ben").delete()
        Person.findByUserName("Nirav").delete()
        List<Person> results = Person.findAll()

        then: "we should only get those not logically deleted"
        results.size() == 1
        results[0].userName == 'Jeff'

        // list() calll
        when:
        results.clear()
        results = Person.list()

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
        assert Person.count() == 3
        Person.findByUserName("Ben").delete()
        Person.findByUserName("Nirav").delete()
        Person result1 = Person.findByUserName("Ben")
        Person result2 = Person.findByUserName("Nirav")

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
        assert Person.count() == 3
        Person.findByUserName("Ben").delete()
        Person.findByUserName("Nirav").delete()
        List<Person> results = Person.findAllByDeleted(true)

        then: "we should not get any because these are logically deleted"
        results.size() == 0
        results.clear()

        when:
        results = Person.findAllByDeleted(false)

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
        assert Person.count() == 3
        Person.findByUserName("Ben").delete()
        Person.findByUserName("Nirav").delete()
        def ben = Person.get(1)
        def nirav = Person.get(2)

        then:
        nirav.userName == "Nirav" && nirav.deleted
        ben.userName == "Ben" && ben.deleted
    }

    /********************* setup *****************************/

    private List<Person> createUsers() {
        def ben = new Person(userName: "Ben").save(flush: true)
        def nirav = new Person(userName: "Nirav").save(flush: true)
        def jeff = new Person(userName: "Jeff").save(flush: true)
        [ben, nirav, jeff]
    }
}
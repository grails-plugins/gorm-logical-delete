package gorm.logical.delete

import gorm.logical.delete.test.Person4
import gorm.logical.delete.test.Person4TestData
import grails.gorm.transactions.Rollback
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class BooleanDynamicFindersSpec extends Specification implements DomainUnitTest<Person4>, Person4TestData {

    /******************* test FindAll ***********************************/

    @Rollback
    void 'test dynamic findAll hide logical deleted items'() {
        // findAll() Call
        when:
        assert Person4.count() == 3
        Person4.findByUserName("Ben").delete()
        Person4.findByUserName("Nirav").delete()
        List<Person4> results = Person4.findAll()

        then: "we should only get those not logically deleted"
        results.size() == 1
        results[0].userName == 'Jeff'

        // list() calll
        when:
        results.clear()
        results = Person4.list()

        then:
        results.size() == 1
        results[0].userName == 'Jeff'
    }

    /***************** test findBy ***************************/

    @Rollback
    void 'test dynamic findByUserName hide logical deleted items'() {
        // findByUserName() Call
        when:
        assert Person4.count() == 3
        Person4.findByUserName("Ben").delete()
        Person4.findByUserName("Nirav").delete()
        Person4 result1 = Person4.findByUserName("Ben")
        Person4 result2 = Person4.findByUserName("Nirav")

        then:  "we shouldn't get any bc it was deleted"
        !result1
        !result2
    }

    /***************** test findByDeleted ***************************/

    @Rollback
    void 'test dynamic findByDeleted hide logical deleted items'() {
        // findByDeleted() Call
        when:
        assert Person4.count() == 3
        Person4.findByUserName("Ben").delete()
        Person4.findByUserName("Nirav").delete()
        List<Person4> results = Person4.findAllByDeletedIsNotNull()

        then: "we should not get any because these are logically deleted"
        results.size() == 0
        results.clear()

        when:
        results = Person4.findAllByDeleted(null)

        then: "we should find the entity because it is not logically deleted"
        results.size() == 1
        results[0].userName == 'Jeff'
    }

    /***************** test get() ***************************/

    @Rollback
    void 'test dynamic get() finds logical deleted items'() {
        when: "when 'get()' is used, we cannot access logically deleted entities"
        assert Person4.count() == 3
        Person4.findByUserName("Ben").delete()
        Person4.findByUserName("Nirav").delete()
        final Person4 ben = Person4.get(1)
        final Person4 nirav = Person4.get(2)

        then:
        !nirav
        !ben
    }
}

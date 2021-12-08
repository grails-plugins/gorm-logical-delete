package gorm.logical.delete

import gorm.logical.delete.test.Person3
import gorm.logical.delete.test.Person3TestData
import grails.gorm.transactions.Rollback
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class StringDynamicFindersSpec extends Specification implements DomainUnitTest<Person3>, Person3TestData {

    /******************* test FindAll ***********************************/

    @Rollback
    void 'test dynamic findAll hide logical deleted items'() {
        // findAll() Call
        when:
        assert Person3.count() == 3
        Person3.findByUserName("Ben").delete('test')
        Person3.findByUserName("Nirav").delete('test')
        List<Person3> results = Person3.findAll()

        then: "we should only get those not logically deleted"
        results.size() == 1
        results[0].userName == 'Jeff'

        // list() calll
        when:
        results.clear()
        results = Person3.list()

        then:
        results.size() == 1
        results[0].userName == 'Jeff'
    }

    /***************** test findBy ***************************/

    @Rollback
    void 'test dynamic findByUserName hide logical deleted items'() {
        // findByUserName() Call
        when:
        assert Person3.count() == 3
        Person3.findByUserName("Ben").delete('test')
        Person3.findByUserName("Nirav").delete('test')
        Person3 result1 = Person3.findByUserName("Ben")
        Person3 result2 = Person3.findByUserName("Nirav")

        then:  "we shouldn't get any bc it was deleted"
        !result1
        !result2
    }

    /***************** test findByDeleted ***************************/

    @Rollback
    void 'test dynamic findByDeleted hide logical deleted items'() {
        // findByDeleted() Call
        when:
        assert Person3.count() == 3
        Person3.findByUserName("Ben").delete('test')
        Person3.findByUserName("Nirav").delete('test')
        List<Person3> results = Person3.findAllByDeleted('test')

        then: "we should not get any because these are logically deleted"
        results.size() == 0
        results.clear()

        when:
        results = Person3.findAllByDeleted(null)

        then: "we should find the entity because it is not logically deleted"
        results.size() == 1
        results[0].userName == 'Jeff'
    }

    /***************** test get() ***************************/

    @Rollback
    void 'test dynamic get() finds logical deleted items'() {
        when: "when 'get()' is used, we cannot access logically deleted entities"
        assert Person3.count() == 3
        Person3.findByUserName("Ben").delete('test')
        Person3.findByUserName("Nirav").delete('test')
        final Person3 ben = Person3.get(1)
        final Person3 nirav = Person3.get(2)

        then:
        !nirav
        !ben
    }
}

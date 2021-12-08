package gorm.logical.delete

import gorm.logical.delete.test.Person2
import gorm.logical.delete.test.Person2TestData
import grails.gorm.transactions.Rollback
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class DateDynamicFindersSpec extends Specification implements DomainUnitTest<Person2>, Person2TestData {

    /******************* test FindAll ***********************************/

    @Rollback
    void 'test dynamic findAll hide logical deleted items'() {
        // findAll() Call
        when:
        assert Person2.count() == 3
        Person2.findByUserName("Ben").delete()
        Person2.findByUserName("Nirav").delete()
        List<Person2> results = Person2.findAll()

        then: "we should only get those not logically deleted"
        results.size() == 1
        results[0].userName == 'Jeff'

        // list() calll
        when:
        results.clear()
        results = Person2.list()

        then:
        results.size() == 1
        results[0].userName == 'Jeff'
    }

    /***************** test findBy ***************************/

    @Rollback
    void 'test dynamic findByUserName hide logical deleted items'() {
        // findByUserName() Call
        when:
        assert Person2.count() == 3
        Person2.findByUserName("Ben").delete()
        Person2.findByUserName("Nirav").delete()
        Person2 result1 = Person2.findByUserName("Ben")
        Person2 result2 = Person2.findByUserName("Nirav")

        then:  "we shouldn't get any bc it was deleted"
        !result1
        !result2
    }

    /***************** test findByDeleted ***************************/

    @Rollback
    void 'test dynamic findByDeleted hide logical deleted items'() {
        // findByDeleted() Call
        when:
        assert Person2.count() == 3
        Person2.findByUserName("Ben").delete()
        Person2.findByUserName("Nirav").delete()
        List<Person2> results = Person2.findAllByDeletedIsNotNull()

        then: "we should not get any because these are logically deleted"
        results.size() == 0
        results.clear()

        when:
        results = Person2.findAllByDeleted(null)

        then: "we should find the entity because it is not logically deleted"
        results.size() == 1
        results[0].userName == 'Jeff'
    }

    /***************** test get() ***************************/

    @Rollback
    void 'test dynamic get() finds logical deleted items'() {
        when: "when 'get()' is used, we cannot access logically deleted entities"
        assert Person2.count() == 3
        Person2.findByUserName("Ben").delete()
        Person2.findByUserName("Nirav").delete()
        final Person2 ben = Person2.get(1)
        final Person2 nirav = Person2.get(2)

        then:
        !nirav
        !ben
    }
}

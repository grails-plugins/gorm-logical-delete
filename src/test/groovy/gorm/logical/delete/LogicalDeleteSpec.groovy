package gorm.logical.delete

import grails.gorm.transactions.Rollback
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

/**
 * This test suite focuses on how the deleted field in a LogicalDelete implementation gets changed from overridded delete()
 * operations. The get(
 */
class LogicalDeleteSpec extends Specification implements DomainUnitTest<Person> {

    Closure doWithSpring() { { ->
            queryListener PreQueryListener
        }
    }

    /******************* delete tests ***********************************/

    @Rollback
    void 'test logical delete flush'() {
        given:
        createPerson()

        when:
        Person p = Person.get(1)

        then:
        !p.deleted

        when:
        p.delete(flush:true)
        p.discard()
        p = Person.get(1)

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete'() {
        given:
        createPerson()

        when:
        Person p = Person.get(1)

        then:
        !p.deleted

        when:
        p.delete()
        p = Person.get(1)

        then:
        p.deleted
    }

    @Rollback
    void 'test logical hard delete'() {
        given:
        createPerson()

        when:
        Person p = Person.get(1)

        then:
        !p.deleted

        when:
        p.delete(hard: true)
        p.discard()

        then:
        Person.count() == 0
    }

    /******************* undelete tests ***********************************/

    @Rollback
    void 'test logical unDelete flush'() {
        given:
        createPerson()

        when:
        Person p = Person.get(1)
        p.delete()
        p = Person.get(1)

        then:
        p.deleted

        when:
        p.unDelete(flush: true)
        p = Person.get(1)

        then:
        !p.deleted

    }

    @Rollback
    void 'test logical unDelete'() {
        given:
        createPerson()

        when:
        Person p = Person.get(1)
        p.delete()
        p = Person.get(1)

        then:
        p.deleted

        when:
        p.unDelete()
        p = Person.get(1)

        then:
        !p.deleted

    }

    Person createPerson() {
        def person = new Person(userName: "Fred").save(flush:true)
        person
    }
}

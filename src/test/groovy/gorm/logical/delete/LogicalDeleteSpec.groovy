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
    void 'test logical delete flush - load'() {
        given:
        Person.createUsers()

        when:
        Person p = Person.get(1)

        then:
        !p.deleted

        when:
        p.delete(flush:true)
        p.discard()
        p = Person.get(1, true)

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete - proxy'() {
        given:
        Person.createUsers()

        when:
        Person p = Person.get(1)

        then:
        !p.deleted

        when:
        p.delete()
        p = Person.get(1, true)

        then:
        p.deleted
    }

    @Rollback
    void 'test logical hard delete'() {
        given:
        Person.createUsers()

        when:
        Person p = Person.get(1)

        then:
        !p.deleted

        when:
        p.delete(hard: true)
        p.discard()

        then:
        Person.count() == 2 // 2 left after one hard deleted
    }

    /******************* undelete tests ***********************************/

    @Rollback
    void 'test logical unDelete flush'() {
        given:
        Person.createUsers()

        when:
        Person p = Person.get(1)
        p.delete()
        p = Person.get(1, true)

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
        Person.createUsers()

        when:
        Person p = Person.get(1)
        p.delete()
        p = Person.get(1, true)

        then:
        p.deleted

        when:
        p.unDelete()
        p = Person.get(1)

        then:
        !p.deleted

    }
}

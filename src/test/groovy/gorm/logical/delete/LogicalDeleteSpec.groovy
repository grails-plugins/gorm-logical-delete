package gorm.logical.delete

import grails.gorm.annotation.Entity
import grails.gorm.transactions.Rollback
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

/**
 * This test suite focuses on how the deleted field in a LogicalDelete implementation gets changed from overridded delete()
 * operations. The get(
 */
class LogicalDeleteSpec extends Specification implements DomainUnitTest<PersonA> {

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
        PersonA p = PersonA.get(1)

        then:
        !p.deleted

        when:
        p.delete(flush:true)
        p.discard()
        p = PersonA.get(1)

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete'() {
        given:
        createPerson()

        when:
        PersonA p = PersonA.get(1)

        then:
        !p.deleted

        when:
        p.delete()
        p = PersonA.get(1)

        then:
        p.deleted
    }

    @Rollback
    void 'test logical hard delete'() {
        given:
        createPerson()

        when:
        PersonA p = PersonA.get(1)

        then:
        !p.deleted

        when:
        p.delete(hard: true)
        p.discard()

        then:
        PersonA.count() == 0
    }

    /******************* undelete tests ***********************************/

    @Rollback
    void 'test logical unDelete flush'() {
        given:
        createPerson()

        when:
        PersonA p = PersonA.get(1)
        p.delete()
        p = PersonA.get(1)

        then:
        p.deleted

        when:
        p.unDelete(flush: true)
        p = PersonA.get(1)

        then:
        !p.deleted

    }

    @Rollback
    void 'test logical unDelete'() {
        given:
        createPerson()

        when:
        PersonA p = PersonA.get(1)
        p.delete()
        p = PersonA.get(1)

        then:
        p.deleted

        when:
        p.unDelete()
        p = PersonA.get(1)

        then:
        !p.deleted

    }

    PersonA createPerson() {
        def person = new PersonA(userName: "Fred").save(flush:true)
        person
    }
}


/**************** GORM Entity *****************************/

@Entity
class PersonA implements LogicalDelete {
    String userName

    String toString() {
        "$userName"
    }
}

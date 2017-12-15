package gorm.logical.delete

import grails.gorm.annotation.Entity
import grails.gorm.transactions.Rollback
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

@Rollback
class LogicalDeleteSpec extends Specification implements DomainUnitTest<Person> {

    /******************* delete tests ***********************************/

    void 'test logical delete flush'() {
        given:
        new Person(userName: "Fred").save(flush:true)

        when:
        Person p = Person.first()

        then:
        !p.deleted

        when:
        p.delete(flush:true)
        p.discard()

        p = Person.first()
        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete'() {
        given:
        new Person(userName: "Fred").save(flush:true)

        when:
        Person p = Person.first()

        then:
        !p.deleted

        when:
        p.delete()
        p = Person.first()

        then:
        p.deleted
    }

    void 'test logical hard delete'() {
        given:
        new Person(userName: "Fred").save(flush:true)

        when:
        Person p = Person.first()

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
        new Person(userName: "Fred").save(flush:true)

        when:
        Person p = Person.first()
        p.delete()
        p = Person.first()

        then:
        p.deleted

        when:
        p.unDelete(flush: true)
        p = Person.first()

        then:
        !p.deleted

    }

    @Rollback
    void 'test logical unDelete'() {
        given:
        new Person(userName: "Fred").save(flush:true)

        when:
        Person p = Person.first()
        p.delete()
        p = Person.first()

        then:
        p.deleted

        when:
        p.unDelete()
        p = Person.first()

        then:
        !p.deleted

    }
}


/**************** GORM Entity *****************************/

@Entity
class Person implements LogicalDelete {
    String userName

    String toString() {
        "$userName"
    }
}

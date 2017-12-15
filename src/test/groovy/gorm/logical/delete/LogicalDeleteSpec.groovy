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

    @Rollback
    void 'test dynamic finder'() {
        given: 'a new, un-deleted person has been added'
        new Person(userName: "Fred").save(flush:true)

        when: 'a search is performed on non-deleted people'
        final List<Person> nonDeletedPersonList = Person.findAllByDeleted(false)
        Person p1 = nonDeletedPersonList.first()

        then:
        p1.userName == 'Fred'
        !p1.deleted

        when: 'a search is performed on deleted people'
        p1.delete()
        final List<Person> deletedPersonList = Person.findAllByDeleted(true)
        Person p2 = deletedPersonList.first()

        then:
        p2.userName == 'Fred'
        p2.deleted

    }

    @Rollback
    void 'test findAll'() {
        given: 'two new, un-deleted person has been added'
        new Person(userName: "Bill").save(flush:true)
        new Person(userName: "Ted").save(flush:true)

        when: 'a search is performed on non-deleted people'
        final List<Person> nonDeletedPersonList = Person.findAll()

        then:
        nonDeletedPersonList.size() == 2

        when: 'a search is performed on deleted people'
        Person p1 = nonDeletedPersonList.first()
        p1.delete()
        final List<Person> someDeletedPersonList = Person.findAll()

        then:
        someDeletedPersonList.size() == 2

        when: 'a search is performed on deleted people'
        Person p2 = nonDeletedPersonList.get(1)
        p2.delete()
        final List<Person> deletedPersonList = Person.findAll()

        then:
        deletedPersonList.size() == 2

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

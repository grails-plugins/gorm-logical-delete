package gorm.logical.delete

import gorm.logical.delete.test.Person
import gorm.logical.delete.test.PersonTestData
import grails.gorm.transactions.Rollback
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

/**
 * This test suite focuses on how the deleted field in a LogicalDelete implementation gets changed from overridded delete()
 * operations. The get(
 */
class LogicalDeleteSpec extends Specification implements DomainUnitTest<Person>, PersonTestData {

    /******************* delete tests - (w/ get) ***********************************/

    @Rollback
    void 'test logical delete flush - get'() {
        when:
        Person p = Person.get(1)

        then:
        !p.deleted

        when:
        p.delete(flush:true)
        p.discard()
        p = Person.withDeleted { Person.get(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete - get'() {
        when:
        Person p = Person.get(1)

        then:
        !p.deleted

        when:
        p.delete()
        p = Person.withDeleted { Person.get(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical hard delete - get'() {
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

    /******************* delete tests - (w/ load) ***********************************/

    @Rollback
    void 'test logical delete flush - load'() {
        when:
        Person p = Person.load(1)

        then:
        !p.deleted

        when:
        p.delete(flush:true)
        p.discard()
        p = Person.withDeleted { Person.load(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete - load'() {
        when:
        Person p = Person.load(1)

        then:
        !p.deleted

        when:
        p.delete()
        p = Person.withDeleted { Person.load(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical hard delete - load'() {
        when:
        Person p = Person.load(1)

        then:
        !p.deleted

        when:
        p.delete(hard: true)
        p.discard()

        then:
        Person.count() == 2 // 2 left after one hard deleted
    }

    /******************* delete tests - (w/ proxy) ***********************************/

    @Rollback
    void 'test logical delete flush - proxy'() {
        when:
        Person p = Person.proxy(1)

        then:
        !p.deleted

        when:
        p.delete(flush:true)
        p.discard()
        p = Person.withDeleted { Person.proxy(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete - proxy'() {
        when:
        Person p = Person.proxy(1)

        then:
        !p.deleted

        when:
        p.delete()
        p = Person.withDeleted { Person.proxy(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical hard delete - proxy'() {
        when:
        Person p = Person.proxy(1)

        then:
        !p.deleted

        when:
        p.delete(hard: true)
        p.discard()

        then:
        Person.count() == 2 // 2 left after one hard deleted
    }

    /******************* delete tests - (w/ read) ***********************************/

    @Rollback
    void 'test logical delete flush - read'() {
        when:
        Person p = Person.read(1)

        then:
        !p.deleted

        when:
        p.delete(flush:true)
        p.discard()
        p = Person.withDeleted { Person.read(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete - read'() {
        when:
        Person p = Person.read(1)

        then:
        !p.deleted

        when:
        p.delete()
        p = Person.withDeleted { Person.read(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical hard delete - read'() {
        when:
        Person p = Person.read(1)

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
        when:
        Person p = Person.get(1)
        p.delete()
        p = Person.withDeleted { Person.get(1) }

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
        when:
        Person p = Person.get(1)
        p.delete()
        p = Person.withDeleted { Person.get(1) }

        then:
        p.deleted

        when:
        p.unDelete()
        p = Person.get(1)

        then:
        !p.deleted

    }
}

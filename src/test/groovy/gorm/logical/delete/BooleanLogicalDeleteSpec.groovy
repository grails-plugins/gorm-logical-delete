package gorm.logical.delete

import gorm.logical.delete.test.Person4
import gorm.logical.delete.test.Person4TestData
import grails.gorm.transactions.Rollback
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class BooleanLogicalDeleteSpec extends Specification implements DomainUnitTest<Person4>, Person4TestData {

    /******************* delete tests - (w/ get) ***********************************/

    @Rollback
    void 'test logical delete flush - get'() {
        when:
        Person4 p = Person4.get(1)

        then:
        !p.deleted

        when:
        p.delete(flush:true)
        p.discard()
        p = Person4.withDeleted { Person4.get(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete - get'() {
        when:
        Person4 p = Person4.get(1)

        then:
        !p.deleted

        when:
        p.delete()
        p = Person4.withDeleted { Person4.get(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical hard delete - get'() {
        when:
        Person4 p = Person4.get(1)

        then:
        !p.deleted

        when:
        p.delete(hard: true)
        p.discard()

        then:
        Person4.count() == 2 // 2 left after one hard deleted
    }

    /******************* delete tests - (w/ load) ***********************************/

    @Rollback
    void 'test logical delete flush - load'() {
        when:
        Person4 p = Person4.load(1)

        then:
        !p.deleted

        when:
        p.delete(flush:true)
        p.discard()
        p = Person4.withDeleted { Person4.load(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete - load'() {
        when:
        Person4 p = Person4.load(1)

        then:
        !p.deleted

        when:
        p.delete()
        p = Person4.withDeleted { Person4.load(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical hard delete - load'() {
        when:
        Person4 p = Person4.load(1)

        then:
        !p.deleted

        when:
        p.delete(hard: true)
        p.discard()

        then:
        Person4.count() == 2 // 2 left after one hard deleted
    }

    /******************* delete tests - (w/ proxy) ***********************************/

    @Rollback
    void 'test logical delete flush - proxy'() {
        when:
        Person4 p = Person4.proxy(1)

        then:
        !p.deleted

        when:
        p.delete(flush:true)
        p.discard()
        p = Person4.withDeleted { Person4.proxy(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete - proxy'() {
        when:
        Person4 p = Person4.proxy(1)

        then:
        !p.deleted

        when:
        p.delete()
        p = Person4.withDeleted { Person4.proxy(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical hard delete - proxy'() {
        when:
        Person4 p = Person4.proxy(1)

        then:
        !p.deleted

        when:
        p.delete(hard: true)
        p.discard()

        then:
        Person4.count() == 2 // 2 left after one hard deleted
    }

    /******************* delete tests - (w/ read) ***********************************/

    @Rollback
    void 'test logical delete flush - read'() {
        when:
        Person4 p = Person4.read(1)

        then:
        !p.deleted

        when:
        p.delete(flush:true)
        p.discard()
        p = Person4.withDeleted { Person4.read(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete - read'() {
        when:
        Person4 p = Person4.read(1)

        then:
        !p.deleted

        when:
        p.delete()
        p = Person4.withDeleted { Person4.read(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical hard delete - read'() {
        when:
        Person4 p = Person4.read(1)

        then:
        !p.deleted

        when:
        p.delete(hard: true)
        p.discard()

        then:
        Person4.count() == 2 // 2 left after one hard deleted
    }

    /******************* undelete tests ***********************************/

    @Rollback
    void 'test logical unDelete flush'() {
        when:
        Person4 p = Person4.get(1)
        p.delete()
        p = Person4.withDeleted { Person4.get(1) }

        then:
        p.deleted

        when:
        p.unDelete(flush: true)
        p = Person4.get(1)

        then:
        !p.deleted

    }

    @Rollback
    void 'test logical unDelete'() {
        when:
        Person4 p = Person4.get(1)
        p.delete()
        p = Person4.withDeleted { Person4.get(1) }

        then:
        p.deleted

        when:
        p.unDelete()
        p = Person4.get(1)

        then:
        !p.deleted

    }
}

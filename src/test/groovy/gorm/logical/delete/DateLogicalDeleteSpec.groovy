package gorm.logical.delete

import gorm.logical.delete.test.Person2
import gorm.logical.delete.test.Person2TestData
import grails.gorm.transactions.Rollback
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class DateLogicalDeleteSpec extends Specification implements DomainUnitTest<Person2>, Person2TestData {

    /******************* delete tests - (w/ get) ***********************************/

    @Rollback
    void 'test logical delete flush - get'() {
        when:
        Person2 p = Person2.get(1)

        then:
        !p.deleted

        when:
        p.delete(newValue: new Date(), flush:true)
        p.discard()
        p = Person2.withDeleted { Person2.get(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete flush - get with default'() {
        when:
        Person2 p = Person2.get(1)

        then:
        !p.deleted

        when:
        p.delete(flush:true)
        p.discard()
        p = Person2.withDeleted { Person2.get(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete - get'() {
        when:
        Person2 p = Person2.get(1)

        then:
        !p.deleted

        when:
        p.delete(new Date())
        p = Person2.withDeleted { Person2.get(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete - get with default'() {
        when:
        Person2 p = Person2.get(1)

        then:
        !p.deleted

        when:
        p.delete()
        p = Person2.withDeleted { Person2.get(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical hard delete - get'() {
        when:
        Person2 p = Person2.get(1)

        then:
        !p.deleted

        when:
        p.delete(hard: true)
        p.discard()

        then:
        Person2.count() == 2 // 2 left after one hard deleted
    }

    /******************* delete tests - (w/ load) ***********************************/

    @Rollback
    void 'test logical delete flush - load'() {
        when:
        Person2 p = Person2.load(1)

        then:
        !p.deleted

        when:
        p.delete(newValue: new Date(), flush:true)
        p.discard()
        p = Person2.withDeleted { Person2.load(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete flush - load with default'() {
        when:
        Person2 p = Person2.load(1)

        then:
        !p.deleted

        when:
        p.delete(flush:true)
        p.discard()
        p = Person2.withDeleted { Person2.load(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete - load'() {
        when:
        Person2 p = Person2.load(1)

        then:
        !p.deleted

        when:
        p.delete(new Date())
        p = Person2.withDeleted { Person2.load(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete - load with default'() {
        when:
        Person2 p = Person2.load(1)

        then:
        !p.deleted

        when:
        p.delete()
        p = Person2.withDeleted { Person2.load(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical hard delete - load'() {
        when:
        Person2 p = Person2.load(1)

        then:
        !p.deleted

        when:
        p.delete(hard: true)
        p.discard()

        then:
        Person2.count() == 2 // 2 left after one hard deleted
    }

    /******************* delete tests - (w/ proxy) ***********************************/

    @Rollback
    void 'test logical delete flush - proxy'() {
        when:
        Person2 p = Person2.proxy(1)

        then:
        !p.deleted

        when:
        p.delete(newValue: new Date(), flush:true)
        p.discard()
        p = Person2.withDeleted { Person2.proxy(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete flush - proxy with default'() {
        when:
        Person2 p = Person2.proxy(1)

        then:
        !p.deleted

        when:
        p.delete(flush:true)
        p.discard()
        p = Person2.withDeleted { Person2.proxy(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete - proxy'() {
        when:
        Person2 p = Person2.proxy(1)

        then:
        !p.deleted

        when:
        p.delete(new Date())
        p = Person2.withDeleted { Person2.proxy(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete - proxy with default'() {
        when:
        Person2 p = Person2.proxy(1)

        then:
        !p.deleted

        when:
        p.delete()
        p = Person2.withDeleted { Person2.proxy(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical hard delete - proxy'() {
        when:
        Person2 p = Person2.proxy(1)

        then:
        !p.deleted

        when:
        p.delete(hard: true)
        p.discard()

        then:
        Person2.count() == 2 // 2 left after one hard deleted
    }

    /******************* delete tests - (w/ read) ***********************************/

    @Rollback
    void 'test logical delete flush - read'() {
        when:
        Person2 p = Person2.read(1)

        then:
        !p.deleted

        when:
        p.delete(newValue: new Date(), flush:true)
        p.discard()
        p = Person2.withDeleted { Person2.read(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete flush - read with default'() {
        when:
        Person2 p = Person2.read(1)

        then:
        !p.deleted

        when:
        p.delete(flush:true)
        p.discard()
        p = Person2.withDeleted { Person2.read(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete - read'() {
        when:
        Person2 p = Person2.read(1)

        then:
        !p.deleted

        when:
        p.delete(new Date())
        p = Person2.withDeleted { Person2.read(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete - read with default'() {
        when:
        Person2 p = Person2.read(1)

        then:
        !p.deleted

        when:
        p.delete()
        p = Person2.withDeleted { Person2.read(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical hard delete - read'() {
        when:
        Person2 p = Person2.read(1)

        then:
        !p.deleted

        when:
        p.delete(hard: true)
        p.discard()

        then:
        Person2.count() == 2 // 2 left after one hard deleted
    }

    /******************* undelete tests ***********************************/

    @Rollback
    void 'test logical unDelete flush'() {
        when:
        Person2 p = Person2.get(1)
        p.delete()
        p = Person2.withDeleted { Person2.get(1) }

        then:
        p.deleted

        when:
        p.unDelete(flush: true)
        p = Person2.get(1)

        then:
        !p.deleted

    }

    @Rollback
    void 'test logical unDelete'() {
        when:
        Person2 p = Person2.get(1)
        p.delete()
        p = Person2.withDeleted { Person2.get(1) }

        then:
        p.deleted

        when:
        p.unDelete()
        p = Person2.get(1)

        then:
        !p.deleted

    }
}

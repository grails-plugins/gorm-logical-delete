package gorm.logical.delete

import gorm.logical.delete.test.Person3
import gorm.logical.delete.test.Person3TestData
import grails.gorm.transactions.Rollback
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class StringLogicalDeleteSpec extends Specification implements DomainUnitTest<Person3>, Person3TestData {

    /******************* delete tests - (w/ get) ***********************************/

    @Rollback
    void 'test logical delete flush - get'() {
        when:
        Person3 p = Person3.get(1)

        then:
        !p.deleted

        when:
        p.delete(newValue: 'test', flush:true)
        p.discard()
        p = Person3.withDeleted { Person3.get(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete flush - get when default value is used'() {
        when:
        Person3 p = Person3.get(1)

        then:
        !p.deleted

        when:
        p.delete(flush:true)
        p.discard()
        p = Person3.withDeleted { Person3.get(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete - get'() {
        when:
        Person3 p = Person3.get(1)

        then:
        !p.deleted

        when:
        p.delete('test')
        p = Person3.withDeleted { Person3.get(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete - get when default value is used'() {
        when:
        Person3 p = Person3.get(1)

        then:
        !p.deleted

        when:
        p.delete()
        p = Person3.withDeleted { Person3.get(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical hard delete - get'() {
        when:
        Person3 p = Person3.get(1)

        then:
        !p.deleted

        when:
        p.delete(hard: true)
        p.discard()

        then:
        Person3.count() == 2 // 2 left after one hard deleted
    }

    /******************* delete tests - (w/ load) ***********************************/

    @Rollback
    void 'test logical delete flush - load'() {
        when:
        Person3 p = Person3.load(1)

        then:
        !p.deleted

        when:
        p.delete(newValue: 'test', flush:true)
        p.discard()
        p = Person3.withDeleted { Person3.load(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete flush - load when default is used'() {
        when:
        Person3 p = Person3.load(1)

        then:
        !p.deleted

        when:
        p.delete(flush:true)
        p.discard()
        p = Person3.withDeleted { Person3.load(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete - load'() {
        when:
        Person3 p = Person3.load(1)

        then:
        !p.deleted

        when:
        p.delete('test')
        p = Person3.withDeleted { Person3.load(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete - load when default is used'() {
        when:
        Person3 p = Person3.load(1)

        then:
        !p.deleted

        when:
        p.delete()
        p = Person3.withDeleted { Person3.load(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical hard delete - load'() {
        when:
        Person3 p = Person3.load(1)

        then:
        !p.deleted

        when:
        p.delete(hard: true)
        p.discard()

        then:
        Person3.count() == 2 // 2 left after one hard deleted
    }

    /******************* delete tests - (w/ proxy) ***********************************/

    @Rollback
    void 'test logical delete flush - proxy'() {
        when:
        Person3 p = Person3.proxy(1)

        then:
        !p.deleted

        when:
        p.delete(newValue: 'test', flush:true)
        p.discard()
        p = Person3.withDeleted { Person3.proxy(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete flush - proxy when default is used'() {
        when:
        Person3 p = Person3.proxy(1)

        then:
        !p.deleted

        when:
        p.delete(flush:true)
        p.discard()
        p = Person3.withDeleted { Person3.proxy(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete - proxy'() {
        when:
        Person3 p = Person3.proxy(1)

        then:
        !p.deleted

        when:
        p.delete('test')
        p = Person3.withDeleted { Person3.proxy(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete - proxy when default is used'() {
        when:
        Person3 p = Person3.proxy(1)

        then:
        !p.deleted

        when:
        p.delete()
        p = Person3.withDeleted { Person3.proxy(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical hard delete - proxy'() {
        when:
        Person3 p = Person3.proxy(1)

        then:
        !p.deleted

        when:
        p.delete(hard: true)
        p.discard()

        then:
        Person3.count() == 2 // 2 left after one hard deleted
    }

    /******************* delete tests - (w/ read) ***********************************/

    @Rollback
    void 'test logical delete flush - read'() {
        when:
        Person3 p = Person3.read(1)

        then:
        !p.deleted

        when:
        p.delete(newValue: 'test', flush:true)
        p.discard()
        p = Person3.withDeleted { Person3.read(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete flush - read when default is used'() {
        when:
        Person3 p = Person3.read(1)

        then:
        !p.deleted

        when:
        p.delete(flush:true)
        p.discard()
        p = Person3.withDeleted { Person3.read(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete - read'() {
        when:
        Person3 p = Person3.read(1)

        then:
        !p.deleted

        when:
        p.delete('test')
        p = Person3.withDeleted { Person3.read(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical delete - read when default is used'() {
        when:
        Person3 p = Person3.read(1)

        then:
        !p.deleted

        when:
        p.delete()
        p = Person3.withDeleted { Person3.read(1) }

        then:
        p.deleted
    }

    @Rollback
    void 'test logical hard delete - read'() {
        when:
        Person3 p = Person3.read(1)

        then:
        !p.deleted

        when:
        p.delete(hard: true)
        p.discard()

        then:
        Person3.count() == 2 // 2 left after one hard deleted
    }

    /******************* undelete tests ***********************************/

    @Rollback
    void 'test logical unDelete flush'() {
        when:
        Person3 p = Person3.get(1)
        p.delete('test')
        p = Person3.withDeleted { Person3.get(1) }

        then:
        p.deleted

        when:
        p.unDelete(flush: true)
        p = Person3.get(1)

        then:
        !p.deleted

    }

    @Rollback
    void 'test logical unDelete'() {
        when:
        Person3 p = Person3.get(1)
        p.delete('test')
        p = Person3.withDeleted { Person3.get(1) }

        then:
        p.deleted

        when:
        p.unDelete()
        p = Person3.get(1)

        then:
        !p.deleted

    }
}

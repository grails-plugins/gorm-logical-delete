package hibernate.logical.delete

import grails.gorm.annotation.Entity
import grails.gorm.transactions.Rollback
import grails.test.hibernate.HibernateSpec

@Rollback
class LogicalDeleteSpec extends HibernateSpec {

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

@Entity
class Person implements LogicalDelete {
    String userName

    String toString() {
        "$userName"
    }
}

package hibernate.logical.delete

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import spock.lang.Specification

@Rollback
@Integration
class LogicalDeleteSpec extends Specification {

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
}



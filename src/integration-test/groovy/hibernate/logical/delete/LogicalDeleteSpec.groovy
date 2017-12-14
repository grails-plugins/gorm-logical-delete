package hibernate.logical.delete

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

@Rollback
@Integration
class LogicalDeleteSpec extends Specification {

    @Autowired
    LogicalDeleteService personService

    void 'test logical delete'() {
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
    void 'test logical with transaction'() {
        given:
        new Person(userName: "Fred").save(flush:true)

        when:
        Person p = Person.first()

        then:
        !p.deleted

        when:
        personService.runDelete(p)
        p = Person.first()

        then:
        p.deleted
    }
}



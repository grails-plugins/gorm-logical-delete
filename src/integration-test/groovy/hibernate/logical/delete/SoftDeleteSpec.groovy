package hibernate.logical.delete

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

@Rollback
@Integration
class SoftDeleteSpec extends Specification {

    @Autowired
    SoftDeleteService personService

    void 'test soft delete'() {
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
    void 'test soft delete with transaction'() {
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



package gorm.logical.delete.annotations

import gorm.logical.delete.test.Person
import gorm.logical.delete.test.PersonTestData
import grails.gorm.services.Service
import grails.testing.gorm.DomainUnitTest
import grails.testing.spock.OnceBefore
import spock.lang.Shared
import spock.lang.Specification

class GormDataServiceWithDeletedTransformationSpec extends Specification implements DomainUnitTest<Person>, PersonTestData {

    @Shared
    PersonService personService

    @OnceBefore
    void setupService() {
        personService = dataStore.getService(PersonService)
    }

    void 'test method marked with @WithDeleted includes logically deleted results'() {
        when:
        List<Person> results = personService.listPeople()

        then:
        results.size() == 3

        when:
        results = personService.listPeopleWithDeleted()

        then:
        results.size() == 3

        when:
        personService.delete Person.findByUserName("Ben").id
        results = personService.listPeople()

        then:
        results.size() == 2

        when:
        results = personService.listPeopleWithDeleted()

        then:
        results.size() == 3
    }
}

// tag::personService[]
@Service(Person)
interface PersonService {

    List<Person> listPeople()

    @WithDeleted
    List<Person> listPeopleWithDeleted()

    void delete(Serializable id)
}
// end::personService[]

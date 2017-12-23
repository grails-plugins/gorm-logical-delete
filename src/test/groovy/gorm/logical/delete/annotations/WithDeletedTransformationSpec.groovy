package gorm.logical.delete.annotations

import gorm.logical.delete.test.Person
import gorm.logical.delete.test.PersonTestData
import grails.testing.gorm.DomainUnitTest
import groovy.transform.CompileStatic
import spock.lang.Specification

class WithDeletedTransformationSpec extends Specification implements DomainUnitTest<Person>, PersonTestData {

    void 'test method marked with @WithDelted includes logically deleted results'() {
        setup:
        def helper = new PersonHelper()

        when:
        List<Person> results = helper.listPeople()

        then:
        results.size() == 3

        when:
        results = helper.listPeopleWithDeleted()

        then:
        results.size() == 3

        when:
        Person.findByUserName("Ben").delete()
        results = results = helper.listPeople()

        then:
        results.size() == 2

        when:
        results = helper.listPeopleWithDeleted()

        then:
        results.size() == 3
    }
}

@CompileStatic
class PersonHelper {

    List<Person> listPeople() {
        Person.list()
    }

    @WithDeleted
    List<Person> listPeopleWithDeleted() {
        Person.list()
    }
}

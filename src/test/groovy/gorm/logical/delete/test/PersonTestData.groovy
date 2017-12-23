package gorm.logical.delete.test

import gorm.logical.delete.PreQueryListener
import org.junit.After
import org.junit.Before

trait PersonTestData {

    Closure doWithSpring() {
        { ->
            queryListener PreQueryListener
        }
    }

    @Before
    void createUsers() {
        new Person(userName: "Ben").save(flush: true)
        new Person(userName: "Nirav").save(flush: true)
        new Person(userName: "Jeff").save(flush: true)
    }

    @After
    void cleanupUsers() {
        Person.withDeleted {
            Person.list()*.delete(hard: true)
        }
    }
}

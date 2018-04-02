package gorm.logical.delete.test

import gorm.logical.delete.PreQueryListener
import org.junit.After
import org.junit.Before

trait Person3TestData {

    Closure doWithSpring() {
        { ->
            queryListener PreQueryListener
        }
    }

    @Before
    void createUsers() {
        try {
            new Person3(userName: "Ben").save(failOnError: true, flush: true)
            new Person3(userName: "Nirav").save(failOnError: true, flush: true)
            new Person3(userName: "Jeff").save(failOnError: true, flush: true)
        } catch (Exception e) {
            println e
        }
    }

    @After
    void cleanupUsers() {
        Person3.withDeleted {
            Person3.list()*.delete(hard: true)
        }
    }
}

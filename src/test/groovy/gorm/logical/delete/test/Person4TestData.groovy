package gorm.logical.delete.test

import gorm.logical.delete.PreQueryListener
import org.junit.After
import org.junit.Before

trait Person4TestData {

    Closure doWithSpring() {
        { ->
            queryListener PreQueryListener
        }
    }

    @Before
    void createUsers() {
        try {
            new Person4(userName: "Ben").save(failOnError: true, flush: true)
            new Person4(userName: "Nirav").save(failOnError: true, flush: true)
            new Person4(userName: "Jeff").save(failOnError: true, flush: true)
        } catch (Exception e) {
            println e
        }
    }

    @After
    void cleanupUsers() {
        Person4.withDeleted {
            Person4.list()*.delete(hard: true)
        }
    }

}
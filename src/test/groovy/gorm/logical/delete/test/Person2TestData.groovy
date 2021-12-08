package gorm.logical.delete.test

import gorm.logical.delete.PreQueryListener
import org.junit.After
import org.junit.Before

trait Person2TestData {

    Closure doWithSpring() {
        { ->
            queryListener PreQueryListener
        }
    }

    @Before
    void createUsers() {
        try {
            new Person2(userName: "Ben").save(failOnError: true, flush: true)
            new Person2(userName: "Nirav").save(failOnError: true, flush: true)
            new Person2(userName: "Jeff").save(failOnError: true, flush: true)
        } catch (Exception e) {
            println e
        }
    }

    @After
    void cleanupUsers() {
        Person2.withDeleted {
            Person2.list()*.delete(hard: true)
        }
    }
}
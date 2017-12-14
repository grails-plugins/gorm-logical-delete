package hibernate.logical.delete

class Person implements SoftDelete<Person> {
    String userName

    String toString() {
        "$userName"
    }

    static constraints = {
    }

}

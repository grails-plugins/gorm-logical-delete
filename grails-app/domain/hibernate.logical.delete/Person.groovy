package hibernate.logical.delete

class Person implements SoftDelete {
    String userName

    String toString() {
        "$userName"
    }

    static constraints = {
    }

}

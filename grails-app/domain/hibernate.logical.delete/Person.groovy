package hibernate.logical.delete

class Person implements LogicalDelete {
    String userName

    String toString() {
        "$userName"
    }

    static constraints = {
    }

}

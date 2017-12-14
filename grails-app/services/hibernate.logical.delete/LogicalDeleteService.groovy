package hibernate.logical.delete

import grails.gorm.transactions.Transactional

@Transactional
class LogicalDeleteService {

    void runDelete(LogicalDelete logicalDeleteObject) {
        logicalDeleteObject.delete()
    }
}

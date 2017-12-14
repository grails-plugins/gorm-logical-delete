package hibernate.logical.delete

import grails.gorm.transactions.Transactional

@Transactional
class SoftDeleteService {

    void runDelete(SoftDelete softDeleteObject) {
        softDeleteObject.delete()
    }
}

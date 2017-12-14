package hibernate.logical.delete

import groovy.transform.CompileStatic
import groovy.transform.SelfType
import org.grails.datastore.gorm.GormEntity

@CompileStatic
@SelfType(GormEntity)
trait HibernateLogicalDeleteGormEntity {

    Boolean deleted = false

    void delete() {
        this.deleted = true
        save()
    }

    void delete(Map params) {
        if (params?.physical == true) {
            params.remove('physical')
            if (params) {
                delete(params)
            } else {
                delete()
            }
        } else {
            this.deleted = true
            save(params)
        }
    }
}
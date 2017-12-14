package hibernate.logical.delete

import grails.artefact.Enhances
import groovy.transform.CompileStatic
import groovy.transform.SelfType
import org.grails.core.artefact.DomainClassArtefactHandler
import org.grails.datastore.gorm.GormEntity

@CompileStatic
@Enhances(DomainClassArtefactHandler.TYPE)
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
            if (params) {
                // not sure why this is only set if params exist...
                this.deleted = true
                save(params)
            } else {
                save()
            }
        }
    }
}
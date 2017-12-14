package hibernate.logical.delete

import grails.artefact.Enhances
import groovy.transform.CompileStatic
import groovy.transform.SelfType
import org.grails.core.artefact.DomainClassArtefactHandler
import org.grails.datastore.gorm.GormEntity

@CompileStatic
@Enhances(DomainClassArtefactHandler.TYPE)
@SelfType(GormEntity)
trait SoftDelete<T> extends GormEntity<T> {
    boolean deleted = false

    @Override
    void delete() {
        this.markDirty("deleted", true, false)
        this.deleted = true
        save()
    }

    @Override
    void delete(Map params) {
        markDirty('deleted', true, false)
        deleted = true
        save(params)
    }
}
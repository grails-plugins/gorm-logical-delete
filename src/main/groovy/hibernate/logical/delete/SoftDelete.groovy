package hibernate.logical.delete

import groovy.transform.CompileStatic
import groovy.transform.SelfType
import org.grails.datastore.gorm.GormEntity

@CompileStatic
@SelfType(GormEntity)
trait SoftDelete {
    Boolean allowQueryOfDeletedItems = false
    Boolean deleted = false

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
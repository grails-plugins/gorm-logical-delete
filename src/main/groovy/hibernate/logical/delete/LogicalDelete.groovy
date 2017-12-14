package hibernate.logical.delete

import groovy.transform.CompileStatic
import groovy.transform.SelfType
import org.grails.datastore.gorm.GormEntity

@CompileStatic
@SelfType(GormEntity)
trait LogicalDelete {
    Boolean allowQueryOfDeletedItems = false
    Boolean deleted = false

    void delete() {
        this.markDirty('deleted', true, false)
        this.deleted = true
        save()
    }

    void delete(Map params) {
        markDirty('deleted', true, false)
        deleted = true
        save(params)
    }
}

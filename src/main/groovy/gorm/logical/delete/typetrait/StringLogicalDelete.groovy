package gorm.logical.delete.typetrait

import gorm.logical.delete.basetrait.LogicalDeleteBase
import groovy.transform.CompileStatic
import org.grails.datastore.gorm.GormEntity

@CompileStatic
trait StringLogicalDelete<D> implements GormEntity<D>, LogicalDeleteBase<D> {
    String deleted = null

    void delete(String newValue) {
        this.markDirty('deleted', newValue, null)
        this.deleted = newValue
        save()
    }

    void delete(Map params) {
        if (params?.hard) {
            super.delete(params)
        } else {
            this.markDirty('deleted', params?.newValue, null)
            this.deleted = (String) params?.newValue
            save(params)
        }
    }

    void unDelete() {
        this.markDirty('deleted', null,)
        this.deleted = null
        save()
    }

    void unDelete(Map params) {
        this.markDirty('deleted', null)
        this.deleted = null
        save(params)
    }
}

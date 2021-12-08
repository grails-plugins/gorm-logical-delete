package gorm.logical.delete.typetrait

import gorm.logical.delete.basetrait.LogicalDeleteBase
import groovy.transform.CompileStatic
import org.grails.datastore.gorm.GormEntity

@CompileStatic
trait StringLogicalDelete<D> implements GormEntity<D>, LogicalDeleteBase<D> {
    String deleted = null

    void delete(String newValue = 'deleted') {
        this.markDirty('deleted', newValue, this.deleted)
        this.deleted = newValue
        save()
    }

    void delete(Map params) {
        if (params?.hard) {
            super.delete(params)
        } else {
            this.markDirty('deleted', params?.newValue, this.deleted)
            this.deleted = (String) params?.newValue ?: 'deleted'
            save(params)
        }
    }

    void unDelete() {
        this.markDirty('deleted', null, this.deleted)
        this.deleted = null
        save()
    }

    void unDelete(Map params) {
        this.markDirty('deleted', params?.newValue, this.deleted)
        this.deleted = (String) params?.newValue ?: null
        save(params)
    }
}

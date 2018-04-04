package gorm.logical.delete.typetrait

import gorm.logical.delete.basetrait.LogicalDeleteBase
import groovy.transform.CompileStatic
import org.grails.datastore.gorm.GormEntity

@CompileStatic
trait DateLogicalDelete<D> implements GormEntity<D>, LogicalDeleteBase<D> {
    Date deleted = null

    void delete(Date date = new Date()) {
        this.markDirty('deleted', date, this.deleted)
        this.deleted = date
        save()
    }

    void delete(Map params) {
        if (params?.hard) {
            super.delete(params)
        } else {
            final Date date = new Date()
            this.markDirty('deleted', params?.newValue, this.deleted)
            this.deleted = (Date) params?.newValue ?: new Date()
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
        this.deleted = (Date) params?.newValue ?: null
        save(params)
    }
}
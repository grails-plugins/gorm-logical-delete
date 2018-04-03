package gorm.logical.delete.typetrait

import gorm.logical.delete.basetrait.LogicalDeleteBase
import org.grails.datastore.gorm.GormEntity

trait DateLogicalDelete<D> implements GormEntity<D>, LogicalDeleteBase<D> {
    Date deleted = null

    void delete() {
        final Date date = new Date()
        this.markDirty('deleted', date, null)
        this.deleted = date
        save()
    }

    void delete(Map params) {
        if (params?.hard) {
            super.delete(params)
        } else {
            final Date date = new Date()
            this.markDirty('deleted', date, null)
            this.deleted = date
            save(params)
        }
    }

    void unDelete() {
        this.markDirty('deleted', null)
        this.deleted = null
        save()
    }

    void unDelete(Map params) {
        this.markDirty('deleted', null)
        this.deleted = null
        save(params)
    }
}
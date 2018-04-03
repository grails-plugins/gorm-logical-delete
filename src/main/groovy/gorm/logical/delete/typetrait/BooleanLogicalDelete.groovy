package gorm.logical.delete.typetrait

import gorm.logical.delete.basetrait.LogicalDeleteBase
import org.grails.datastore.gorm.GormEntity

trait BooleanLogicalDelete<D> implements GormEntity<D>, LogicalDeleteBase<D> {
    Boolean deleted = null

    void delete() {
        this.markDirty('deleted', true, null)
        this.deleted = true
        save()
    }

    void delete(Map params) {
        if (params?.hard) {
            super.delete(params)
        } else {
            this.markDirty('deleted', true, null)
            this.deleted = true
            save(params)
        }
    }

    void unDelete() {
        this.markDirty('deleted', null, true)
        this.deleted = null
        save()
    }

    void unDelete(Map params) {
        this.markDirty('deleted', null, true)
        this.deleted = null
        save(params)
    }
}
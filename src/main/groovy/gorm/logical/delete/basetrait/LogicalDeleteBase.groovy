package gorm.logical.delete.basetrait

import grails.gorm.DetachedCriteria
import groovy.transform.CompileStatic
import org.grails.datastore.gorm.GormEnhancer
import org.grails.datastore.gorm.GormStaticApi

import static gorm.logical.delete.PreQueryListener.IGNORE_DELETED_FILTER

@CompileStatic
trait LogicalDeleteBase<D> {
    static def deletedValue = null

    static void setDeletedValue(final newDeletedValue) {
        deletedValue = newDeletedValue
    }

    static returnDeletedValue() {
        deletedValue
    }

    static Object withDeleted(Closure closure) {
        final initialThreadLocalValue = IGNORE_DELETED_FILTER.get()
        try {
            IGNORE_DELETED_FILTER.set(true)
            return closure.call()
        } finally {
            IGNORE_DELETED_FILTER.set(initialThreadLocalValue)
        }
    }

    static D get(final Serializable id) {
        if (IGNORE_DELETED_FILTER.get()) {
            this.currentGormStaticApi().get(id)
        } else {
            new DetachedCriteria(this).build {
                eq 'id', id
                eq 'deleted', deletedValue
            }.get()
        }
    }

    static D read(final Serializable id) {
        if (IGNORE_DELETED_FILTER.get()) {
            this.currentGormStaticApi().read(id)
        } else {
            new DetachedCriteria(this).build {
                eq 'id', id
                eq 'deleted', deletedValue
            }.get()
        }
    }

    static D load(final Serializable id) {
        if (IGNORE_DELETED_FILTER.get()) {
            this.currentGormStaticApi().load(id)
        } else {
            new DetachedCriteria(this).build {
                eq 'id', id
                eq 'deleted', deletedValue
            }.get()
        }
    }

    static D proxy(final Serializable id) {
        if (IGNORE_DELETED_FILTER.get()) {
            this.currentGormStaticApi().proxy(id)
        } else {
            new DetachedCriteria(this).build {
                eq 'id', id
                eq 'deleted', deletedValue
            }.get()
        }
    }

    /** ============================================================================================
     * Private Methods:
     * ============================================================================================= */
    private static GormStaticApi<D> currentGormStaticApi() {
        (GormStaticApi<D>)GormEnhancer.findStaticApi(this)
    }
}

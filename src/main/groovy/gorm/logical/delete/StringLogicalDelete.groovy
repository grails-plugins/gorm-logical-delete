package gorm.logical.delete

import grails.gorm.DetachedCriteria
import groovy.transform.CompileStatic
import org.grails.datastore.gorm.GormEnhancer
import org.grails.datastore.gorm.GormEntity
import org.grails.datastore.gorm.GormStaticApi

import static gorm.logical.delete.PreQueryListener.IGNORE_DELETED_FILTER

@CompileStatic
trait StringLogicalDelete<D> extends GormEntity<D> {
    String deleted = null

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
                eq 'deleted', null
            }.get()
        }
    }

    static D read(final Serializable id) {
        if (IGNORE_DELETED_FILTER.get()) {
            this.currentGormStaticApi().read(id)
        } else {
            new DetachedCriteria(this).build {
                eq 'id', id
                eq 'deleted', null
            }.get()
        }
    }

    static D load(final Serializable id) {
        if (IGNORE_DELETED_FILTER.get()) {
            this.currentGormStaticApi().load(id)
        } else {
            new DetachedCriteria(this).build {
                eq 'id', id
                eq 'deleted', null
            }.get()
        }
    }

    static D proxy(final Serializable id) {
        if (IGNORE_DELETED_FILTER.get()) {
            this.currentGormStaticApi().proxy(id)
        } else {
            new DetachedCriteria(this).build {
                eq 'id', id
                eq 'deleted', null
            }.get()
        }
    }

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

    /** ============================================================================================
     * Private Methods:
     * ============================================================================================= */
    private static GormStaticApi<D> currentGormStaticApi() {
        (GormStaticApi<D>)GormEnhancer.findStaticApi(this)
    }
}

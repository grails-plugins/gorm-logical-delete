/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gorm.logical.delete

import grails.gorm.DetachedCriteria
import groovy.transform.CompileStatic
import org.grails.datastore.gorm.GormEnhancer
import org.grails.datastore.gorm.GormEntity
import org.grails.datastore.gorm.GormStaticApi

@CompileStatic
trait LogicalDelete<D> extends GormEntity<D> {
    Boolean deleted = false

    public static final ThreadLocal<Boolean> USE_PREQUERY_LISTENER = ThreadLocal.withInitial { -> true }

    static Object withDeleted(Closure closure) {
        USE_PREQUERY_LISTENER.set(false)
        def results = closure.call()
        USE_PREQUERY_LISTENER.set(true)
        results
    }

    static get(final Serializable id) {
        if (!USE_PREQUERY_LISTENER.get()) {
            this.currentGormStaticApi().get(id)
        } else {
            new DetachedCriteria(this).build {
                eq 'id', id
                eq 'deleted', false
            }.get()
        }
    }

    static read(final Serializable id) {
        if (!USE_PREQUERY_LISTENER.get()) {
            this.currentGormStaticApi().read(id)
        } else {
            new DetachedCriteria(this).build {
                eq 'id', id
                eq 'deleted', false
            }.get()
        }
    }

    static load(final Serializable id) {
        if (!USE_PREQUERY_LISTENER.get()) {
            this.currentGormStaticApi().load(id)
        } else {
            new DetachedCriteria(this).build {
                eq 'id', id
                eq 'deleted', false
            }.get()
        }
    }

    static proxy(final Serializable id) {
        if (!USE_PREQUERY_LISTENER.get()) {
            this.currentGormStaticApi().proxy(id)
        } else {
            new DetachedCriteria(this).build {
                eq 'id', id
                eq 'deleted', false
            }.get()
        }
    }

    void delete() {
        this.markDirty('deleted', true, false)
        this.deleted = true
        save()
    }

    void delete(Map params) {
        if (params?.hard) {
            super.delete(params)
        } else {
            this.markDirty('deleted', true, false)
            this.deleted = true
            save(params)
        }
    }

    void unDelete() {
        this.markDirty('deleted', false, true)
        this.deleted = false
        save()
    }

    void unDelete(Map params) {
        this.markDirty('deleted', false, true)
        this.deleted = false
        save(params)
    }

    /** ============================================================================================
     * Private Methods:
     * ============================================================================================= */
    private static GormStaticApi<D> currentGormStaticApi() {
        (GormStaticApi<D>)GormEnhancer.findStaticApi(this)
    }
}

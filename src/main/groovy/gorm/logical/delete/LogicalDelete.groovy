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

import groovy.transform.CompileStatic
import org.grails.datastore.gorm.GormEntity

@CompileStatic
trait LogicalDelete<D> extends GormEntity<D>{
    Boolean deleted = false

    void delete() {
        this.markDirty('deleted', true, false)
        this.deleted = true
        save()
    }

    void delete(Map params) {
        if (params?.hard) {
            super.delete(params)
        } else {
            markDirty('deleted', true, false)
            deleted = true
            save(params)
        }
    }

    void unDelete() {
        markDirty('deleted', false, true)
        deleted = false
        save()
    }

    void unDelete(Map params) {
        markDirty('deleted', false, true)
        deleted = false
        save(params)
    }


}

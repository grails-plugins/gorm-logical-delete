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
import groovy.util.logging.Slf4j
import org.grails.datastore.mapping.model.PersistentEntity
import org.grails.datastore.mapping.query.Query
import org.grails.datastore.mapping.query.event.PreQueryEvent
import org.springframework.context.ApplicationListener

@Slf4j
@CompileStatic
class PreQueryListener implements ApplicationListener<PreQueryEvent> {

    public static final ThreadLocal<Boolean> USE_DELETED_FILTER = ThreadLocal.withInitial { -> true }

    @Override
    void onApplicationEvent(PreQueryEvent event) {
        try {
            Query query = event.query
            PersistentEntity entity = query.entity

            if (LogicalDelete.isAssignableFrom(entity.javaClass)) {
                log.debug "This entity [${entity.javaClass}] implements logical delete"

                if (USE_DELETED_FILTER.get()) {
                    query.eq('deleted', false)
                }
            }
        } catch (Exception e) {
            log.error(e.message)
        }
    }
}

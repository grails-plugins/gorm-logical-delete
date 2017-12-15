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

    @Override
    void onApplicationEvent(PreQueryEvent event) {
        try {
            Query query = event.query
            PersistentEntity entity = query.entity

            if (LogicalDelete.isAssignableFrom(entity.javaClass)) {
                log.debug "This entity [${entity.javaClass}] implements logical delete"

                query.eq('deleted', false)
            }
        } catch (Exception e) {
            log.error(e.message)
        }
    }
}

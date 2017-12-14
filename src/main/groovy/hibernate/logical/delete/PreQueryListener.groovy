package hibernate.logical.delete

import groovy.transform.CompileStatic
import org.grails.datastore.mapping.query.event.PreQueryEvent
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Scope
@Scope("singleton")
@CompileStatic
class PreQueryListener implements ApplicationListener<PreQueryEvent> {

    @Override
    void onApplicationEvent(PreQueryEvent event) {

        if(event.query.entity.persistentPropertyNames.contains('deleted')) {
            println "This entity implements logical delete"

            event.query.eq('deleted', false )
        }
    }
}

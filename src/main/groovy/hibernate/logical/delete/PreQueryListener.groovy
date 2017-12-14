package hibernate.logical.delete

import org.grails.datastore.mapping.query.event.PreQueryEvent
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import javax.annotation.Resource

@Component("hibernateLogicalDeletePreQueryListener")
@Scope("singleton")
class PreQueryListener implements ApplicationListener<PreQueryEvent> {
    //@Resource def logicalDeleteGormEntity

    @Override
    void onApplicationEvent(PreQueryEvent event) {
        final gormEntity = event.query.entity.javaClass


//        if(logicalDeleteGormEntity[gormEntity]) {
//            if(!event.query.session.getSessionProperty(HibernateLogicalDeleteGormEntity.PHYSICAL_SESSION)) {
//                event.query.eq(
//                        logicalDeleteGormEntity[gormEntity].property,
//                        !logicalDeleteGormEntity[gormEntity].deletedState
//                )
//            }
//        }
    }

}

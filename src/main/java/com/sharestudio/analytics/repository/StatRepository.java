package com.sharestudio.analytics.repository;

import com.sharestudio.analytics.entity.EventTypes;
import com.sharestudio.analytics.entity.Stat;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatRepository extends MongoRepository<Stat,String> {

     List<Stat> findByButtonEvent(EventTypes event);

     @Query("{'buttonEvent':{ $in : ['LOGIN_SUCCESS','REGISTRATION_SUCCESS','INVITEE_REGISTRATION_SUCCESS'] },'portalId': ?0 }")
     List<Stat> findByButtonEventAccountTypeAndPortalId(String portalId);

     @Query("{'buttonEvent': ?0 , 'portalId':{ $in : ?1 } }")
     List<Stat> findByButtonEventAndPortalIdList(EventTypes event,List<String> portalList);

     List<Stat> findByPortalIdAndButtonEvent(String portalId, EventTypes event);

     @Query("{'buttonEvent': { $in: ?0 }, 'portalId' : { $in : ?1 } }")
     List<Stat> findByButtonEventListAndPortalIdList( List<EventTypes> eventList, List<String> portalList);

     @Query("{'buttonEvent': { $in: ?0 }, 'portalId' : { $in : ?1 } , 'created': { $gt : ?2 , $lt : ?3 } }")
     List<Stat> findByButtonEventListAndPortalIdListAndDateRange(List<EventTypes> eventList, List<String> portalList , String starDate , String endDate);


}

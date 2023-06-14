package com.sharestudio.analytics.repository;

import com.sharestudio.analytics.entity.Homepage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface HomepageRepository extends MongoRepository<Homepage,String> {

    List<Homepage> findByOrganizationId(String id);


    @Query("{'organizationId':{ $in: ?0 } ,'clientId':{ $in: ?1 } ,'portalId':{ $in: ?2 } }")
    List<Homepage> findByOrganizationAndClientAndPortal(List<String> organizationId,List<String> clientId,List<String> portalId);

    @Query("{'clientId':{ $in: ?0 } ,'portalId':{ $in: ?1 } }")
    List<Homepage> findByClientAndPortal(List<String> clientId,List<String> portalId);

    List<Homepage> findByClientId(String id);


}

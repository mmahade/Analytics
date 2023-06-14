package com.sharestudio.analytics.repository;

import com.sharestudio.analytics.entity.Viewer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViewerRepository extends MongoRepository<Viewer,String> {

    @Query("{ 'id' : ?0 }")
    Viewer findByViewerId(int id);

    @Query("{'stst_registered_webcastId' : { $in : ?0 } }")
    List<Viewer> findViewerByWebcastIdList (List<Integer> webcastIdList);

    @Query("{ 'email' : ?0 , 'stst_registered_webcastId' : ?1 }")
    Viewer findByEamilAndWebcastId(String email, int webcastId);

}

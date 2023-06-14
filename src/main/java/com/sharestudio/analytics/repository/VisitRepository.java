package com.sharestudio.analytics.repository;

import com.sharestudio.analytics.entity.Viewer;
import com.sharestudio.analytics.entity.Visit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VisitRepository extends MongoRepository<Visit , String> {
    @Query("{ 'id' : ?0 }")
    Visit findByVisitId(int id);

    @Query("{ 'viewerId' : ?0 }")
    List<Visit> findByViewerId(int viewerId);

    @Query("{ 'viewerId' : { $in : ?0 } }")
    List<Visit> findByViewerIdList(List<Integer> viewerIdList);

    @Query("{ 'viewerId' : { $in : ?0 } , 'startTime' : {$gt : ?1, $lt : ?2 } }")
    List<Visit> findByViewerIdListDateRange(List<Integer> viewerIdList , Date startTime , Date endTime);


}

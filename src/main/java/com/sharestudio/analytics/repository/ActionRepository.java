package com.sharestudio.analytics.repository;

import com.sharestudio.analytics.entity.Action;
import com.sharestudio.analytics.entity.Viewer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRepository extends MongoRepository<Action,String> {
    @Query("{ 'id' : ?0 }")
    Action findByActionId(int id);
}

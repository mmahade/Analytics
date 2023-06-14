package com.sharestudio.analytics.repository;

import com.sharestudio.analytics.entity.Session;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends MongoRepository<Session , String> {
}

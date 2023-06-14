package com.sharestudio.analytics.repository;

import com.sharestudio.analytics.entity.Branding;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandingRepository extends MongoRepository<Branding,String> {
    List<Branding> findByOrganizationId(String id);
}

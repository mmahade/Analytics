package com.sharestudio.analytics.repository;

import com.sharestudio.analytics.entity.Qanda;
import com.sharestudio.analytics.entity.Visit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface QandaRepository extends MongoRepository<Qanda,String> {

    @Query("{'BC_ID' : ?0 , 'email' : ?1}")
    List<Qanda> findQandaByWebcastIdAndEmail(Integer BC_ID,String email);

    @Query("{ 'BC_ID' : { $in : ?0 }, 'email' : ?1}")
    List<Qanda> findQandaByWebcastIdListAndEmail(List<Integer> webcastList,String email);

    @Query("{ 'BC_ID' : { $in : ?0 } }")
    List<Qanda> findQandaByWebcastIdList(List<Integer> webcastList);

    @Query("{ 'BC_ID' : { $in : ?0 } , 'QA_date' : {$gt : ?1 , $lt : ?2 } }")
    List<Qanda> findQandaByWebcastIdListDateRange(List<Integer> webcastList , long startTime , long endTime);

    List<Qanda> findQandaByEmail(String email);

    @Query("{ 'id' : ?0 }")
    Qanda findByQandaId(long id);
}

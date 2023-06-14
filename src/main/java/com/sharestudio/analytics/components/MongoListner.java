package com.sharestudio.analytics.components;


import com.fasterxml.jackson.core.JsonProcessingException;

import com.sharestudio.analytics.entity.Session;
import com.sharestudio.analytics.entity.Stat;
import com.sharestudio.analytics.repository.SessionRepository;
import com.sharestudio.analytics.repository.StatRepository;
import org.bson.Document;
import org.bson.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.print.Doc;


@Component
public class MongoListner {

    @Value("${attendee.location.api.url}")
    private String locationAPIUrl;

    @Value("${attendee.location.api.url.key}")
    private String locationAPIUrlKey;
    
    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    SessionRepository sessionRepository;

    Logger logger = LoggerFactory.getLogger(MongoListner.class);

    @KafkaListener(topics="mongoDb.stat.stat",groupId = "groupId2")
    void listner(JsonObject data) throws JsonProcessingException{

        try{
            Document dataDocument = Document.parse(data.toString());

            Document payload=(Document) dataDocument.get("payload");

            Document afterData = Document.parse(payload.get("after").toString());

//THIS PORTION IS FOR IP DETAILS STORE
//        String uri="http://ip-api.com/json/37.111.200.179";
            RestTemplate rest = new RestTemplate();

            Object userLocation = rest.getForObject(locationAPIUrl + afterData.get("ip") + locationAPIUrlKey, Object.class);
            afterData.append("attendees_location_info", userLocation);
            logger.info("STAT CONSUMER DATA :"+ afterData.toString());
            mongoTemplate.save(afterData,"stat");

        }catch (Exception e){
            logger.error("STAT CONSUMER :"+ e.getMessage());
        }

    }

    @KafkaListener(topics="mongoDb-data.branding.branding",groupId = "groupId5")
    void listner2(JsonObject data){
        try{
            Document dataDocument = Document.parse(data.toString());

            Document payload=(Document) dataDocument.get("payload");

            Document afterData = Document.parse(payload.get("after").toString());

            logger.info("THIS IS BRANDING :" + afterData.toString());
            mongoTemplate.save(afterData,"branding");
        }catch (Exception e){
            logger.error("branding service :" + e.getMessage());
        }



    }

    @KafkaListener(topics="mongoDb-data.homePage.home-page",groupId = "groupId6")
    void homepageListner(JsonObject data){
        try{
            Document dataDocument = Document.parse(data.toString());

            Document payload=(Document) dataDocument.get("payload");

            Document afterData = Document.parse(payload.get("after").toString());

            logger.info("HOMEPAGE DATA :" + afterData.toString());
            mongoTemplate.save(afterData,"homepage");

        }catch (Exception e){
            logger.error("HOMEPAGE CONSUMER: "+e.getMessage());
        }
    }

    @KafkaListener(topics="mongoDb-data.session.session",groupId = "groupId_S1")
    void sessionListner(JsonObject data){
        try{
            Document dataDocument = Document.parse(data.toString());

            Document payload=(Document) dataDocument.get("payload");

            Document afterData = Document.parse(payload.get("after").toString());

            logger.info("SESSION DATA :" + afterData.toString());
            mongoTemplate.save(afterData,"session");

        }catch (Exception e){
            logger.error(" SESSION : "+e.getMessage());
        }
    }

}

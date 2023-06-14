/*package com.sharestudio.analytics.components;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharestudio.analytics.entity.Attendees;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MySqlListner {

    @Autowired
    MongoTemplate mongoTemplate;

    @KafkaListener(topics="sql-data.attendees.user",groupId = "groupId1")
    void listner(String data) throws JsonProcessingException {

            ObjectMapper om=new ObjectMapper();
            JsonNode node=om.readTree(data);
            JsonNode afterData=node.path("payload").path("after");

            Attendees attendees=om.readValue(afterData.toString(),Attendees.class);

            mongoTemplate.save(attendees,"attendees");

            System.out.println(afterData.toString());


    }

}
*/
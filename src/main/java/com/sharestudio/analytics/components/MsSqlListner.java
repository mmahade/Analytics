package com.sharestudio.analytics.components;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharestudio.analytics.entity.Action;
import com.sharestudio.analytics.entity.Qanda;
import com.sharestudio.analytics.entity.Viewer;
import com.sharestudio.analytics.entity.Visit;
import com.sharestudio.analytics.repository.ActionRepository;
import com.sharestudio.analytics.repository.QandaRepository;
import com.sharestudio.analytics.repository.ViewerRepository;
import com.sharestudio.analytics.repository.VisitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MsSqlListner {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    ViewerRepository viewerRepository;

    @Autowired
    VisitRepository visitRepository;

    @Autowired
    ActionRepository actionRepository;

    @Autowired
    QandaRepository qandaRepository;

    Logger logger = LoggerFactory.getLogger(MsSqlListner.class);

    @KafkaListener(topics="MS_SQL.dbo.qanda",groupId = "groupId_2")
    void listnerQanda(String data) throws JsonProcessingException {
        try{
            ObjectMapper om=new ObjectMapper();
            JsonNode node=om.readTree(data);
            JsonNode afterData=node.path("payload").path("after");
            logger.info("QANDA DATA :"+ afterData.toString());
            Qanda qanda = om.readValue(afterData.toString(),Qanda.class);
            if(afterData.path("id").asLong() != 0){
                Qanda resQanda = qandaRepository.findByQandaId(afterData.path("id").asLong());
                if(resQanda != null){
                    qanda.set_id(resQanda.get_id());
                    qandaRepository.save(qanda);
                }
                else {
                    qandaRepository.save(qanda);
                }
            }
        }catch (Exception e){
            logger.error("QANDA ERROR : " + e.getMessage());
        }
    }

    @KafkaListener(topics="MS_SQL.dbo.qanda",groupId = "groupId_2")
    void listnerQanda_A(String data) throws JsonProcessingException {
        try{
            ObjectMapper om=new ObjectMapper();
            JsonNode node=om.readTree(data);
            JsonNode afterData=node.path("payload").path("after");
            logger.info("QANDA DATA :"+ afterData.toString());
            Qanda qanda = om.readValue(afterData.toString(),Qanda.class);
            if(afterData.path("id").asLong() != 0){
                Qanda resQanda = qandaRepository.findByQandaId(afterData.path("id").asLong());
                if(resQanda != null){
                    qanda.set_id(resQanda.get_id());
                    qandaRepository.save(qanda);
                }
                else {
                    qandaRepository.save(qanda);
                }
            }
        }catch (Exception e){
            logger.error("QANDA ERROR : " + e.getMessage());
        }

    }

    @KafkaListener(topics="MS_SQL.dbo.stst_actions",groupId = "groupId_8")
    void listnerAction(String data) throws JsonProcessingException {

        ObjectMapper om=new ObjectMapper();
        JsonNode node=om.readTree(data);
        JsonNode afterData=node.path("payload").path("after");
        Action action = om.readValue(afterData.toString(),Action.class);
        Action resAction= actionRepository.findByActionId(afterData.path("id").asInt());

        if(resAction != null){
            action.set_id(resAction.get_id());
            actionRepository.save(action);
        }
        else {
            actionRepository.save(action);
        }
    }

    @KafkaListener(topics="MS_SQL.dbo.stst_actions",groupId = "groupId_8")
    void listnerAction_A(String data) throws JsonProcessingException {
        ObjectMapper om=new ObjectMapper();
        JsonNode node=om.readTree(data);
        JsonNode afterData=node.path("payload").path("after");

        Action action = om.readValue(afterData.toString(),Action.class);
        Action resAction= actionRepository.findByActionId(afterData.path("id").asInt());
        if(resAction != null){
            action.set_id(resAction.get_id());
            actionRepository.save(action);
        }
        else {
            actionRepository.save(action);
        }
    }

    @KafkaListener(topics="MS_SQL.dbo.stst_viewers",groupId = "groupId_9")
    void listnerMS3(String data) throws JsonProcessingException {
        try{
            ObjectMapper om=new ObjectMapper();
            JsonNode node=om.readTree(data);
            JsonNode afterData=node.path("payload").path("after");
            logger.info("VIEWERS DATA: "+afterData.toString());
            Viewer viewer = om.readValue(afterData.toString(),Viewer.class);
            viewerRepository.save(viewer);
        }catch (Exception e){
            logger.error("VIEWER ERROR : "+ e.getMessage());
        }

    }

    @KafkaListener(topics="MS_SQL.dbo.stst_viewers",groupId = "groupId_9")
    void listnerMS3A(String data) throws JsonProcessingException {
        try{
            ObjectMapper om=new ObjectMapper();
            JsonNode node=om.readTree(data);
            JsonNode afterData=node.path("payload").path("after");
            logger.info("VIEWERS DATA: "+afterData.toString());
            Viewer viewer = om.readValue(afterData.toString(),Viewer.class);
            viewerRepository.save(viewer);
        }catch (Exception e){
            logger.error("VIEWER ERROR : "+ e.getMessage());
        }
    }

    @KafkaListener(topics="MS_SQL.dbo.stst_viewers",groupId = "groupId_9")
    void listnerMS3B(String data) throws JsonProcessingException {
        try{
            ObjectMapper om=new ObjectMapper();
            JsonNode node=om.readTree(data);
            JsonNode afterData=node.path("payload").path("after");
            logger.info("VIEWERS DATA: "+afterData.toString());
            Viewer viewer = om.readValue(afterData.toString(),Viewer.class);
            viewerRepository.save(viewer);
        }catch (Exception e){
            logger.error("VIEWER ERROR : "+ e.getMessage());
        }
    }

    @KafkaListener(topics="MS_SQL.dbo.stst_viewers",groupId = "groupId_9")
    void listnerMS3C(String data) throws JsonProcessingException {
        try{
            ObjectMapper om=new ObjectMapper();
            JsonNode node=om.readTree(data);
            JsonNode afterData=node.path("payload").path("after");
            logger.info("VIEWERS DATA: "+afterData.toString());
            Viewer viewer = om.readValue(afterData.toString(),Viewer.class);
            viewerRepository.save(viewer);
        }catch (Exception e){
            logger.error("VIEWER ERROR : "+ e.getMessage());
        }
    }

    @KafkaListener(topics="MS_SQL.dbo.stst_viewers",groupId = "groupId_9")
    void listnerMS3D(String data) throws JsonProcessingException {
        try{
            ObjectMapper om=new ObjectMapper();
            JsonNode node=om.readTree(data);
            JsonNode afterData=node.path("payload").path("after");
            logger.info("VIEWERS DATA: "+afterData.toString());
            Viewer viewer = om.readValue(afterData.toString(),Viewer.class);
            viewerRepository.save(viewer);
        }catch (Exception e){
            logger.error("VIEWER ERROR : "+ e.getMessage());
        }
    }


    @KafkaListener(topics="MS_SQL.dbo.stst_visits",groupId = "groupId_10")
    void listnerMS4(String data) throws JsonProcessingException {
        try {
            ObjectMapper om=new ObjectMapper();
            JsonNode node=om.readTree(data);
            JsonNode afterData=node.path("payload").path("after");
            Visit visit = om.readValue(afterData.toString(),Visit.class);
            if(afterData.path("id").asInt() != 0){
                Visit resVisit = visitRepository.findByVisitId(afterData.path("id").asInt());
                if(resVisit != null){
                    visit.set_id(resVisit.get_id());
                    logger.info("VISITS DATA: "+afterData.toString());
                    visitRepository.save(visit);
                }
                else {
                    logger.info("VISITS DATA: "+afterData.toString());
                    visitRepository.save(visit);
                }
            }
        }catch (Exception e){
            logger.error("VISITS ERROR :"+e.getMessage());
        }

    }


    @KafkaListener(topics="MS_SQL.dbo.stst_visits",groupId = "groupId_10")
    void listnerMS4A(String data) throws JsonProcessingException {
        try {
            ObjectMapper om=new ObjectMapper();
            JsonNode node=om.readTree(data);
            JsonNode afterData=node.path("payload").path("after");
            Visit visit = om.readValue(afterData.toString(),Visit.class);
            if(afterData.path("id").asInt() != 0){
                Visit resVisit = visitRepository.findByVisitId(afterData.path("id").asInt());
                if(resVisit != null){
                    visit.set_id(resVisit.get_id());
                    logger.info("VISITS DATA: "+afterData.toString());
                    visitRepository.save(visit);
                }
                else {
                    logger.info("VISITS DATA: "+afterData.toString());
                    visitRepository.save(visit);
                }
            }
        }catch (Exception e){
            logger.error("VISITS ERROR :"+e.getMessage());
        }
    }

    @KafkaListener(topics="MS_SQL.dbo.stst_visits",groupId = "groupId_10")
    void listnerMS4B(String data) throws JsonProcessingException {
        try {
            ObjectMapper om=new ObjectMapper();
            JsonNode node=om.readTree(data);
            JsonNode afterData=node.path("payload").path("after");
            Visit visit = om.readValue(afterData.toString(),Visit.class);
            if(afterData.path("id").asInt() != 0){
                Visit resVisit = visitRepository.findByVisitId(afterData.path("id").asInt());
                if(resVisit != null){
                    visit.set_id(resVisit.get_id());
                    logger.info("VISITS DATA: "+afterData.toString());
                    visitRepository.save(visit);
                }
                else {
                    logger.info("VISITS DATA: "+afterData.toString());
                    visitRepository.save(visit);
                }
            }
        }catch (Exception e){
            logger.error("VISITS ERROR :"+e.getMessage());
        }
    }

    @KafkaListener(topics="MS_SQL.dbo.stst_visits",groupId = "groupId_10")
    void listnerMS4C(String data) throws JsonProcessingException {
        try {
            ObjectMapper om=new ObjectMapper();
            JsonNode node=om.readTree(data);
            JsonNode afterData=node.path("payload").path("after");
            Visit visit = om.readValue(afterData.toString(),Visit.class);
            if(afterData.path("id").asInt() != 0){
                Visit resVisit = visitRepository.findByVisitId(afterData.path("id").asInt());
                if(resVisit != null){
                    visit.set_id(resVisit.get_id());
                    logger.info("VISITS DATA: "+afterData.toString());
                    visitRepository.save(visit);
                }
                else {
                    logger.info("VISITS DATA: "+afterData.toString());
                    visitRepository.save(visit);
                }
            }
        }catch (Exception e){
            logger.error("VISITS ERROR :"+e.getMessage());
        }
    }

    @KafkaListener(topics="MS_SQL.dbo.stst_visits",groupId = "groupId_10")
    void listnerMS4D(String data) throws JsonProcessingException {
        try {
            ObjectMapper om=new ObjectMapper();
            JsonNode node=om.readTree(data);
            JsonNode afterData=node.path("payload").path("after");
            Visit visit = om.readValue(afterData.toString(),Visit.class);
            if(afterData.path("id").asInt() != 0){
                Visit resVisit = visitRepository.findByVisitId(afterData.path("id").asInt());
                if(resVisit != null){
                    visit.set_id(resVisit.get_id());
                    logger.info("VISITS DATA: "+afterData.toString());
                    visitRepository.save(visit);
                }
                else {
                    logger.info("VISITS DATA: "+afterData.toString());
                    visitRepository.save(visit);
                }
            }
        }catch (Exception e){
            logger.error("VISITS ERROR :"+e.getMessage());
        }
    }

    @KafkaListener(topics="MS_SQL.dbo.stst_visits",groupId = "groupId_10")
    void listnerMS4E(String data) throws JsonProcessingException {
        try {
            ObjectMapper om=new ObjectMapper();
            JsonNode node=om.readTree(data);
            JsonNode afterData=node.path("payload").path("after");
            Visit visit = om.readValue(afterData.toString(),Visit.class);
            if(afterData.path("id").asInt() != 0){
                Visit resVisit = visitRepository.findByVisitId(afterData.path("id").asInt());
                if(resVisit != null){
                    visit.set_id(resVisit.get_id());
                    logger.info("VISITS DATA: "+afterData.toString());
                    visitRepository.save(visit);
                }
                else {
                    logger.info("VISITS DATA: "+afterData.toString());
                    visitRepository.save(visit);
                }
            }
        }catch (Exception e){
            logger.error("VISITS ERROR :"+e.getMessage());
        }
    }



}

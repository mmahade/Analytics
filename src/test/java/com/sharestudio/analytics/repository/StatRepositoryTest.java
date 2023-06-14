package com.sharestudio.analytics.repository;

import com.sharestudio.analytics.entity.EventTypes;
import com.sharestudio.analytics.entity.Stat;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StatRepositoryTest {

    @Autowired
    private StatRepository statRepository;

    @RepeatedTest(3)
    void findByButtonEvent(RepetitionInfo repetitionInfo){

        if(repetitionInfo.getCurrentRepetition()==1){
            List<Stat> responses=statRepository.findByButtonEvent(EventTypes.LOGIN_SUCCESS);
            assertEquals(EventTypes.LOGIN_SUCCESS,responses.get(0).getButtonEvent());
            assertEquals(EventTypes.LOGIN_SUCCESS,responses.get(1).getButtonEvent());
        }
        else if(repetitionInfo.getCurrentRepetition()==2){
            List<Stat> responses=statRepository.findByButtonEvent(EventTypes.REGISTRATION_SUCCESS);
            assertEquals(EventTypes.REGISTRATION_SUCCESS,responses.get(0).getButtonEvent());
            assertEquals(EventTypes.REGISTRATION_SUCCESS,responses.get(1).getButtonEvent());
        }
        else if(repetitionInfo.getCurrentRepetition()==3){
            List<Stat> responses=statRepository.findByButtonEvent(EventTypes.INVITEE_REGISTRATION_SUCCESS);
            assertEquals(EventTypes.INVITEE_REGISTRATION_SUCCESS,responses.get(0).getButtonEvent());
            assertEquals(EventTypes.INVITEE_REGISTRATION_SUCCESS,responses.get(1).getButtonEvent());
        }

    }


}
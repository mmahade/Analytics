package com.sharestudio.analytics.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sharestudio.analytics.dto.*;
import com.sharestudio.analytics.entity.EventTypes;
import com.sharestudio.analytics.entity.Homepage;
import com.sharestudio.analytics.entity.Qanda;
import com.sharestudio.analytics.entity.Stat;
import com.sharestudio.analytics.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock
    MongoTemplate mongoTemplate;

    @Mock
    StatRepository statRepository;

    @Mock
    QandaRepository qandaRepository;

    @Mock
    HomepageRepository homepageRepository;

    @Mock
    ViewerRepository viewerRepository;

    @Mock
    VisitRepository visitRepository;


    @InjectMocks
    AnalyticsServiceImpl analyticsService;



    List<Stat> users;
    Stat testData;
    private Object a;

    List<Homepage> homepageList;

    @BeforeEach
    void setUp(){
        testData=new Stat();
        testData.setId("61d6ff84852ccc1ce02d31c4");
        testData.setPortalId("51d6ff84852ccc1ce02d3122");
        testData.setIp("1.1.1.1");

        Homepage homepageData = new Homepage();
        homepageData.setClientId("61d6ff84852ccc1ce02d31c4");
        homepageData.setPortalId("51d6ff84852ccc1ce02d3122");
        homepageData.setId("51d6ff84852ccc1ce02dfghdfh");

        homepageList = new ArrayList<>();
        homepageList.add(homepageData);

    }

    @RepeatedTest(3)
    void getUserAccountTypeReport(RepetitionInfo repetInfo) throws JsonProcessingException, ParseException {

        if(repetInfo.getCurrentRepetition()==1){
            testData.setButtonEvent(EventTypes.LOGIN_SUCCESS);
            users =new ArrayList<>();
            users.add(testData);
            given(homepageRepository.findByClientAndPortal(any(),any())).willReturn(homepageList);
            given(statRepository.findByButtonEventListAndPortalIdList(any(),any())).willReturn(users);
            ResponseEntity<List<UserAccountTypeReportDto>> response = analyticsService.getUserAccountTypeReport(null,any(),any(),null);
            assertEquals("Yes",response.getBody().get(0).getAttended());
            assertEquals("No",response.getBody().get(0).getRegistered());
            assertEquals("No",response.getBody().get(0).getInvited());
        }
        else if(repetInfo.getCurrentRepetition()==2){
            testData.setButtonEvent(EventTypes.REGISTRATION_SUCCESS);
            users =new ArrayList<>();
            users.add(testData);
            given(homepageRepository.findByClientAndPortal(any(),any())).willReturn(homepageList);
            given(statRepository.findByButtonEventListAndPortalIdList(any(),any())).willReturn(users);
            ResponseEntity<List<UserAccountTypeReportDto>> response = analyticsService.getUserAccountTypeReport(null,any(),any(),null);
            assertEquals("Yes",response.getBody().get(0).getRegistered());
            assertNotEquals("Yes",response.getBody().get(0).getInvited());
        }

        else{
            testData.setButtonEvent(EventTypes.INVITEE_REGISTRATION_SUCCESS);
            users =new ArrayList<>();
            users.add(testData);
            given(homepageRepository.findByClientAndPortal(any(),any())).willReturn(homepageList);
            given(statRepository.findByButtonEventListAndPortalIdList(any(),any())).willReturn(users);
            ResponseEntity<List<UserAccountTypeReportDto>> response=analyticsService.getUserAccountTypeReport(null,any(),any(),null);
            assertEquals("Yes",response.getBody().get(0).getInvited());
            assertNotEquals("Yes",response.getBody().get(0).getRegistered());
        }
    }

    @Test
    void getUserInfoForWebcastViewReport() throws JsonProcessingException, ParseException {


        Map<String,String> userInfo=new HashMap<>();
        Map<String,String> webcast=new HashMap<>();

        String expectedEmail="e@gmail.com";
        String expectedFirstName="John";
        userInfo.put("email",expectedEmail);
        userInfo.put("firstName",expectedFirstName);
        webcast.put("duration","50");
        webcast.put("webcastId" , "25644");
        webcast.put("legacyStatus","live");
        webcast.put("title","TEST");
        testData.setWebcast(webcast);
        testData.setUserInfo(userInfo);

        users=new ArrayList<>();
        users.add(testData);

        given(homepageRepository.findByClientAndPortal(any(),any())).willReturn(homepageList);
        given(statRepository.findByButtonEventListAndPortalIdList(any(),any())).willReturn(users);

        List<UserAccountLiveDataDto> response=analyticsService.getUserInfoForWebcastViewReport(null,any(),any(),null);

        assertFalse(response.isEmpty());
        assertEquals(response.get(0).getEmail(),expectedEmail);
        assertEquals(response.get(0).getFirstName(),expectedFirstName);

    }

    @Test
    void getUserInfoForWebcastViewReportException() throws JsonProcessingException, ParseException {

        given(homepageRepository.findByClientAndPortal(any(),any())).willReturn(homepageList);
        given(statRepository.findByButtonEventListAndPortalIdList(any(),any())).willReturn(null);

        List<UserAccountLiveDataDto> response=analyticsService.getUserInfoForWebcastViewReport(null,any(),any(),null);

        assertTrue(response.isEmpty());

    }

    @Test
    void getUserInfoForResourceReport() throws JsonProcessingException, ParseException {

        Map<String,String> testResourceDownloadData=new HashMap<>();

        String expectedEmail="e@gmail.com";
        String expectedFirstName="John";
        testResourceDownloadData.put("email",expectedEmail);
        testResourceDownloadData.put("firstName",expectedFirstName);
        testResourceDownloadData.put("resource","Test Resource File Name");
        testData.setUserInfo(testResourceDownloadData);

        users=new ArrayList<>();
        users.add(testData);

        given(homepageRepository.findByClientAndPortal(any(),any())).willReturn(homepageList);
        given(statRepository.findByButtonEventListAndPortalIdList(any(),any())).willReturn(users);


        ResourceDownloadReportDto response = analyticsService.getUserInfoForResourceReport(null,any(),any(),null);

        assertFalse(response==null);
        assertEquals(response.getUserInfoForResourceDownloadReport().get(0).getEmail(),expectedEmail);
        assertEquals(response.getUserInfoForResourceDownloadReport().get(0).getFirstName(),expectedFirstName);

    }

    @Test
    void getUserInfoForDeviceInfoReport() throws JsonProcessingException, ParseException {

        Map<String,String> testUserInfoForDeviceInfoDto = new HashMap<>();

        String expectedEmail="e@gmail.com";
        String expectedFirstName="John";
        testUserInfoForDeviceInfoDto.put("email",expectedEmail);
        testUserInfoForDeviceInfoDto.put("firstName",expectedFirstName);
        testData.setUserInfo(testUserInfoForDeviceInfoDto);

        users=new ArrayList<>();
        users.add(testData);

        given(homepageRepository.findByClientAndPortal(any(),any())).willReturn(homepageList);
        given(statRepository.findByButtonEventListAndPortalIdList(any(),any())).willReturn(users);


        List<UserInfoForDeviceInfoDto> response=analyticsService.getUserInfoForDeviceInfoReport(null,any(),any(),null);

        assertFalse(response.isEmpty());
        assertEquals(response.get(0).getEmail(),expectedEmail);
        assertEquals(response.get(0).getFirstName(),expectedFirstName);

    }

    @Test
    void getUserInfoForQuestionReport() throws JsonProcessingException, ParseException {

        Map<String,String> userInfo=new HashMap<>();
        Map<String,String> webcast=new HashMap<>();

        String expectedEmail="e@gmail.com";
        String expectedFirstName="John";
        userInfo.put("email",expectedEmail);
        userInfo.put("firstName",expectedFirstName);
        String webcastId="4450";
        webcast.put("webcastId",webcastId);
        String webcastName="Test Webcast";
        webcast.put("title",webcastName);
        testData.setWebcast(webcast);
        testData.setUserInfo(userInfo);

        users=new ArrayList<>();
        users.add(testData);

        Qanda testQanda=new Qanda();
        testQanda.setBC_ID(Integer.valueOf(webcastId));
        testQanda.setEmail(expectedEmail);
        testQanda.setQuestion("Question 1 Test");
        testQanda.setFirstname(expectedFirstName);

        List<Qanda> qandaList=new ArrayList<>();
        qandaList.add(testQanda);

        given(homepageRepository.findByClientAndPortal(any(),any())).willReturn(homepageList);
        given(statRepository.findByButtonEventListAndPortalIdList(any(),any())).willReturn(users);

        given(qandaRepository.findQandaByWebcastIdList(any())).willReturn(qandaList);

        List<UserInfoQuestionReportDto> response = analyticsService.getUserInfoForQuestionReport(null,any(),any(),null);

        assertFalse(response.isEmpty());
        assertEquals(response.get(0).getWebcastName(),webcastName);
        assertEquals(response.get(0).getEmail(),expectedEmail);
    }


    @Test
    void getDemographicsChartTest() throws ParseException, JsonProcessingException {

        Map<String,String> userInfo=new HashMap<>();
        Map<String,String> webcast=new HashMap<>();

        String expectedEmail="e@gmail.com";
        String expectedFirstName="John";
        userInfo.put("email",expectedEmail);
        userInfo.put("firstName",expectedFirstName);
        webcast.put("duration","50");
        webcast.put("title","TEST");
        testData.setWebcast(webcast);
        testData.setUserInfo(userInfo);

        users=new ArrayList<>();
        users.add(testData);

        given(homepageRepository.findByClientAndPortal(any(),any())).willReturn(homepageList);
        given(statRepository.findByButtonEventListAndPortalIdList(any(),any())).willReturn(users);

        List<DemoGraphicsDto> response=analyticsService.getDemographicsChart(null,any(),any(),null);

        assertFalse(response.isEmpty());
        assertEquals(response.get(0).getEmail(),expectedEmail);
        assertEquals(response.get(0).getFirstName(),expectedFirstName);

    }

    @Test
    void getUserInfoForWatchedVideoReportTest() throws ParseException, JsonProcessingException {

        Map<String,String> userInfo=new HashMap<>();
        Map<String,String> webcast=new HashMap<>();

        String expectedEmail="e@gmail.com";
        String expectedFirstName="John";
        userInfo.put("email",expectedEmail);
        userInfo.put("firstName",expectedFirstName);
        webcast.put("duration","50");
        webcast.put("title","TEST");
        testData.setWebcast(webcast);
        testData.setUserInfo(userInfo);
        testData.setVideoID("61d6ff84852ccc1ce02d4525");
        testData.setDuration("0.55");

        users=new ArrayList<>();
        users.add(testData);

        given(homepageRepository.findByClientAndPortal(any(),any())).willReturn(homepageList);
        given(statRepository.findByButtonEventListAndPortalIdList(any(),any())).willReturn(users);

        List<UserInfoForWatchedVideoReportDto> response = analyticsService.getUserInfoForWatchedVideoReport(null,any(),any(),null);

        assertFalse(response.isEmpty());
        assertEquals(response.get(0).getEmail(),expectedEmail);
        assertEquals(response.get(0).getFirstName(),expectedFirstName);

    }

//    @Test
//    void overviewReportsTest() throws ParseException, JsonProcessingException {
//        Map<String,String> userInfo=new HashMap<>();
//        Map<String,String> webcast=new HashMap<>();
//
//        String expectedEmail="e@gmail.com";
//        String expectedFirstName="John";
//        userInfo.put("email",expectedEmail);
//        userInfo.put("firstName",expectedFirstName);
//        webcast.put("duration","50");
//        webcast.put("title","TEST");
//        testData.setButtonEvent(EventTypes.LOGIN_SUCCESS);
//        testData.setWebcast(webcast);
//        testData.setUserInfo(userInfo);
//
//        users=new ArrayList<>();
//        users.add(testData);
//
//        given(homepageRepository.findByClientAndPortal(any(),any())).willReturn(homepageList);
//        given(statRepository.findByButtonEventListAndPortalIdList(any(),any())).willReturn(users);
//
//        List<UserInfoOverviewReportDto> response = analyticsService.overviewReports(null,any(),any(),null);
//
//        assertFalse(response.isEmpty());
//        assertEquals(response.get(0).getEmail(),expectedEmail);
//        assertEquals(response.get(0).getFirstname(),expectedFirstName);
//    }



}
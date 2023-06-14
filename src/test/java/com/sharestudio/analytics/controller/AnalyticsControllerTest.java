package com.sharestudio.analytics.controller;

import com.sharestudio.analytics.dto.*;
import com.sharestudio.analytics.repository.HomepageRepository;
import com.sharestudio.analytics.service.AnalyticsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnalyticsController.class)
public class AnalyticsControllerTest {

    @Autowired
    private MockMvc mocMvc;

    @MockBean
    public AnalyticsServiceImpl analyticsService;

    @MockBean
    public HomepageRepository homepageRepository;

    List<String> AnyList;

    List<UserAccountTypeReportDto> userList;
    UserAccountTypeReportDto testData;
    UserAccountLiveDataDto testLiveData;
    UserInfoForResourceDownloadReportDto testResourceDownloadData;
    UserInfoForDeviceInfoDto testUserInfoForDeviceInfoDto;

    @BeforeEach
    void setUp(){
          userList=new ArrayList<>();
          testData=new UserAccountTypeReportDto();
          testData.setFirstName("Rohim");
          testData.setLastName("Din");
          testData.setEmail("Rohim@Gmail.com");
          testData.setInvited("yes");
          userList.add(testData);

          testLiveData = new UserAccountLiveDataDto();
          testLiveData.setFirstName("Test1");
          testLiveData.setLastName("grbs");
          testLiveData.setEmail("a@gmail.com");
          testLiveData.setWebcastTitle("New Webcast");
          testLiveData.setVisitDuration(90);

          testResourceDownloadData = new UserInfoForResourceDownloadReportDto();
          testResourceDownloadData.setFirstName("Rohim");
          testResourceDownloadData.setLastName("Din");
          testResourceDownloadData.setEmail("Rohim@Gmail.com");
          testResourceDownloadData.setResource("Test Resource File Name");

        testUserInfoForDeviceInfoDto = new UserInfoForDeviceInfoDto();
        testUserInfoForDeviceInfoDto.setFirstName("Rohim");
        testUserInfoForDeviceInfoDto.setLastName("Din");
        testUserInfoForDeviceInfoDto.setEmail("Rohim@Gmail.com");
        testUserInfoForDeviceInfoDto.setIp("8.8.8.8");
        testUserInfoForDeviceInfoDto.setBrowser("Chrome");
        testUserInfoForDeviceInfoDto.setDeviceType("Desktop");

        AnyList = List.of("d5fg4545d4fgf5gdf","skdjljg42545454df");
    }

    @Test
    void getUserAccountTypeReport() throws Exception {

        given(analyticsService.getUserAccountTypeReport(any(),any(),any(),any())).willReturn(ResponseEntity.ok(userList));

        mocMvc.perform(get("/api/v1/UserAccountTypeReports")
                        .param("organizationIdList",String.valueOf(AnyList))
                        .param("clientIdList",String.valueOf(AnyList))
                        .param("portalIdList",String.valueOf(AnyList))
                        .param("dateRange",String.valueOf(AnyList))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        //.andExpect(jsonPath("$", hasSize(1)))
                       // .andExpect(jsonPath("$[0].firstName",is(userList.get(0).getFirstName())))
                        .andReturn();

    }


    @Test
    void getUserInfoForWebcastViewReport() throws Exception {

        List<UserAccountLiveDataDto> liveDataList=new ArrayList<>();
        liveDataList.add(testLiveData);
        given(analyticsService.getUserInfoForWebcastViewReport(any(),any(),any(),any())).willReturn(liveDataList);
        mocMvc.perform(get("/api/v1/UserInfoForWebcastView")
                        .param("organizationIdList",String.valueOf(AnyList))
                        .param("clientIdList",String.valueOf(AnyList))
                        .param("portalIdList",String.valueOf(AnyList))
                        .param("dateRange",String.valueOf(AnyList))
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName",is(liveDataList.get(0).getFirstName())))
                .andReturn();

    }

    @Test
    void getUserInfoForResourceReport() throws Exception {
        ResourceDownloadReportDto resourceDownloadReportDto = new ResourceDownloadReportDto();
        resourceDownloadReportDto.setUserInfoForResourceDownloadReport(List.of(testResourceDownloadData));
        given(analyticsService.getUserInfoForResourceReport(any(),any(),any(),any())).willReturn(resourceDownloadReportDto);

        mocMvc.perform(get("/api/v1/userInfoForResourceReport")
                .param("organizationIdList",String.valueOf(AnyList))
                .param("clientIdList",String.valueOf(AnyList))
                .param("portalIdList",String.valueOf(AnyList))
                .param("dateRange",String.valueOf(AnyList))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userInfoForResourceDownloadReport", hasSize(1)))
                .andExpect(jsonPath("$.userInfoForResourceDownloadReport[0].firstName",is(resourceDownloadReportDto.getUserInfoForResourceDownloadReport().get(0).getFirstName())))
                .andReturn();

    }

    @Test
    void getUserInfoForDeviceInfoReport() throws Exception {
        List<UserInfoForDeviceInfoDto> userInfoForDeviceInfoDtoList = new ArrayList<>();
        userInfoForDeviceInfoDtoList.add(testUserInfoForDeviceInfoDto);
        given(analyticsService.getUserInfoForDeviceInfoReport(any(),any(),any(),any())).willReturn(userInfoForDeviceInfoDtoList);

        mocMvc.perform(get("/api/v1/userInfoForDeviceInfoReport")
                        .param("organizationIdList",String.valueOf(AnyList))
                        .param("clientIdList",String.valueOf(AnyList))
                        .param("portalIdList",String.valueOf(AnyList))
                        .param("dateRange",String.valueOf(AnyList))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName",is(userInfoForDeviceInfoDtoList.get(0).getFirstName())))
                .andReturn();

    }

    @Test
    void getUserInfoForQuestionReport() throws Exception {

        List<UserInfoQuestionReportDto> userInfoQuestionList=new ArrayList<>();

        UserInfoQuestionReportDto userInfoQuestionReport=new UserInfoQuestionReportDto();

        userInfoQuestionReport.setFirstName("Test Name");
        userInfoQuestionReport.setEmail("Test@gmail.com");
        userInfoQuestionReport.setWebcastName("Test Webcast");
        userInfoQuestionReport.setQuestions(List.of("Question 1","Questin 2"));

        userInfoQuestionList.add(userInfoQuestionReport);
        given(analyticsService.getUserInfoForQuestionReport(any(),any(),any(),any())).willReturn(userInfoQuestionList);
        mocMvc.perform(get("/api/v1/UserInfoForQuestionReport")
                        .param("organizationIdList",String.valueOf(AnyList))
                        .param("clientIdList",String.valueOf(AnyList))
                        .param("portalIdList",String.valueOf(AnyList))
                        .param("dateRange",String.valueOf(AnyList))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName",is(userInfoQuestionList.get(0).getFirstName())))
                .andExpect(jsonPath("$[0].webcastName",is(userInfoQuestionList.get(0).getWebcastName())))
                .andReturn();

    }


}

package com.sharestudio.analytics.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sharestudio.analytics.dto.*;
import com.sharestudio.analytics.repository.HomepageRepository;
import com.sharestudio.analytics.service.AnalyticsService;
import com.sharestudio.analytics.service.AnalyticsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Analytics Service",  description = "Endpoints for Generating the report of statistics .")
public class AnalyticsController {
    private final AnalyticsService analyticsService;
    @Operation(summary = "Get Attendance report ", description = "Generate Attendance report using organizationIdList , Client ID List , Portal Id List and Date range", tags = { "Analytics Service" })
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "successful operation")  })
    @GetMapping("/UserAccountTypeReports")
    public ResponseEntity getUserAccountTypeReport(
            @Parameter(description = "Organization Id List for fetching data . Can be empty")
            @RequestParam(value = "organizationIdList",required = false) List<String> organizationIdList,
            @Parameter(description = "Client Id List for fetching data . Can't be empty")
            @RequestParam(value = "clientIdList") List<String> clientList,
            @Parameter(description = "Portal Id List for fetching data . Can't be empty")
            @RequestParam(value = "portalIdList") List<String> portalList,
            @Parameter(description = "Date range for fetching data between the date. Can be empty")
            @RequestParam(value = "dateRange",required = false) List<String> dateRange ) throws JsonProcessingException, ParseException {
        return analyticsService.getUserAccountTypeReport(organizationIdList,clientList,portalList,dateRange);
    }

    @Operation(summary = "Get Webcast report ", description = "Generate Webcast report using organization Id List , Client Id List , Portal Id List and Date range", tags = { "Analytics Service" })
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "successful operation")  })
    @GetMapping("/UserInfoForWebcastView")
    public List<UserAccountLiveDataDto> getUserInfoForWebcastViewReport(
            @Parameter(description = "Organization Id List for fetching data . Can be empty")
            @RequestParam(value = "organizationIdList",required = false) List<String> organizationIdList,
            @Parameter(description = "Client Id List for fetching data . Can't be empty")
            @RequestParam(value = "clientIdList") List<String> clientList,
            @Parameter(description = "Portal Id List for fetching data . Can't be empty")
            @RequestParam(value = "portalIdList") List<String> portalList,
            @Parameter(description = "Date range for fetching data between the date. Can be empty")
            @RequestParam(value = "dateRange",required = false) List<String> dateRange
    ) throws JsonProcessingException, ParseException {
        return analyticsService.getUserInfoForWebcastViewReport(organizationIdList,clientList,portalList,dateRange);
    }

    @Operation(summary = "Get Content Views & Downloads report ", description = "Generate Content Views & Downloads report using organization Id List , Client Id List , Portal Id List and Date range", tags = { "Analytics Service" })
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "successful operation")  })
    @GetMapping("/userInfoForResourceReport")
    public ResourceDownloadReportDto getUserInfoForResourceReport(
            @Parameter(description = "Organization Id List for fetching data . Can be empty")
            @RequestParam(value = "organizationIdList", required = false) List<String> organizationIdList,
            @Parameter(description = "Client Id List for fetching data . Can't be empty")
            @RequestParam(value = "clientIdList") List<String> clientIdList,
            @Parameter(description = "Portal Id List for fetching data . Can't be empty")
            @RequestParam(value = "portalIdList") List<String> portalIdList,
            @Parameter(description = "Date range for fetching data between the date. Can be empty")
            @RequestParam(value = "dateRange", required = false) List<String> dateRange
    ) throws JsonProcessingException, ParseException {
        return analyticsService.getUserInfoForResourceReport(organizationIdList, clientIdList, portalIdList, dateRange);
    }

    @Operation(summary = "Get Connectivity report ", description = "Generate Connectivity report using organization Id List , Client Id List , Portal Id List and Date range", tags = { "Analytics Service" })
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "successful operation")  })
    @GetMapping("/userInfoForDeviceInfoReport")
    public List<UserInfoForDeviceInfoDto> getUserInfoForDeviceInfoReport(
            @Parameter(description = "Organization Id List for fetching data . Can be empty")
            @RequestParam(value = "organizationIdList", required = false) List<String> organizationIdList,
            @Parameter(description = "Client Id List for fetching data . Can't be empty")
            @RequestParam(value = "clientIdList") List<String> clientIdList,
            @Parameter(description = "Portal Id List for fetching data . Can't be empty")
            @RequestParam(value = "portalIdList") List<String> portalIdList,
            @Parameter(description = "Date range for fetching data between the date. Can be empty")
            @RequestParam(value = "dateRange", required = false) List<String> dateRange) throws JsonProcessingException, ParseException {
        return analyticsService.getUserInfoForDeviceInfoReport(organizationIdList, clientIdList, portalIdList, dateRange);
    }

    @Operation(summary = "Get Q&A report ", description = "Generate Q&A report using organization Id List , Client Id List , Portal Id List and Date range", tags = { "Analytics Service" })
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "successful operation")  })
    @GetMapping("/UserInfoForQuestionReport")
    public  List<UserInfoQuestionReportDto> getUserInfoForQuestionReport(
            @Parameter(description = "Organization Id List for fetching data . Can be empty")
            @RequestParam(value = "organizationIdList",required = false) List<String> organizationIdList,
            @Parameter(description = "Client Id List for fetching data . Can't be empty")
            @RequestParam(value = "clientIdList") List<String> clientIdList,
            @Parameter(description = "Portal Id List for fetching data . Can't be empty")
            @RequestParam(value = "portalIdList") List<String> portalIdList,
            @Parameter(description = "Date range for fetching data between the date. Can be empty")
            @RequestParam(value = "dateRange",required = false) List<String> dateRange
    ) throws JsonProcessingException, ParseException {
        return analyticsService.getUserInfoForQuestionReport(organizationIdList, clientIdList, portalIdList, dateRange);
    }

    @Operation(summary = "Get Demographic Chart report ", description = "Generate Demographic Chart report using organization Id List , Client Id List , Portal Id List and Date range", tags = { "Analytics Service" })
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "successful operation")  })
    @GetMapping("/demographicsChart")
    public  List<DemoGraphicsDto> getDemographicsChart(
            @Parameter(description = "Organization Id List for fetching data . Can be empty")
            @RequestParam(value = "organizationIdList", required = false) List<String> organizationIdList,
            @Parameter(description = "Client Id List for fetching data . Can't be empty")
            @RequestParam(value = "clientIdList") List<String> clientIdList,
            @Parameter(description = "Portal Id List for fetching data . Can't be empty")
            @RequestParam(value = "portalIdList") List<String> portalIdList,
            @Parameter(description = "Date range for fetching data between the date. Can be empty")
            @RequestParam(value = "dateRange", required = false) List<String> dateRange
    ) throws JsonProcessingException, ParseException {
        return analyticsService.getDemographicsChart(organizationIdList, clientIdList, portalIdList, dateRange);
    }

    @Operation(summary = "Get Graph Bar-Chart report ", description = "Generate Graph Bar-Chart report using organization Id List , Client Id List , Portal Id List and Date range", tags = { "Analytics Service" })
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "successful operation")  })
    @GetMapping("/graphBarChart")
    public GraphBarChartDto graphBarChart(
            @Parameter(description = "Organization Id List for fetching data . Can be empty")
            @RequestParam(value = "organizationIdList", required = false) List<String> organizationIdList,
            @Parameter(description = "Client Id List for fetching data . Can't be empty")
            @RequestParam(value = "clientIdList") List<String> clientIdList,
            @Parameter(description = "Portal Id List for fetching data . Can't be empty")
            @RequestParam(value = "portalIdList") List<String> portalIdList,
            @Parameter(description = "Date range for fetching data between the date. Can be empty")
            @RequestParam(value = "dateRange", required = false) List<String> dateRange
    ) throws JsonProcessingException, ParseException {
        return analyticsService.graphBarChart(organizationIdList, clientIdList, portalIdList, dateRange);
    }

    @Operation(summary = "Get Videos report ", description = "Generate Videos report using organization Id List , Client Id List , Portal Id List and Date range", tags = { "Analytics Service" })
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "successful operation")  })
    @GetMapping("/userInfoForWatchedVideoReport")
    public  List<UserInfoForWatchedVideoReportDto> getUserInfoForWatchedVideoReport(
            @Parameter(description = "Organization Id List for fetching data . Can be empty")
            @RequestParam(value = "organizationIdList", required = false) List<String> organizationIdList,
            @Parameter(description = "Client Id List for fetching data . Can't be empty")
            @RequestParam(value = "clientIdList") List<String> clientIdList,
            @Parameter(description = "Portal Id List for fetching data . Can't be empty")
            @RequestParam(value = "portalIdList") List<String> portalIdList,
            @Parameter(description = "Date range for fetching data between the date. Can be empty")
            @RequestParam(value = "dateRange", required = false) List<String> dateRange
    ) throws JsonProcessingException, ParseException {
        return analyticsService.getUserInfoForWatchedVideoReport(organizationIdList, clientIdList, portalIdList, dateRange);
    }

    @Operation(summary = "Get Overview report ", description = "Generate Overview report using organization Id List , Client Id List , Portal Id List and Date range", tags = { "Analytics Service" })
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "successful operation")  })
    @GetMapping("/userInfoOverviewReport")
    public List<UserInfoOverviewReportDto> getUserInfoOverviewReport(
            @Parameter(description = "Organization Id List for fetching data . Can be empty")
            @RequestParam(value = "organizationIdList", required = false) List<String> organizationIdList,
            @Parameter(description = "Client Id List for fetching data . Can't be empty")
            @RequestParam(value = "clientIdList") List<String> clientIdList,
            @Parameter(description = "Portal Id List for fetching data . Can't be empty")
            @RequestParam(value = "portalIdList") List<String> portalIdList,
            @Parameter(description = "Date range for fetching data between the date. Can be empty")
            @RequestParam(value = "dateRange", required = false) List<String> dateRange
    ) throws ParseException, JsonProcessingException {
        return analyticsService.overviewReports(organizationIdList, clientIdList, portalIdList, dateRange);
    }

}

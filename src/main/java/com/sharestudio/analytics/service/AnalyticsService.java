package com.sharestudio.analytics.service;

import com.sharestudio.analytics.dto.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AnalyticsService {
    ResponseEntity getUserAccountTypeReport(List<String> organizationList, List<String> clientList, List<String> portalList, List<String> dateRange);
    List<UserAccountLiveDataDto> getUserInfoForWebcastViewReport(List<String> organizationList, List<String> clientList, List<String> portalList, List<String> dateRange);
    ResourceDownloadReportDto getUserInfoForResourceReport(List<String> organizationIdList, List<String> clientIdList, List<String> portalIdList, List<String> dateRange);
    List<UserInfoForDeviceInfoDto> getUserInfoForDeviceInfoReport(List<String> organizationIdList, List<String> clientIdList, List<String> portalIdList, List<String> dateRange);
    List<UserInfoQuestionReportDto> getUserInfoForQuestionReport(List<String> organizationList, List<String> clientList, List<String> portalList, List<String> dateRange);
    List<DemoGraphicsDto> getDemographicsChart(List<String> organizationIdList, List<String> clientIdList, List<String> portalIdList, List<String> dateRange);
    GraphBarChartDto graphBarChart(List<String> organizationList, List<String> clientList, List<String> portalList, List<String> dateRange);
    List<UserInfoForWatchedVideoReportDto> getUserInfoForWatchedVideoReport(List<String> organizationIdList, List<String> clientIdList, List<String> portalIdList, List<String> dateRange);
    List<UserInfoOverviewReportDto> overviewReports(List<String> organizationIdList, List<String> clientIdList, List<String> portalIdList, List<String> dateRange);
}

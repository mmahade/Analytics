package com.sharestudio.analytics.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResourceDownloadReportDto {

    private List<UserInfoForResourceDownloadReportDto> userInfoForResourceDownloadReport;
    private DynamicColumnDto header;
}

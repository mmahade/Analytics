package com.sharestudio.analytics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Model to give response for Bar chart report")
public class GraphBarChartDto {

    @Schema(allowableValues = "Number of downloads", example = "5")
    private int totalDownloads;
    @Schema(allowableValues = "Number of Video watched", example = "10")
    private int totalVideoWatched;
    @Schema(allowableValues = "Number of Question Asked", example = "9")
    private int totalQuestionsAsked;
    @Schema(allowableValues = "Number of Live Webcast attended", example = "7")
    private int totalLiveViews;
}

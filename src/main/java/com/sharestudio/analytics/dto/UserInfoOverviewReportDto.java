package com.sharestudio.analytics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Schema(description = "Model to give response for Overview report")
public class UserInfoOverviewReportDto {

    @Schema(allowableValues = "First name of the user", example = "Robin")
    private String firstname;
    @Schema(allowableValues = "Last name of the user", example = "Devid")
    private String lastName;
    @Schema(allowableValues = "Email of the user", example = "example@gmail.com")
    private String email;
    @Schema(allowableValues = "List of question which user asked", example = "[what is this?,why?]")
    private List<String> questions;
    @Schema(allowableValues = "Date and time user registered to the portal", example = "2017-05-6T:12:33:555")
    private String dateAndTimeRegistraton;
    @Schema(allowableValues = "OverAll duration user spends on portals", example = "55")
    private String cumulativeAttendanceDuration;
    @Schema(allowableValues = "Number of portal visited", example = "5")
    private String portalVisits;
    @Schema(allowableValues = "Number of webcast attended which was live", example = "5")
    private String liveWebcastAttended;
    @Schema(allowableValues = "Number of webcast watched total", example = "10")
    private String replayWebcastWatched;
    @Schema(allowableValues = "Number of resource download by the user", example = "8")
    private String downloads;
    @Schema(allowableValues = "Number of Videos watched by the user", example = "8")
    private String videosWatched;
    @Schema(allowableValues = "Number of time homepage visited by the user", example = "150")
    private String homeVisited;
    @Schema(allowableValues = "Number of time session page visited by the user", example = "120")
    private String sessionsVisited;
    @Schema(allowableValues = "Number of time resource page visited by the user", example = "20")
    private String resourceVisited;
    @Schema(allowableValues = "Number of time agenda page visited by the user", example = "20")
    private String agendaSpeaker;
    @Schema(allowableValues = "Number of time videoLibrary page visited by the user", example = "20")
    private String videoLibraryPageVisited;

    public UserInfoOverviewReportDto(){
        downloads = "0";
        portalVisits = "0";
        liveWebcastAttended = "0";
        replayWebcastWatched = "0";
        videosWatched = "0";
        homeVisited = "0";
        sessionsVisited = "0";
        resourceVisited = "0";
        agendaSpeaker = "0";
        cumulativeAttendanceDuration="0.00";
        videoLibraryPageVisited = "0";

    }
}

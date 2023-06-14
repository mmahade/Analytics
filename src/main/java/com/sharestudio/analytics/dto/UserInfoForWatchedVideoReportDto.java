package com.sharestudio.analytics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Model to give response for Video report")
public class UserInfoForWatchedVideoReportDto {

    @Schema(allowableValues = "First name of the user", example = "Robin")
    private String firstName;
    @Schema(allowableValues = "Last name of the user", example = "Devid")
    private String lastName;
    @Schema(allowableValues = "Email of the user", example = "example@gmail.com")
    private String email;
    @Schema(allowableValues = "Name of the video user watched", example = "rainy day")
    private String videoName;
    @Schema(allowableValues = "Number of minute user watched the video", example = "5")
    private Double duration;
}

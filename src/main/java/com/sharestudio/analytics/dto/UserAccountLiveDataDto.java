package com.sharestudio.analytics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Model to give response for webcast report")
public class UserAccountLiveDataDto {

    @Schema(allowableValues = "First name of the user", example = "Robin")
    private String firstName;
    @Schema(allowableValues = "Last name of the user", example = "Devid")
    private String lastName;
    @Schema(allowableValues = "Email of the user", example = "example@gmail.com")
    private String email;
    @Schema(allowableValues = "Webcast title which user entered", example = "May day event")
    private String webcastTitle;
    @Schema(allowableValues = "How much time he spend in the watch live page", example = "25")
    private long visitDuration;
    @Schema(allowableValues = "Preview mode of the webcast if the user entered", example = "Yes")
    private String preview;
    @Schema(allowableValues = "Live mode of the webcast if the user entered", example = "Robin")
    private String live;
    @Schema(allowableValues = "onDemand mode of the webcast if the user entered", example = "Robin")
    private String onDemand;

   public UserAccountLiveDataDto(){
       this.preview = "NO";
       this.live = "NO";
       this.onDemand = "NO";
       this.visitDuration = 0;
   }
}

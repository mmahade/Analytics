package com.sharestudio.analytics.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Model for response of Connectivity report")
public class UserInfoForDeviceInfoDto {
    @Schema(allowableValues = "First name of the user",example = "Robin")
    private String firstName;
    @Schema(allowableValues = "last name of the user",example = "Devid")
    private String lastName;
    @Schema(allowableValues = "Email of the user",example = "example@gmail.com")
    private String email;
    @Schema(allowableValues = "Ip address of the user",example = "135:25:84:77")
    private String ip;
    @Schema(allowableValues = "Device used by the user",example = "mobile")
    private String deviceType;
    @Schema(allowableValues = "browser used by the user",example = "Safari")
    private String browser;
}

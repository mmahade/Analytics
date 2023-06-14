package com.sharestudio.analytics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Model to give response for Demographic chart report")
public class DemoGraphicsDto {
    @Schema(allowableValues = "First name of the user", example = "Robin")
    private String firstName;
    @Schema(allowableValues = "Last name of the user", example = "Devid")
    private String lastName;
    @Schema(allowableValues = "Email of the user", example = "example@gmail.com")
    private String email;
    private String as;
    private String city;
    private String country;
    private String countryCode;
    private String isp;
    private String lat;
    private String lon;
    private String org;
    private String query;
    private String region;
    private String regionName;
    private String status;
    private String timezone;
    private String zip;
}

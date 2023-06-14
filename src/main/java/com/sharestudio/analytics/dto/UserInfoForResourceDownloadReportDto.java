package com.sharestudio.analytics.dto;

import com.sharestudio.analytics.entity.EventTypes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Model to give response for Download report")
public class UserInfoForResourceDownloadReportDto {
    @Schema(allowableValues = "First name of the user", example = "Robin")
    private String firstName;
    @Schema(allowableValues = "Last name of the user", example = "Devid")
    private String lastName;
    @Schema(allowableValues = "Email of the user", example = "example@gmail.com")
    private String email;
    @Schema(allowableValues = "Id of the resource user download", example = "56dfghfhf45645sdfgsd")
    private String resourceId;
    @Schema(allowableValues = "Title of the resource user download", example = "example")
    private String title;
    @Schema(allowableValues = "Name of the resource user download", example = "slide")
    private String resource;
    @Schema(allowableValues = "Button Event of the document", example = "VIDEO")
    private EventTypes buttonEvent;
}

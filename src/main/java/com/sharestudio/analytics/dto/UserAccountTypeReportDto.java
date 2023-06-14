package com.sharestudio.analytics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
@Getter
@Setter
@Schema(description = "Model to give response for Attendance report")
public class UserAccountTypeReportDto {
    @Schema(allowableValues = "First name of the user", example = "Robin")
    String firstName;
    @Schema(allowableValues = "Last name of the user", example = "Devid")
    String lastName;
    @Schema(allowableValues = "Email of the user", example = "example@gmail.com")
    String email;
    @Schema(allowableValues = "If the user is invited or not", example = "Yes/No")
    String invited;
    @Schema(allowableValues = "If the user is registered user or not", example = "Yes/No")
    String registered;
    @Schema(allowableValues = "If the user is Logged in or not", example = "Yes/No")
    String attended;

    HashMap<String ,String> optionalData = new HashMap<>();

    String createdAt;

    public UserAccountTypeReportDto(){
        invited = "No";
        registered = "No";
        attended = "No";
    }

    public UserAccountTypeReportDto(String firstName, String surName, String email, String invited, String registered, String attended) {
        this.firstName = firstName;
        this.lastName = surName;
        this.email = email;
        this.invited = invited;
        this.registered = registered;
        this.attended = attended;
    }
}

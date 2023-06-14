package com.sharestudio.analytics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Schema(description = "Model to give response for Q&A report")
public class UserInfoQuestionReportDto {
	@Schema(allowableValues = "First name of the user", example = "Robin")
	private String firstName;
	@Schema(allowableValues = "Last name of the user", example = "Devid")
	private String lastName;
	@Schema(allowableValues = "Email of the user", example = "example@gmail.com")
	private String email;
	@Schema(allowableValues = "Webcast name which user entered", example = "May day event")
	private String webcastName;
	@Schema(allowableValues = "List of question which user asked", example = "[what is this?,why?]")
	private List<String> questions;

}

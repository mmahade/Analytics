package com.sharestudio.analytics.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Getter
@Setter
@Document(collection = "stat")
@Schema(description = "Model to fetch data from stat collection")
public class Stat {

    @Id
    @Schema(allowableValues = "Stat id will auto generate in db", example = "616efd6a7df4da48502f6f07")
    private String id;

    @Schema(allowableValues = "Ip address of user", example = "157.254.254.6")
    private String ip;
    @Schema(allowableValues = "path of the visited event", example = "/home")
    private String path;
    @Schema(allowableValues = "Button Event which use for separate data from each other", example = "WATCH_LIVE")
    private EventTypes buttonEvent;
    @Schema(allowableValues = "Portal id from where stat data generated", example = "616efd6a7df4da48502f6f07")
    private String portalId;

    @Schema(allowableValues = "webcast id from where stat data generated", example = "13350")
    private Object webcast;
    @Schema(allowableValues = "Information of the user", example = "email,firstName,LastName etc")
    private Object userInfo;
    private int count;

    @Schema(allowableValues = "Information of the user Ip address", example = "address,street,postcode etc")
    private Object attendees_location_info;

    @Schema(allowableValues = "Id of the resources", example = "616efd6a7df4da48502f6f07")
    private String resourceID;
    @Schema(allowableValues = "type of resources", example = "pdf/doc")
    private String resourceType;
    @Schema(allowableValues = "Name of the resources", example = "Sample.pdf")
    private String resourceName;
    private String resourceTitle;

    @Schema(allowableValues = "Info of device which user used", example = "chrome / safari,mobile/leptop ")
    private Object deviceInfo;

    @Schema(allowableValues = "Id of the video", example = "616efd6a7df4da48502f6f07")
    private String videoID;

    @Schema(allowableValues = "Name of the video", example = "wind_of_change.mp4")
    private String videoName;
    private String videoType;

    @Schema(allowableValues = "duration of watch video", example = "4.25")
    private String duration;

    @Schema(allowableValues = "When Enter to the portal", example = "2017-05-3T:55:22")
    private String loginTime;

    @Schema(allowableValues = "When Exit from the portal", example = "2017-05-3T:55:22")
    private String logoutTime;
}

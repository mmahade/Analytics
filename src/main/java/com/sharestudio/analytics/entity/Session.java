package com.sharestudio.analytics.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Document(collection = "session")
public class Session {

    @Id
    private String id;

    private String title;


    private String organizationId;


    private String legacyOrganizationId;

    private String clientId;


    private String legacyClientId;


    private String portalId;


    private String webcastId;


    private String webcastTicketId;


    private String enableChinaCDN;


    private String scheduleLiveStartDate;


    private String ondemandOverDate;


    private String duration;


    private String recording;


    private String meeting;


    private String defaultLanguage;


    private List<String> additionalLanguages;


    private String timeZone;


    private String bitRate;


    private LocalDateTime createdDate;


    private LocalDateTime updatedDate;

    private String image;


    private boolean status;

    private boolean enabled;

    private String legacyStatus;
}

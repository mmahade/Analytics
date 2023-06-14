package com.sharestudio.analytics.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Getter
@Setter
@Document(collection = "branding")
public class Branding {
    @Id
    private String id;
    private String organizationId;
    private String clientId;
    private String logo;
    private String backgroundImage;
    private String getBackgroundColor;
    private String primaryColor;
    private String secondaryColor;
    private String font;
    private String fontColor;
    private boolean published;
    private boolean liveActive;
    private boolean agendaActive;
    private boolean ondemandActive;
    private Object portalAccessRules;
    private String createdAt;
    private String updatedAt;
}



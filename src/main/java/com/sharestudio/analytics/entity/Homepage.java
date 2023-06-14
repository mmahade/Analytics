package com.sharestudio.analytics.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Getter
@Setter
@Document(collection = "homepage")
public class Homepage {

    @Id
    private String id;
    private String organizationId;
    private String clientId;
    private String portalId;
    private String eventName;
    private String subTitle;
    private String date;
    private String closingDate;
    private String eventIntroduction;
    private String homepageButton;
    private String homepageButtonLink;
    private String homePageImage;
    private String createdAt;
}

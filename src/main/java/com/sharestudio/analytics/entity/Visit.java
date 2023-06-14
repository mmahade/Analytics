package com.sharestudio.analytics.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
@Getter
@Setter
@Document(collection = "webcast_visits")
public class Visit {

    @Id
    private String _id;
    private int viewerId;
    private int id;
    private int sessionId;
    private int eventId;
    private int stst_coid;
    private int stst_csoid;
    private int stst_webcastId;
    private String stst_status;
    private Date startTime;
    private Date endTime;
    private String stst_urlReferrer;
}

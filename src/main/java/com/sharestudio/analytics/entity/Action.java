package com.sharestudio.analytics.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
@Getter
@Setter
@Document(collection = "webcast_actions")
public class Action {

    @Id
    private String _id;
    private int visitId;
    private Date startTime;
    private int id;
    private Date endTime;
    private int streamStartTime;
    private int streamEndTime;
    private String type;
    private String value;
}

package com.sharestudio.analytics.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
@Getter
@Setter
@Document(collection = "webcast_viewers")
public class Viewer {
    @Id
    private String _id;
    private String email;
    private String password;
    private String name;
    private int id;
    private String language;
    private Date stst_registered_date;
    private String stst_firstName;
    private String stst_company;
    private String stst_jobTitle;
    private String stst_phone;
    private String stst_fax;
    private String stst_address;
    private String stst_zip;
    private String stst_city;
    private String stst_country;
    private String stst_text1;
    private int stst_registered_coid;
    private int stst_registered_csoid;
    private int stst_registered_webcastId;
    private int registeredEventId;
    private boolean unsubscribe;
    private boolean banned;
}

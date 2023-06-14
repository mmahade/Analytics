package com.sharestudio.analytics.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Getter
@Setter
@Document(collection = "attendees")
public class Attendees {

    @Id
    private String id;
    private String account_type;
    private String created;
    private String email;
    private String first_name;
    private String invitee_email_status;
    private String last_name;
    private String optional_fields;
    private String password;
    private String portal_id;
    private String portal_name;
    private String updated;
}

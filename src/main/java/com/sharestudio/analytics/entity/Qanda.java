package com.sharestudio.analytics.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "qanda")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Qanda {
	@Id
	private String _id;
	private int BC_ID;
	private int CO_ID;
	private int CSO_ID;
	private int state;
	private long QA_date;
	private String question;
	private String firstname;
	private String lastname;
	private String jobtitle;
	private String company;
	private String country;
	private int regid;
	private String email;
	private String category;
	private String labels;
	private String answer;
	private boolean repliedOne;
	private boolean repliedToAll;
	private boolean answered;

	private long id;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public int getBC_ID() {
		return BC_ID;
	}

	@JsonProperty("BC_ID")
	public void setBC_ID(int BC_ID) {
		this.BC_ID = BC_ID;
	}


	public int getCO_ID() {
		return CO_ID;
	}

	public void setCO_ID(int CO_ID) {
		this.CO_ID = CO_ID;
	}

	@JsonProperty("CO_ID")
	public int getCSO_ID() {
		return CSO_ID;
	}

	@JsonProperty("CSO_ID")
	public void setCSO_ID(int CSO_ID) {
		this.CSO_ID = CSO_ID;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public long getQA_date() {
		return QA_date;
	}

	@JsonProperty("QA_date")
	public void setQA_date(long QA_date) {
		this.QA_date = QA_date;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getJobtitle() {
		return jobtitle;
	}

	public void setJobtitle(String jobtitle) {
		this.jobtitle = jobtitle;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getRegid() {
		return regid;
	}

	public void setRegid(int regid) {
		this.regid = regid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getLabels() {
		return labels;
	}

	public void setLabels(String labels) {
		this.labels = labels;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public boolean isRepliedOne() {
		return repliedOne;
	}

	public void setRepliedOne(boolean repliedOne) {
		this.repliedOne = repliedOne;
	}

	public boolean isRepliedToAll() {
		return repliedToAll;
	}

	public void setRepliedToAll(boolean repliedToAll) {
		this.repliedToAll = repliedToAll;
	}

	public boolean isAnswered() {
		return answered;
	}

	public void setAnswered(boolean answered) {
		this.answered = answered;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}

package com.cb.vmss.model;

import java.io.Serializable;

public class Address implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String addId;
	private String addUserId;
	private String addFullName;
	private String addPhone;
	private String addAddress1;
	private String addAddress2;
	private String addLandmark;
	private String addCity;
	private String addZipCode;
	private String addCreatedDate;
	private String addUpdatedDate;
	private String addStatus;
	public String getAddId() {
		return addId;
	}
	public void setAddId(String addId) {
		this.addId = addId;
	}
	public String getAddUserId() {
		return addUserId;
	}
	public void setAddUserId(String addUserId) {
		this.addUserId = addUserId;
	}
	public String getAddFullName() {
		return addFullName;
	}
	public void setAddFullName(String addFullName) {
		this.addFullName = addFullName;
	}
	public String getAddPhone() {
		return addPhone;
	}
	public void setAddPhone(String addPhone) {
		this.addPhone = addPhone;
	}
	public String getAddAddress1() {
		return addAddress1;
	}
	public void setAddAddress1(String addAddress1) {
		this.addAddress1 = addAddress1;
	}
	public String getAddAddress2() {
		return addAddress2;
	}
	public void setAddAddress2(String addAddress2) {
		this.addAddress2 = addAddress2;
	}
	public String getAddLandmark() {
		return addLandmark;
	}
	public void setAddLandmark(String addLandmark) {
		this.addLandmark = addLandmark;
	}
	public String getAddCity() {
		return addCity;
	}
	public void setAddCity(String addCity) {
		this.addCity = addCity;
	}
	public String getAddZipCode() {
		return addZipCode;
	}
	public void setAddZipCode(String addZipCode) {
		this.addZipCode = addZipCode;
	}
	public String getAddCreatedDate() {
		return addCreatedDate;
	}
	public void setAddCreatedDate(String addCreatedDate) {
		this.addCreatedDate = addCreatedDate;
	}
	public String getAddUpdatedDate() {
		return addUpdatedDate;
	}
	public void setAddUpdatedDate(String addUpdatedDate) {
		this.addUpdatedDate = addUpdatedDate;
	}
	public String getAddStatus() {
		return addStatus;
	}
	public void setAddStatus(String addStatus) {
		this.addStatus = addStatus;
	}
}

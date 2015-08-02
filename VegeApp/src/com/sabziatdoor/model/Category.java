package com.sabziatdoor.model;

import java.io.Serializable;

public class Category implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String categoryId;
	private String categoryName;
	private String categoryImage;
	private String categroyDiscription;
	
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCategoryImage() {
		return categoryImage;
	}
	public void setCategoryImage(String categoryImage) {
		this.categoryImage = categoryImage;
	}
	public String getCategroyDiscription() {
		return categroyDiscription;
	}
	public void setCategroyDiscription(String categroyDiscription) {
		this.categroyDiscription = categroyDiscription;
	}
	
	
}

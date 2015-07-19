package com.cb.vmss.model;

import java.io.Serializable;

import org.json.JSONObject;

public class PreviousOrder implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String orderId;
	private String orderTotalPrice;
	private String orderDate;
	private String totalItem;
	private String jsonObject;
	
	public String getOrderId() {
		return orderId;
	}
	public String getJsonObject() {
		return jsonObject;
	}
	public void setJsonObject(String jsonObject) {
		this.jsonObject = jsonObject;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderTotalPrice() {
		return orderTotalPrice;
	}
	public void setOrderTotalPrice(String orderTotalPrice) {
		this.orderTotalPrice = orderTotalPrice;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getTotalItem() {
		return totalItem;
	}
	public void setTotalItem(String totalItem) {
		this.totalItem = totalItem;
	}
	
}

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
	private String orderDelivertyDate;
	private String orderDelivertTime;
	private String orderDate;
	private String orderTime;
	private String orderStatus;
	private String orderPromoCode;
	private String orderCouponPrice;
	private String orderSubPrice;
	private String totalItem;
	private String jsonObject;
	private String userName;
	private String addressLine1;
	private String addressLine2;
	private String addressLandmark;
	private String addressCity;
	private String addressZipCode;
	private String addressId;
	
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
	public String getOrderDelivertyDate() {
		return orderDelivertyDate;
	}
	public void setOrderDelivertyDate(String orderDelivertyDate) {
		this.orderDelivertyDate = orderDelivertyDate;
	}
	public String getOrderDelivertTime() {
		return orderDelivertTime;
	}
	public void setOrderDelivertTime(String orderDelivertTime) {
		this.orderDelivertTime = orderDelivertTime;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public String getTotalItem() {
		return totalItem;
	}
	public void setTotalItem(String totalItem) {
		this.totalItem = totalItem;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOrderPromoCode() {
		return orderPromoCode;
	}
	public void setOrderPromoCode(String orderPromoCode) {
		this.orderPromoCode = orderPromoCode;
	}
	public String getOrderCouponPrice() {
		return orderCouponPrice;
	}
	public void setOrderCouponPrice(String orderCouponPrice) {
		this.orderCouponPrice = orderCouponPrice;
	}
	public String getOrderSubPrice() {
		return orderSubPrice;
	}
	public void setOrderSubPrice(String orderSubPrice) {
		this.orderSubPrice = orderSubPrice;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAddressLine1() {
		return addressLine1;
	}
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	public String getAddressLine2() {
		return addressLine2;
	}
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	public String getAddressLandmark() {
		return addressLandmark;
	}
	public void setAddressLandmark(String addressLandmark) {
		this.addressLandmark = addressLandmark;
	}
	public String getAddressCity() {
		return addressCity;
	}
	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}
	public String getAddressZipCode() {
		return addressZipCode;
	}
	public void setAddressZipCode(String addressZipCode) {
		this.addressZipCode = addressZipCode;
	}
	public String getAddressId() {
		return addressId;
	}
	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}	
}
package com.sabziatdoor.model;

import java.io.Serializable;

public class OrderItems implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String OrdItemName;
	private String OrdItemQty;
	private String OrdItemPrice;
	public String getOrdItemName() {
		return OrdItemName;
	}
	public void setOrdItemName(String ordItemName) {
		OrdItemName = ordItemName;
	}
	public String getOrdItemQty() {
		return OrdItemQty;
	}
	public void setOrdItemQty(String ordItemQty) {
		OrdItemQty = ordItemQty;
	}
	public String getOrdItemPrice() {
		return OrdItemPrice;
	}
	public void setOrdItemPrice(String ordItemPrice) {
		OrdItemPrice = ordItemPrice;
	}
	

}

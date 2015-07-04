package com.cb.vmss.model;

import java.io.Serializable;

import android.graphics.Bitmap;

public class Product implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String productId;
	private String productName;
	private String productImage;
	private String productMainPrice;
	private String productDisplayPrice;
	private String productUnitId;
	private String categoryId;
	private String unit_key;
	private String unit_value;
	private String categoryName;
	private int productQty;
	private Bitmap productBitmap;
	
	public int getProductQty() {
		return productQty;
	}
	public void setProductQty(int productQty) {
		this.productQty = productQty;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductImage() {
		return productImage;
	}
	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}
	public String getProductMainPrice() {
		return productMainPrice;
	}
	public void setProductMainPrice(String productMainPrice) {
		this.productMainPrice = productMainPrice;
	}
	public String getProductDisplayPrice() {
		return productDisplayPrice;
	}
	public void setProductDisplayPrice(String productDisplayPrice) {
		this.productDisplayPrice = productDisplayPrice;
	}
	public String getProductUnitId() {
		return productUnitId;
	}
	public void setProductUnitId(String productUnitId) {
		this.productUnitId = productUnitId;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getUnit_key() {
		return unit_key;
	}
	public void setUnit_key(String unit_key) {
		this.unit_key = unit_key;
	}
	public String getUnit_value() {
		return unit_value;
	}
	public void setUnit_value(String unit_value) {
		this.unit_value = unit_value;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public Bitmap getProductBitmap() {
		return productBitmap;
	}
	public void setProductBitmap(Bitmap productBitmap) {
		this.productBitmap = productBitmap;
	}
}

package com.loremipsumlogistics.web.jdbc;

import java.io.Serializable;

public class Item implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private int productNumber;
private String productName;
private String productDescription;
private double retailPrice;
private int quantityOnHand;
private int categoryID;


public Item(int productNumber, String productName, String productDescription, double retailPrice2, int quantityOnHand,
		int categoryID) {
	super();
	this.productNumber = productNumber;
	this.productName = productName;
	this.productDescription = productDescription;
	this.retailPrice = retailPrice2;
	this.quantityOnHand = quantityOnHand;
	this.categoryID = categoryID;
}


public Item(String productName, String productDescription, double retailPrice, int quantityOnHand,
		int categoryID) {
	this.productName = productName;
	this.productDescription = productDescription;
	this.retailPrice = retailPrice;
	this.quantityOnHand = quantityOnHand;
	this.categoryID = categoryID;
}


public int getProductNumber() {
	return productNumber;
}


public void setProductNumber(int productNumber) {
	this.productNumber = productNumber;
}


public String getProductName() {
	return productName;
}


public void setProductName(String productName) {
	this.productName = productName;
}


public String getProductDescription() {
	return productDescription;
}


public void setProductDescription(String productDescription) {
	this.productDescription = productDescription;
}


public double getRetailPrice() {
	return retailPrice;
}


public void setRetailPrice(int retailPrice) {
	this.retailPrice = retailPrice;
}


public int getQuantityOnHand() {
	return quantityOnHand;
}


public void setQuantityOnHand(int quantityOnHand) {
	this.quantityOnHand = quantityOnHand;
}


public int getCategoryID() {
	return categoryID;
}


public void setCategoryID(int categoryID) {
	this.categoryID = categoryID;
}





}

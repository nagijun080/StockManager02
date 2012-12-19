package com.example.stockmanagerforandroid;

public class CustomHistoryList {

	private String status;
	private Integer orderId;
	private String ownerId;
	private String userId;
	private Integer totalValue;
	
	public void setStatus(String sta) {
		status = sta;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setOrderId(Integer order) {
		orderId = order;
	}
	
	public Integer getOrderId() {
		return orderId;
	}
	
	public void setOwnerId(String owner) {
		ownerId = owner;
	}
	
	public String getOwnerId() {
		return ownerId;
	}
	
	public void setUserId(String user) {
		userId = user;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setTotalValue(Integer value) {
		totalValue = value;
	}
	
	public Integer getTotalValue() {
		return totalValue;
	}
}

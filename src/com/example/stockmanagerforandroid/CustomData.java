 package com.example.stockmanagerforandroid;

public class CustomData {
	private String itemId_;
	private Integer itemImageId_;
    private String itemName_;
    private Integer itemValue_;
    private String itemData_;
 
    public void setItemId(String id) {
        itemId_ = id;
    }
 
    public String getItemId() {
        return itemId_;
    }
    
    public void setItemImageId(Integer imageId) {
    	itemImageId_ = imageId;
    }
    
    public Integer getItemImageId() {
    	return itemImageId_;
    }
 
    public void setItemName(String itemName) {
        itemName_ = itemName;
    }
 
    public String getItemName() {
        return itemName_;
    }

    public void setItemValue(Integer value) {
        itemValue_ = value;
    }
 
    public Integer getItemValue() {
        return itemValue_;
    }
 
    public void setItemData(String data) {
        itemData_ = data;
    }
 
    public String getItemData() {
        return itemData_;
    }
}

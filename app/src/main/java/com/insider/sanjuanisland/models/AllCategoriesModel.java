package com.insider.sanjuanisland.models;

/**
 *
 */
public class AllCategoriesModel {
    Integer Category_ID, Client_ID;
    String Category_Name;

    public Integer getCategory_ID() {
        return Category_ID;
    }

    public void setCategory_ID(Integer category_ID) {
        Category_ID = category_ID;
    }

    public String getCategory_Name() {
        return Category_Name;
    }

    public void setCategory_Name(String category_Name) {
        Category_Name = category_Name;
    }

    public Integer getClient_ID() {
        return Client_ID;
    }

    public void setClient_ID(Integer client_ID) {
        Client_ID = client_ID;
    }
}

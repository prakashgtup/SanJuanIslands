package com.insider.sanjuanisland.models;

/**
 *
 */
public class ClientDetailsModel {
    Integer Client_ID, Client_Status_ID, Reward_Code;
    String Client_Name, App_Name, Logo_Image, Bkg_Image, Description, Map_GPS, Map_Radius, Created,
            Updated, Message;
    boolean success;


    public String getApp_Name() {
        return App_Name;
    }

    public void setApp_Name(String app_Name) {
        App_Name = app_Name;
    }

    public String getBkg_Image() {
        return Bkg_Image;
    }

    public void setBkg_Image(String bkg_Image) {
        Bkg_Image = bkg_Image;
    }

    public Integer getClient_ID() {
        return Client_ID;
    }

    public void setClient_ID(Integer client_ID) {
        Client_ID = client_ID;
    }

    public String getClient_Name() {
        return Client_Name;
    }

    public void setClient_Name(String client_Name) {
        Client_Name = client_Name;
    }

    public Integer getClient_Status_ID() {
        return Client_Status_ID;
    }

    public void setClient_Status_ID(Integer client_Status_ID) {
        Client_Status_ID = client_Status_ID;
    }

    public String getCreated() {
        return Created;
    }

    public void setCreated(String created) {
        Created = created;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getLogo_Image() {
        return Logo_Image;
    }

    public void setLogo_Image(String logo_Image) {
        Logo_Image = logo_Image;
    }

    public String getMap_GPS() {
        return Map_GPS;
    }

    public void setMap_GPS(String map_GPS) {
        Map_GPS = map_GPS;
    }

    public String getMap_Radius() {
        return Map_Radius;
    }

    public void setMap_Radius(String map_Radius) {
        Map_Radius = map_Radius;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public Integer getReward_Code() {
        return Reward_Code;
    }

    public void setReward_Code(Integer reward_Code) {
        Reward_Code = reward_Code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getUpdated() {
        return Updated;
    }

    public void setUpdated(String updated) {
        Updated = updated;
    }
}

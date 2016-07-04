package com.insider.sanjuanisland.models;


import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 */
public class LocationsModel  implements Parcelable {
    Integer Location_ID, Client_ID, Location_Status_ID, Category_ID, Reward_Points_Earned, Location_Type,Reward_Points_Required,
    noChildLocations = 0;
    String Location_Name, Description, Map_GPS, Point_Collection_Radius, Photo, Address1, Address2, City, State, Zip,
            Country, Phone, Email, URL, Facebook_URL, Pinterest_URL, Twitter_URL, InstaGram_URL,
            Reward_Description, Created, Updated, CategoryName;

    double distance;

    public double getDistance() {
        return distance;
    }

    public Integer getNoChildLocations() {
        return noChildLocations;
    }

    public void setNoChildLocations(Integer noChildLocations) {
        this.noChildLocations = noChildLocations;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    boolean Parent_Location;

    public LocationsModel() {
        super();
    }
    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }
    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String address1) {
        Address1 = address1;
    }

    public String getAddress2() {
        return Address2;
    }

    public void setAddress2(String address2) {
        Address2 = address2;
    }

    public Integer getCategory_ID() {
        return Category_ID;
    }

    public void setCategory_ID(Integer category_ID) {
        Category_ID = category_ID;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public Integer getClient_ID() {
        return Client_ID;
    }

    public void setClient_ID(Integer client_ID) {
        Client_ID = client_ID;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
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

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFacebook_URL() {
        return Facebook_URL;
    }

    public void setFacebook_URL(String facebook_URL) {
        Facebook_URL = facebook_URL;
    }

    public String getInstaGram_URL() {
        return InstaGram_URL;
    }

    public void setInstaGram_URL(String instaGram_URL) {
        InstaGram_URL = instaGram_URL;
    }

    public Integer getLocation_ID() {
        return Location_ID;
    }

    public void setLocation_ID(Integer location_ID) {
        Location_ID = location_ID;
    }

    public String getLocation_Name() {
        return Location_Name;
    }

    public void setLocation_Name(String location_Name) {
        Location_Name = location_Name;
    }

    public Integer getLocation_Status_ID() {
        return Location_Status_ID;
    }

    public void setLocation_Status_ID(Integer location_Status_ID) {
        Location_Status_ID = location_Status_ID;
    }

    public Integer getLocation_Type() {
        return Location_Type;
    }

    public void setLocation_Type(Integer location_Type) {
        Location_Type = location_Type;
    }

    public String getMap_GPS() {
        return Map_GPS;
    }

    public void setMap_GPS(String map_GPS) {
        Map_GPS = map_GPS;
    }

    public boolean isParent_Location() {
        return Parent_Location;
    }

    public void setParent_Location(boolean parent_Location) {
        Parent_Location = parent_Location;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getPinterest_URL() {
        return Pinterest_URL;
    }

    public void setPinterest_URL(String pinterest_URL) {
        Pinterest_URL = pinterest_URL;
    }

    public String getPoint_Collection_Radius() {
        return Point_Collection_Radius;
    }

    public void setPoint_Collection_Radius(String point_Collection_Radius) {
        Point_Collection_Radius = point_Collection_Radius;
    }

    public String getReward_Description() {
        return Reward_Description;
    }

    public void setReward_Description(String reward_Description) {
        Reward_Description = reward_Description;
    }

    public Integer getReward_Points_Earned() {
        return Reward_Points_Earned;
    }

    public void setReward_Points_Earned(Integer reward_Points_Earned) {
        Reward_Points_Earned = reward_Points_Earned;
    }

    public Integer getReward_Points_Required() {
        return Reward_Points_Required;
    }

    public void setReward_Points_Required(Integer reward_Points_Required) {
        Reward_Points_Required = reward_Points_Required;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getTwitter_URL() {
        return Twitter_URL;
    }

    public void setTwitter_URL(String twitter_URL) {
        Twitter_URL = twitter_URL;
    }

    public String getUpdated() {
        return Updated;
    }

    public void setUpdated(String updated) {
        Updated = updated;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getZip() {
        return Zip;
    }

    public void setZip(String zip) {
        Zip = zip;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(Location_ID);
        dest.writeInt(Client_ID);
        dest.writeInt(Location_Status_ID);
        dest.writeInt(Category_ID);
        dest.writeInt(Reward_Points_Earned);
        dest.writeInt(Location_Type);
        dest.writeInt(Reward_Points_Required);
        dest.writeString(Location_Name);
        dest.writeString(Description);
        dest.writeString(Map_GPS);
        dest.writeString(Point_Collection_Radius);
        dest.writeString(Photo);
        dest.writeString(Address1);
        dest.writeString(Address2);
        dest.writeString(City);
        dest.writeString(State);
        dest.writeString(Zip);
        dest.writeString(Country);
        dest.writeString(Phone);
        dest.writeString(Email);
        dest.writeString(URL);
        dest.writeString(Facebook_URL);
        dest.writeString(Pinterest_URL);
        dest.writeString(Twitter_URL);
        dest.writeString(InstaGram_URL);
        dest.writeString(Reward_Description);
        dest.writeString(Created);
        dest.writeString(Updated);
        dest.writeString(CategoryName);
        if(Parent_Location) {
            dest.writeInt(1);
        }else {
            dest.writeInt(0);
        }
        dest.writeDouble(distance);
        dest.writeInt(noChildLocations);
    }

    private void readFromParcel(Parcel in) {


        Location_ID = in.readInt();
        Client_ID = in.readInt();
        Location_Status_ID = in.readInt();
        Category_ID = in.readInt();
        Reward_Points_Earned = in.readInt();
        Location_Type = in.readInt();
        Reward_Points_Required = in.readInt();
        Location_Name = in.readString();
        Description = in.readString();
        Map_GPS = in.readString();
        Point_Collection_Radius = in.readString();
        Photo = in.readString();
        Address1 = in.readString();
        Address2 = in.readString();
        City = in.readString();
        State = in.readString();
        Zip = in.readString();
        Country = in.readString();
        Phone = in.readString();
        Email = in.readString();
        URL = in.readString();
        Facebook_URL = in.readString();
        Pinterest_URL= in.readString();
        Twitter_URL= in.readString();
        InstaGram_URL= in.readString();
        Reward_Description = in.readString();
        Created = in.readString();
        Updated = in.readString();
        CategoryName = in.readString();
        if(in.readInt()==1) {
            Parent_Location = true;
        }else {
            Parent_Location =false;
        }
        distance = in.readDouble();
        noChildLocations = in.readInt();
    }

    public LocationsModel(Parcel in){
        readFromParcel(in);
    }

    public static final Creator<LocationsModel> CREATOR = new Creator<LocationsModel>() {

        @Override
        public LocationsModel createFromParcel(Parcel source) {
            return new LocationsModel(source);
        }

        @Override
        public LocationsModel[] newArray(int size) {
            return new LocationsModel[size];
        }
    };
}

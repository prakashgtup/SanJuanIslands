package com.insider.sanjuanisland.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.insider.sanjuanisland.models.AbtModel;
import com.insider.sanjuanisland.models.AllCategoriesModel;
import com.insider.sanjuanisland.models.ClientDetailsModel;
import com.insider.sanjuanisland.models.LocationsModel;
import com.insider.sanjuanisland.models.Parent_Child_Relationships_Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *Used to perform the Database Operations
 */
public class DataBaseAdapter extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "SanJuanIslandsDB.db";
    public static final int DATABASE_VERSION = 1;
    SQLiteDatabase sqliteDB;

    public DataBaseAdapter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db, TableCreation.ClientDetails.TABLE_NAME, TableCreation.getColumnsStringFromArray(TableCreation.ClientDetails.columns));
        createTable(db, TableCreation.AllCategories.TABLE_NAME, TableCreation.getColumnsStringFromArray(TableCreation.AllCategories.columns));
        createTable(db, TableCreation.Locations.TABLE_NAME, TableCreation.getColumnsStringFromArray(TableCreation.Locations.columns));
        createTable(db, TableCreation.Parent_Child_Relationships.TABLE_NAME, TableCreation.getColumnsStringFromArray(TableCreation.Parent_Child_Relationships.columns));
        createTable(db, TableCreation.About.TABLE_NAME, TableCreation.getColumnsStringFromArray(TableCreation.About.columns));
        createTable(db, TableCreation.TipsAndSupport.TABLE_NAME, TableCreation.getColumnsStringFromArray(TableCreation.TipsAndSupport.columns));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createTable(SQLiteDatabase db, String tableName, String columns) {
        String query = "CREATE TABLE " + tableName + " (" + columns + ");";
        db.execSQL(query);
    }

    public void dropTable(SQLiteDatabase db, String tableName) {
        String query = "DROP TABLE IF EXISTS " + tableName + ";";
        db.execSQL(query);
    }

    // Open the Database in write mode
    public void open() {
        sqliteDB = this.getWritableDatabase();
    }//Open the database in read mode

    public void read() {
        sqliteDB = this.getReadableDatabase();
    }

    // Close the Database
    public void Close() {
        sqliteDB.close();
    }

    //get the Updated Date
    public String getDate() {
        String result = "";
        read();
        Cursor cursorUpdatedDate = sqliteDB.query(TableCreation.ClientDetails.TABLE_NAME, null, null, null, null, null, null);
        if (cursorUpdatedDate != null && cursorUpdatedDate.getCount() > 0) {
            if (cursorUpdatedDate.moveToLast()) {
                result = cursorUpdatedDate.getString(cursorUpdatedDate.getColumnIndex("Updated"));
            }
        }
        cursorUpdatedDate.close();
        close();
        return result;
    }

    //get the Max Date from Country Table
    public String getLocationIdByMaxDate() {
        String result = "";
        read();
        Cursor cursorMaxDate = null;
        cursorMaxDate = sqliteDB.rawQuery("SELECT Location_ID, MAX (Updated) INIT_DATE FROM Locations", null);
        if (cursorMaxDate != null) {
            cursorMaxDate.moveToFirst();
            result = cursorMaxDate.getString(cursorMaxDate.getColumnIndex("Location_ID"));
        }
        cursorMaxDate.close();
        close();
        return result;
    }

    //Insert Abt and Tips details
    public void insertAbtDetails(AbtModel abtModel, String tableName) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableCreation.About.Title, abtModel.getTitle());
        contentValues.put(TableCreation.About.Content, abtModel.getContent());
        contentValues.put(TableCreation.About.Created, abtModel.getCreated());
        contentValues.put(TableCreation.About.Updated, abtModel.getUpdated());
        sqliteDB.insert(tableName, null, contentValues);
        close();
    }

    //get the abt and tips values
    public AbtModel getAbtDetails(String tableName){
        AbtModel mAbtModel = null;
        read();
        Cursor cursorAbtDetails=sqliteDB.query(tableName,null,null,null,null,null,null);
        if(cursorAbtDetails!=null&&cursorAbtDetails.getCount()>0){
            if(cursorAbtDetails.moveToFirst()){
                mAbtModel = new AbtModel();
                mAbtModel.setTitle(cursorAbtDetails.getString(cursorAbtDetails.getColumnIndex(TableCreation.About.Title)));
                mAbtModel.setContent(cursorAbtDetails.getString(cursorAbtDetails.getColumnIndex(TableCreation.About.Content)));
            }
        }
        close();
        return mAbtModel;
    }

    //get the Updated Date from About Table
    public String getAbtUpdatedDate(String tableName) {
        String result = "";
        String convertDate = "";
        read();
        Cursor cursorMaxDate = null;
        cursorMaxDate = sqliteDB.rawQuery("SELECT * FROM "+tableName, null);
        if (cursorMaxDate != null) {
            if(cursorMaxDate.moveToFirst()) {
                result = cursorMaxDate.getString(cursorMaxDate.getColumnIndex("Updated"));
            }
        }
        if(!result.equalsIgnoreCase("")) {
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSSSSS");
            SimpleDateFormat convertformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
            try {
                date = format.parse(result);
                convertDate = convertformat.format(date);
                System.out.println("convertDate-------->" + convertDate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        cursorMaxDate.close();
        close();
        return convertDate;
    }

    //get the Max Date from Country Table
    public String getMaxDate(String mLocationId) {
        String result = "";
        String convertDate = "";
        read();
        Cursor cursorUpdatedDate = sqliteDB.query(TableCreation.Locations.TABLE_NAME, null, TableCreation.Locations.Location_ID+"=?", new String[]{String.valueOf(mLocationId)}, null, null, null, null);
        if (cursorUpdatedDate != null && cursorUpdatedDate.getCount() > 0) {
            if (cursorUpdatedDate.moveToLast()) {
                result = cursorUpdatedDate.getString(cursorUpdatedDate.getColumnIndex("Updated"));
            }
        }
        if(!result.equalsIgnoreCase("")) {
            Log.i("MaxDate",result);
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSSSSS");
            SimpleDateFormat convertformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
            try {
                date = format.parse(result);
                convertDate = convertformat.format(date);
                System.out.println("convertDate-------->" + convertDate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        cursorUpdatedDate.close();
        close();
        return convertDate;
    }

    //get Locations based on parent
    public List<LocationsModel> getExploreLocationsList(){
        List<LocationsModel> locationsList = new ArrayList<>();
        LocationsModel mLocationsModel = null;
        read();
        Cursor cursorSection = null;
        try {
            cursorSection = sqliteDB.rawQuery("SELECT DISTINCT * FROM Locations WHERE Location_Type IN (1,3) AND Location_ID NOT IN (SELECT Child_Location_ID FROM Parent_Child_Relationships) ", null);
            if (cursorSection != null && cursorSection.getCount() > 0) {
                if (cursorSection.moveToFirst()) {
                    do {
                        if(cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Location_Status_ID))==1) {
                            mLocationsModel = new LocationsModel();
                            mLocationsModel.setLocation_ID(cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Location_ID)));
                            mLocationsModel.setClient_ID(cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Client_ID)));
                            mLocationsModel.setLocation_Name(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Location_Name)));
                            mLocationsModel.setLocation_Status_ID(cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Location_Status_ID)));
                            mLocationsModel.setMap_GPS(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Map_GPS)));
                            mLocationsModel.setDescription(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Description)));
                            mLocationsModel.setPoint_Collection_Radius(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Point_Collection_Radius)));
                            mLocationsModel.setPhoto(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Photo)));
                            mLocationsModel.setAddress1(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Address1)));
                            mLocationsModel.setAddress2(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Address2)));
                            mLocationsModel.setCity(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.City)));
                            mLocationsModel.setState(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.State)));
                            mLocationsModel.setCountry(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Country)));
                            mLocationsModel.setZip(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Zip)));
                            mLocationsModel.setPhone(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Phone)));
                            mLocationsModel.setEmail(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Email)));
                            mLocationsModel.setURL(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.URL)));
                            mLocationsModel.setFacebook_URL(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Facebook_URL)));
                            mLocationsModel.setPinterest_URL(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Pinterest_URL)));
                            mLocationsModel.setTwitter_URL(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Twitter_URL)));
                            mLocationsModel.setInstaGram_URL(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.InstaGram_URL)));
                            if (cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Parent_Location)) == 1) {
                                mLocationsModel.setParent_Location(true);
                            } else {
                                mLocationsModel.setParent_Location(false);
                            }
                            mLocationsModel.setCategory_ID(cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Category_ID)));
                            mLocationsModel.setReward_Points_Earned(cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Reward_Points_Earned)));
                            mLocationsModel.setLocation_Type(cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Location_Type)));
                            mLocationsModel.setReward_Description(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Reward_Description)));
                            mLocationsModel.setCreated(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Created)));
                            mLocationsModel.setReward_Points_Required(cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Reward_Points_Required)));
                            mLocationsModel.setUpdated(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Updated)));
                            locationsList.add(mLocationsModel);
                        }
                    } while (cursorSection.moveToNext());
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursorSection != null)
                cursorSection.close();
        }
        close();
        return locationsList;
    }

    //get Locations based on parent id
    public List<LocationsModel> getLocationsListByParent(Integer locationId){
        List<LocationsModel> locationsList = new ArrayList<>();
        LocationsModel mLocationsModel = null;
        read();
        Cursor cursorSection = null;
        try {
            cursorSection = sqliteDB.rawQuery("SELECT * FROM Locations join Parent_Child_Relationships WHERE Parent_Child_Relationships.Child_Location_ID = Locations.Location_ID AND Parent_Child_Relationships.Parent_Location_ID= "+locationId, null);
            if (cursorSection != null && cursorSection.getCount() > 0) {
                if (cursorSection.moveToFirst()) {
                    do {
                        if(cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Location_Status_ID))==1) {
                            mLocationsModel = new LocationsModel();
                            mLocationsModel.setLocation_ID(cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Location_ID)));
                            mLocationsModel.setClient_ID(cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Client_ID)));
                            mLocationsModel.setLocation_Name(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Location_Name)));
                            mLocationsModel.setLocation_Status_ID(cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Location_Status_ID)));
                            mLocationsModel.setMap_GPS(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Map_GPS)));
                            mLocationsModel.setDescription(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Description)));
                            mLocationsModel.setPoint_Collection_Radius(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Point_Collection_Radius)));
                            mLocationsModel.setPhoto(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Photo)));
                            mLocationsModel.setAddress1(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Address1)));
                            mLocationsModel.setAddress2(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Address2)));
                            mLocationsModel.setCity(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.City)));
                            mLocationsModel.setState(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.State)));
                            mLocationsModel.setCountry(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Country)));
                            mLocationsModel.setZip(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Zip)));
                            mLocationsModel.setPhone(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Phone)));
                            mLocationsModel.setEmail(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Email)));
                            mLocationsModel.setURL(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.URL)));
                            mLocationsModel.setFacebook_URL(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Facebook_URL)));
                            mLocationsModel.setPinterest_URL(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Pinterest_URL)));
                            mLocationsModel.setTwitter_URL(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Twitter_URL)));
                            mLocationsModel.setInstaGram_URL(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.InstaGram_URL)));
                            if (cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Parent_Location)) == 1) {
                                mLocationsModel.setParent_Location(true);
                            } else {
                                mLocationsModel.setParent_Location(false);
                            }
                            mLocationsModel.setCategory_ID(cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Category_ID)));
                            mLocationsModel.setReward_Points_Earned(cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Reward_Points_Earned)));
                            mLocationsModel.setLocation_Type(cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Location_Type)));
                            mLocationsModel.setReward_Description(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Reward_Description)));
                            mLocationsModel.setCreated(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Created)));
                            mLocationsModel.setReward_Points_Required(cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Reward_Points_Required)));
                            mLocationsModel.setUpdated(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Updated)));
                            locationsList.add(mLocationsModel);
                        }
                    } while (cursorSection.moveToNext());
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursorSection != null)
                cursorSection.close();
        }
        close();
        return locationsList;
    }

    //get Locations based on parent
    public List<LocationsModel> getListRewardsLocationsList(){
        List<LocationsModel> locationsList = new ArrayList<>();
        LocationsModel mLocationsModel = null;
        read();
        Cursor cursorSection = null;
        try {
            cursorSection = sqliteDB.rawQuery("SELECT * FROM Locations WHERE Location_Type IN (2,3) ", null);
            if (cursorSection != null && cursorSection.getCount() > 0) {
                if (cursorSection.moveToFirst()) {
                    do {
                        if(cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Location_Status_ID))==1) {
                            mLocationsModel = new LocationsModel();
                            mLocationsModel.setLocation_ID(cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Location_ID)));
                            mLocationsModel.setClient_ID(cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Client_ID)));
                            mLocationsModel.setLocation_Name(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Location_Name)));
                            mLocationsModel.setLocation_Status_ID(cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Location_Status_ID)));
                            mLocationsModel.setMap_GPS(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Map_GPS)));
                            mLocationsModel.setDescription(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Description)));
                            mLocationsModel.setPoint_Collection_Radius(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Point_Collection_Radius)));
                            mLocationsModel.setPhoto(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Photo)));
                            mLocationsModel.setAddress1(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Address1)));
                            mLocationsModel.setAddress2(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Address2)));
                            mLocationsModel.setCity(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.City)));
                            mLocationsModel.setState(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.State)));
                            mLocationsModel.setCountry(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Country)));
                            mLocationsModel.setZip(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Zip)));
                            mLocationsModel.setPhone(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Phone)));
                            mLocationsModel.setEmail(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Email)));
                            mLocationsModel.setURL(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.URL)));
                            mLocationsModel.setFacebook_URL(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Facebook_URL)));
                            mLocationsModel.setPinterest_URL(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Pinterest_URL)));
                            mLocationsModel.setTwitter_URL(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Twitter_URL)));
                            mLocationsModel.setInstaGram_URL(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.InstaGram_URL)));
                            if (cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Parent_Location)) == 1) {
                                mLocationsModel.setParent_Location(true);
                            } else {
                                mLocationsModel.setParent_Location(false);
                            }
                            mLocationsModel.setCategory_ID(cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Category_ID)));
                            mLocationsModel.setReward_Points_Earned(cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Reward_Points_Earned)));
                            mLocationsModel.setLocation_Type(cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Location_Type)));
                            mLocationsModel.setReward_Description(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Reward_Description)));
                            mLocationsModel.setCreated(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Created)));
                            mLocationsModel.setReward_Points_Required(cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Reward_Points_Required)));
                            mLocationsModel.setUpdated(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Updated)));
                            locationsList.add(mLocationsModel);
                        }
                    } while (cursorSection.moveToNext());
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursorSection != null)
                cursorSection.close();
        }
        close();
        return locationsList;
    }


    //get the Max Date from parent child relationship Table
    public String getCategory_Name(Integer mCategoryId) {
        String result = "";
        read();
        Cursor cursorCategoryName = null;
        cursorCategoryName = sqliteDB.query(TableCreation.AllCategories.TABLE_NAME, null, TableCreation.AllCategories.Category_ID + "=?", new String[]{String.valueOf(mCategoryId)}, null, null, null);
        if (cursorCategoryName != null) {
            cursorCategoryName.moveToFirst();
            result = cursorCategoryName.getString(cursorCategoryName.getColumnIndex(TableCreation.AllCategories.Category_Name));
        }
        cursorCategoryName.close();
        close();
        return result;
    }


    //Insert client details
    public void insertClientDetails(ClientDetailsModel clientDetailsModel) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableCreation.ClientDetails.Client_ID, clientDetailsModel.getClient_ID());
        contentValues.put(TableCreation.ClientDetails.Client_Status_ID, clientDetailsModel.getClient_Status_ID());
        contentValues.put(TableCreation.ClientDetails.Client_Name, clientDetailsModel.getClient_Name());
        contentValues.put(TableCreation.ClientDetails.App_Name, clientDetailsModel.getApp_Name());
        contentValues.put(TableCreation.ClientDetails.Logo_Image, clientDetailsModel.getLogo_Image());
        contentValues.put(TableCreation.ClientDetails.Bkg_Image, clientDetailsModel.getBkg_Image());
        contentValues.put(TableCreation.ClientDetails.Description, clientDetailsModel.getDescription());
        contentValues.put(TableCreation.ClientDetails.Reward_Code, clientDetailsModel.getReward_Code());
        contentValues.put(TableCreation.ClientDetails.Map_GPS, clientDetailsModel.getMap_GPS());
        contentValues.put(TableCreation.ClientDetails.Map_Radius, clientDetailsModel.getMap_Radius());
        contentValues.put(TableCreation.ClientDetails.Created, clientDetailsModel.getCreated());
        contentValues.put(TableCreation.ClientDetails.Updated, clientDetailsModel.getUpdated());
        contentValues.put(TableCreation.ClientDetails.Message, clientDetailsModel.getMessage());
        sqliteDB.insert(TableCreation.ClientDetails.TABLE_NAME, null, contentValues);
        close();
    }

    //Insert Location details
    public void insertLocationDetails(List<LocationsModel> locationsModelList) {
        open();
        for(int i=0; i<locationsModelList.size();i++) {
            LocationsModel mLocationsModel = locationsModelList.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(TableCreation.Locations.Location_ID, mLocationsModel.getLocation_ID());
            contentValues.put(TableCreation.Locations.Client_ID, mLocationsModel.getClient_ID());
            contentValues.put(TableCreation.Locations.Location_Name, mLocationsModel.getLocation_Name());
            contentValues.put(TableCreation.Locations.Location_Status_ID, mLocationsModel.getLocation_Status_ID());
            contentValues.put(TableCreation.Locations.Map_GPS, mLocationsModel.getMap_GPS());
            contentValues.put(TableCreation.Locations.Description, mLocationsModel.getDescription());
            contentValues.put(TableCreation.Locations.Point_Collection_Radius, mLocationsModel.getPoint_Collection_Radius());
            contentValues.put(TableCreation.Locations.Photo, mLocationsModel.getPhoto());
            contentValues.put(TableCreation.Locations.Address1, mLocationsModel.getAddress1());
            contentValues.put(TableCreation.Locations.Address2, mLocationsModel.getAddress2());
            contentValues.put(TableCreation.Locations.City, mLocationsModel.getCity());
            contentValues.put(TableCreation.Locations.State, mLocationsModel.getState());
            contentValues.put(TableCreation.Locations.Country, mLocationsModel.getCountry());
            contentValues.put(TableCreation.Locations.Zip, mLocationsModel.getZip());
            contentValues.put(TableCreation.Locations.Phone, mLocationsModel.getPhone());
            contentValues.put(TableCreation.Locations.Email, mLocationsModel.getEmail());
            contentValues.put(TableCreation.Locations.URL, mLocationsModel.getURL());
            contentValues.put(TableCreation.Locations.Facebook_URL, mLocationsModel.getFacebook_URL());
            contentValues.put(TableCreation.Locations.Pinterest_URL, mLocationsModel.getPinterest_URL());
            contentValues.put(TableCreation.Locations.Twitter_URL, mLocationsModel.getTwitter_URL());
            contentValues.put(TableCreation.Locations.InstaGram_URL, mLocationsModel.getInstaGram_URL());
            contentValues.put(TableCreation.Locations.Parent_Location, mLocationsModel.isParent_Location());
            contentValues.put(TableCreation.Locations.Category_ID, mLocationsModel.getCategory_ID());
            contentValues.put(TableCreation.Locations.Reward_Points_Earned, mLocationsModel.getReward_Points_Earned());
            contentValues.put(TableCreation.Locations.Location_Type, mLocationsModel.getLocation_Type());
            contentValues.put(TableCreation.Locations.Reward_Description, mLocationsModel.getReward_Description());
            contentValues.put(TableCreation.Locations.Created, mLocationsModel.getCreated());
            contentValues.put(TableCreation.Locations.Reward_Points_Required, mLocationsModel.getReward_Points_Required());
            contentValues.put(TableCreation.Locations.Updated, mLocationsModel.getUpdated());
            sqliteDB.insert(TableCreation.Locations.TABLE_NAME, null, contentValues);
        }
        close();
    }

    //Insert All Categories details
    public void insertAllCategories(List<AllCategoriesModel> allCategoriesModelList) {
        open();
        for(int i=0; i<allCategoriesModelList.size();i++) {
            AllCategoriesModel mAllCategoriesModel = allCategoriesModelList.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(TableCreation.AllCategories.Category_ID, mAllCategoriesModel.getCategory_ID());
            contentValues.put(TableCreation.AllCategories.Category_Name, mAllCategoriesModel.getCategory_Name());
            contentValues.put(TableCreation.AllCategories.Client_ID, mAllCategoriesModel.getClient_ID());
            sqliteDB.insert(TableCreation.AllCategories.TABLE_NAME, null, contentValues);
        }
        close();
    }

    //Insert AllLocationRelation details
    public void insertAllLocationRelation(List<Parent_Child_Relationships_Model> parentChildRelationshipsModelList) {
        open();
        for(int i=0; i<parentChildRelationshipsModelList.size();i++) {
            Parent_Child_Relationships_Model mParentChildRelationshipsModel = parentChildRelationshipsModelList.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(TableCreation.Parent_Child_Relationships.Parent_Child_Relationship_ID,
                    mParentChildRelationshipsModel.getParent_Child_Relationship_ID());
            contentValues.put(TableCreation.Parent_Child_Relationships.Parent_Location_ID,
                    mParentChildRelationshipsModel.getParent_Location_ID());
            contentValues.put(TableCreation.Parent_Child_Relationships.Child_Location_ID,
                    mParentChildRelationshipsModel.getChild_Location_ID());
            sqliteDB.insert(TableCreation.Parent_Child_Relationships.TABLE_NAME, null, contentValues);
        }
        close();
    }

    //Update the client details
    public void updateClientDetails(ClientDetailsModel clientDetailsModel) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableCreation.ClientDetails.Client_Name, clientDetailsModel.getClient_Name());
        contentValues.put(TableCreation.ClientDetails.App_Name, clientDetailsModel.getApp_Name());
        contentValues.put(TableCreation.ClientDetails.Logo_Image, clientDetailsModel.getLogo_Image());
        contentValues.put(TableCreation.ClientDetails.Bkg_Image, clientDetailsModel.getBkg_Image());
        contentValues.put(TableCreation.ClientDetails.Description, clientDetailsModel.getDescription());
        contentValues.put(TableCreation.ClientDetails.Reward_Code, clientDetailsModel.getReward_Code());
        contentValues.put(TableCreation.ClientDetails.Map_GPS, clientDetailsModel.getMap_GPS());
        contentValues.put(TableCreation.ClientDetails.Map_Radius, clientDetailsModel.getMap_Radius());
        contentValues.put(TableCreation.ClientDetails.Created, clientDetailsModel.getCreated());
        contentValues.put(TableCreation.ClientDetails.Updated, clientDetailsModel.getUpdated());
        contentValues.put(TableCreation.ClientDetails.Message, clientDetailsModel.getMessage());
        sqliteDB.update(TableCreation.ClientDetails.TABLE_NAME, contentValues,
                TableCreation.ClientDetails.Client_ID + "=? AND " + TableCreation.ClientDetails.Client_Status_ID + "=?"
                , new String[]{String.valueOf(clientDetailsModel.getClient_ID()),
                        String.valueOf(clientDetailsModel.getClient_Status_ID())});
        close();
    }

    //get the all Client values
    public ClientDetailsModel getClientDetails(){
        ClientDetailsModel mClientDetailsModel = null;
        read();
        Cursor cursorClientDetails=sqliteDB.query(TableCreation.ClientDetails.TABLE_NAME,null,null,null,null,null,null);
        if(cursorClientDetails!=null&&cursorClientDetails.getCount()>0){
            if(cursorClientDetails.moveToFirst()){
                    mClientDetailsModel = new ClientDetailsModel();
                mClientDetailsModel.setClient_ID(cursorClientDetails.getInt(cursorClientDetails.getColumnIndex("Client_ID")));
                mClientDetailsModel.setClient_Status_ID(cursorClientDetails.getInt(cursorClientDetails.getColumnIndex("Client_Status_ID")));
                mClientDetailsModel.setClient_Name(cursorClientDetails.getString(cursorClientDetails.getColumnIndex("Client_Name")));
                mClientDetailsModel.setApp_Name(cursorClientDetails.getString(cursorClientDetails.getColumnIndex("App_Name")));
                mClientDetailsModel.setLogo_Image(cursorClientDetails.getString(cursorClientDetails.getColumnIndex("Logo_Image")));
                mClientDetailsModel.setBkg_Image(cursorClientDetails.getString(cursorClientDetails.getColumnIndex("Bkg_Image")));
                mClientDetailsModel.setDescription(cursorClientDetails.getString(cursorClientDetails.getColumnIndex("Description")));
                mClientDetailsModel.setReward_Code(cursorClientDetails.getInt(cursorClientDetails.getColumnIndex("Reward_Code")));
                mClientDetailsModel.setMap_GPS(cursorClientDetails.getString(cursorClientDetails.getColumnIndex("Map_GPS")));
                mClientDetailsModel.setMap_Radius(cursorClientDetails.getString(cursorClientDetails.getColumnIndex("Map_Radius")));
                /*SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
                Date createdDate = new Date();
                Date updatedDate = new Date();
                try {
                    createdDate = dateFormat.parse(cursorClientDetails.getString(cursorClientDetails.getColumnIndex("Created")));
                    updatedDate = dateFormat.parse(cursorClientDetails.getString(cursorClientDetails.getColumnIndex("Updated")));
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }*/
                mClientDetailsModel.setCreated(cursorClientDetails.getString(cursorClientDetails.getColumnIndex("Created")));
                mClientDetailsModel.setUpdated(cursorClientDetails.getString(cursorClientDetails.getColumnIndex("Updated")));
                mClientDetailsModel.setMessage(cursorClientDetails.getString(cursorClientDetails.getColumnIndex("Message")));
            }
        }
        close();
        return mClientDetailsModel;
    }

    //get Location detail
    public LocationsModel getLocationsDetail(Integer locationId){
        LocationsModel mLocationsModel = null;
        read();
        Cursor cursorSection = null;
        try {
            cursorSection = sqliteDB.rawQuery("SELECT * FROM Locations WHERE Location_ID = "+locationId, null);
            if (cursorSection != null && cursorSection.getCount() > 0) {
                if (cursorSection.moveToFirst()) {
                        mLocationsModel = new LocationsModel();
                        mLocationsModel.setLocation_ID(cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Location_ID)));
                        mLocationsModel.setClient_ID(cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Client_ID)));
                        mLocationsModel.setLocation_Name(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Location_Name)));
                        mLocationsModel.setLocation_Status_ID(cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Location_Status_ID)));
                        mLocationsModel.setMap_GPS(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Map_GPS)));
                        mLocationsModel.setDescription(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Description)));
                        mLocationsModel.setPoint_Collection_Radius(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Point_Collection_Radius)));
                        mLocationsModel.setPhoto(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Photo)));
                        mLocationsModel.setAddress1(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Address1)));
                        mLocationsModel.setAddress2(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Address2)));
                        mLocationsModel.setCity(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.City)));
                        mLocationsModel.setState(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.State)));
                        mLocationsModel.setCountry(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Country)));
                        mLocationsModel.setZip(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Zip)));
                        mLocationsModel.setPhone(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Phone)));
                        mLocationsModel.setEmail(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Email)));
                        mLocationsModel.setURL(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.URL)));
                        mLocationsModel.setFacebook_URL(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Facebook_URL)));
                        mLocationsModel.setPinterest_URL(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Pinterest_URL)));
                        mLocationsModel.setTwitter_URL(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Twitter_URL)));
                        mLocationsModel.setInstaGram_URL(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.InstaGram_URL)));
                        if(cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Parent_Location))==1) {
                            mLocationsModel.setParent_Location(true);
                        }else {
                            mLocationsModel.setParent_Location(false);
                        }
                        mLocationsModel.setCategory_ID(cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Category_ID)));
                        mLocationsModel.setReward_Points_Earned(cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Reward_Points_Earned)));
                        mLocationsModel.setLocation_Type(cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Location_Type)));
                        mLocationsModel.setReward_Description(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Reward_Description)));
                        mLocationsModel.setCreated(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Created)));
                        mLocationsModel.setReward_Points_Required(cursorSection.getInt(cursorSection.getColumnIndex(TableCreation.Locations.Reward_Points_Required)));
                        mLocationsModel.setUpdated(cursorSection.getString(cursorSection.getColumnIndex(TableCreation.Locations.Updated)));

                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursorSection != null)
                cursorSection.close();
        }
        close();
        return mLocationsModel;
    }

    //Delete all records from table
    public void deleteRecords(String TableName){
        open();
        sqliteDB.delete(TableName, null, null);
        close();
    }

    //Delete records from Location Table table based on Location id
    public void deleteRecords(String[] locationID){
        open();
        String args = TextUtils.join(", ", locationID);
        Log.i("args",args);
        sqliteDB.execSQL(String.format("DELETE FROM Locations WHERE Location_ID IN (%s);", args));
        close();
    }

    @Override
    public synchronized void close() {
        if (sqliteDB != null) {
            sqliteDB.close();
            super.close();
        }
    }


}

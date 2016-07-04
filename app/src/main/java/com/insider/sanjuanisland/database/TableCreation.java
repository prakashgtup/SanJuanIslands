package com.insider.sanjuanisland.database;

import android.provider.BaseColumns;

/**
 *All the DataBase Table Names and Columns are Available in this class
 */
public class TableCreation {
    private static final String PRIMARY_KEY = " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT";
    private static final String INTEGER_TYPE_NOT_NULL = " INTEGER NOT NULL";
    private static final String INTEGER_TYPE = " INTEGER NULL";
    private static final String TEXT_TYPE_NOT_NULL = " TEXT NOT NULL";
    private static final String TEXT_TYPE = " TEXT NULL";

    public static String getColumnsStringFromArray(String[] strings) {
        String columnString = "";
        for (int i = 0; i < strings.length; i++) {
            columnString += strings[i];
        }
        return columnString;
    }

    //---------------------------------------------------
    //               ClientDetails table
    //---------------------------------------------------
    public static abstract class ClientDetails implements BaseColumns {
        public static final String TABLE_NAME = "ClientDetails";
        public static final String Client_ID = "Client_ID";
        public static final String Client_Status_ID = "Client_Status_ID";
        public static final String Client_Name = "Client_Name";
        public static final String App_Name = "App_Name";
        public static final String Logo_Image = "Logo_Image";
        public static final String Bkg_Image = "Bkg_Image";
        public static final String Description = "Description";
        public static final String Reward_Code = "Reward_Code";
        public static final String Map_GPS = "Map_GPS";
        public static final String Map_Radius = "Map_Radius";
        public static final String Created = "Created";
        public static final String Updated = "Updated";
        public static final String Message = "Message";

        public static String[] columns = {
                Client_ID + INTEGER_TYPE_NOT_NULL + ",",
                Client_Status_ID + INTEGER_TYPE_NOT_NULL + ",",
                Client_Name + TEXT_TYPE + ",",
                App_Name + TEXT_TYPE + ",",
                Logo_Image + TEXT_TYPE + ",",
                Bkg_Image + TEXT_TYPE + ",",
                Description + TEXT_TYPE + ",",
                Reward_Code + INTEGER_TYPE + ",",
                Map_GPS + TEXT_TYPE + ",",
                Map_Radius + TEXT_TYPE + ",",
                Created + TEXT_TYPE + ",",
                Updated + TEXT_TYPE + ",",
                Message + TEXT_TYPE
        };
    }

    //---------------------------------------------------
    //               AllCategories table
    //---------------------------------------------------
    public static abstract class AllCategories implements BaseColumns {
        public static final String TABLE_NAME = "AllCategories";
        public static final String Category_ID = "Category_ID";
        public static final String Category_Name = "Category_Name";
        public static final String Client_ID = "Client_ID";

        public static String[] columns = {
                Category_ID + PRIMARY_KEY + ",",
                Category_Name + " VARCHAR(600) NOT NULL" + ",",
                Client_ID + INTEGER_TYPE_NOT_NULL
        };
    }

    //---------------------------------------------------
    //              Locations table
    //---------------------------------------------------
    public static abstract class Locations implements BaseColumns {
        public static final String TABLE_NAME = "Locations";
        public static final String Location_ID = "Location_ID";
        public static final String Client_ID = "Client_ID";
        public static final String Location_Status_ID = "Location_Status_ID";
        public static final String Location_Name = "Location_Name";
        public static final String Description = "Description";
        public static final String Map_GPS = "Map_GPS";
        public static final String Point_Collection_Radius = "Point_Collection_Radius";
        public static final String Photo = "Photo";
        public static final String Address1 = "Address1";
        public static final String Address2 = "Address2";
        public static final String City = "City";
        public static final String State = "State";
        public static final String Zip = "Zip";
        public static final String Country = "Country";
        public static final String Phone = "Phone";
        public static final String Email = "Email";
        public static final String URL = "URL";
        public static final String Facebook_URL = "Facebook_URL";
        public static final String Pinterest_URL = "Pinterest_URL";
        public static final String Twitter_URL = "Twitter_URL";
        public static final String InstaGram_URL = "InstaGram_URL";
        public static final String Parent_Location = "Parent_Location";
        public static final String Category_ID = "Category_ID";
        public static final String Reward_Points_Earned = "Reward_Points_Earned";
        public static final String Location_Type = "Location_Type";
        public static final String Reward_Description = "Reward_Description";
        public static final String Created = "Created";
        public static final String Updated = "Updated";
        public static final String Reward_Points_Required = "Reward_Points_Required";


        public static String[] columns = {
                Location_ID + PRIMARY_KEY + ",",
                Client_ID + INTEGER_TYPE_NOT_NULL + ",",
                Location_Status_ID + INTEGER_TYPE_NOT_NULL + ",",
                Location_Name + " VARCHAR(200) NOT NULL" + ",",
                Description + TEXT_TYPE_NOT_NULL + ",",
                Map_GPS + " VARCHAR(100) NOT NULL" + ",", Point_Collection_Radius + " VARCHAR(50) NOT NULL" + ",",
                Photo + " VARCHAR(200) NOT NULL" + ",",
                Address1 + " VARCHAR(510) NULL" + ",",
                Address2 + " VARCHAR(510) NULL" + ",",
                City + " VARCHAR(100) NULL" + ",",
                State + " VARCHAR(100) NULL" + ",",
                Zip + " VARCHAR(20) NULL" + ",",
                Country + " VARCHAR(50) NULL" + ",",
                Phone + " VARCHAR(100) NULL" + ",",
                Email + " VARCHAR(510) NULL" + ",",
                URL + " VARCHAR(600) NULL" + ",",
                Facebook_URL + " VARCHAR(600) NULL" + ",",
                Pinterest_URL + " VARCHAR(600) NULL" + ",",
                Twitter_URL + " VARCHAR(600) NULL" + ",",
                InstaGram_URL + " VARCHAR(600) NULL" + ",",
                Parent_Location + " BOOL NOT NULL" + ",",
                Category_ID + INTEGER_TYPE + ",",
                Reward_Points_Earned + INTEGER_TYPE + ",",
                Location_Type + INTEGER_TYPE_NOT_NULL + ",",
                Reward_Description + TEXT_TYPE + ",",
                Created + TEXT_TYPE + ",",
                Updated + TEXT_TYPE + ",",
                Reward_Points_Required + " INTEGER NULL"
        };
    }

    //---------------------------------------------------
    //               Parent_Child_Relationships table
    //---------------------------------------------------
    public static abstract class Parent_Child_Relationships implements BaseColumns {
        public static final String TABLE_NAME = "Parent_Child_Relationships";
        public static final String Parent_Child_Relationship_ID = "Parent_Child_Relationship_ID";
        public static final String Parent_Location_ID = "Parent_Location_ID";
        public static final String Child_Location_ID = "Child_Location_ID";

        public static String[] columns = {
                Parent_Child_Relationship_ID + PRIMARY_KEY + ",",
                Parent_Location_ID + INTEGER_TYPE_NOT_NULL + ",",
                Child_Location_ID + INTEGER_TYPE_NOT_NULL
        };
    }

    //---------------------------------------------------
    //               About table
    //---------------------------------------------------
    public static abstract class About implements BaseColumns {
        public static final String TABLE_NAME = "About";
        public static final String ID = "ID";
        public static final String Title  = "Title";
        public static final String Content = "Content";
        public static final String Created  = "Created";
        public static final String Updated  = "Updated";

        public static String[] columns = {
               ID + PRIMARY_KEY + ",",
                Title + " VARCHAR(200) NOT NULL" + ",",
                Content + TEXT_TYPE_NOT_NULL + ",",
                Created + TEXT_TYPE + ",",
                Updated + TEXT_TYPE
        };
    }
    //---------------------------------------------------
    //               Tips and Support table
    //---------------------------------------------------
    public static abstract class TipsAndSupport implements BaseColumns {
        public static final String TABLE_NAME = "TipsAndSupport";
        public static final String ID = "ID";
        public static final String Title  = "Title";
        public static final String Content = "Content";
        public static final String Created  = "Created";
        public static final String Updated  = "Updated";

        public static String[] columns = {
                ID + PRIMARY_KEY + ",",
                Title + " VARCHAR(200) NOT NULL" + ",",
                Content + TEXT_TYPE_NOT_NULL + ",",
                Created + TEXT_TYPE + ",",
                Updated + TEXT_TYPE
        };
    }
}

package com.insider.sanjuanisland.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

/**
 *To store the session values
 */
public class Session {
    public static final String SANJUAN = "sanjuan", USER_ID = "user_id", IS_lOGGED_IN = "is_logged_in",
            CLIENT_NAME = "ClientName", USER_STATUS = "User_Status", EMAIL = "Email", PASSWORD = "Password",
            FIRST_NAME = "First_Name", LAST_NAME = "Last_Name", PHONE = "Phone", COUNTRY = "Country",
            STATE = "State", CITY = "City", TOTAL_POINTS_EARNED = "Total_Points_Earned",
            TOTAL_POINTS_REDEEMED = "Total_Points_Redeemed", IS_IT_FIRST_TIME = "is_it_first_time";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private Context mContext;

    public Session(Context context) {
        mContext = context;
        pref = context.getSharedPreferences(SANJUAN, 0);
        editor = pref.edit();
        editor.commit();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    //store the user information's
    public void setUserInfo(String userId, String clientName, String userStatus, String email,
                            String firstName, String lastName, String phoneNo, String country,
                            String state, String city, Integer pointsEarned, Integer pointsRedeemed) {
        editor.putString(USER_ID, userId);
        editor.putString(CLIENT_NAME, clientName);
        editor.putString(USER_STATUS, userStatus);
        editor.putString(EMAIL, email);
        editor.putString(FIRST_NAME, firstName);
        editor.putString(LAST_NAME, lastName);
        editor.putString(PHONE, phoneNo);
        editor.putString(COUNTRY, country);
        editor.putString(STATE, state);
        editor.putString(CITY, city);
        editor.putInt(TOTAL_POINTS_EARNED, pointsEarned);
        editor.putInt(TOTAL_POINTS_REDEEMED, pointsRedeemed);
        editor.putBoolean(IS_lOGGED_IN, true);
        editor.commit();
    }

    //Logout
    public void logout() {
        editor.putString(USER_ID, "0");
        editor.putString(PASSWORD, "");
        editor.putBoolean(IS_lOGGED_IN, false);
        editor.commit();
    }


    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_lOGGED_IN, false);
    }

    public String getUserId() {
        return pref.getString(USER_ID, "0");
    }

    public String getFirstName() {
        return pref.getString(FIRST_NAME, "");
    }

    public Integer getTotalPointsEarned() {
        return pref.getInt(TOTAL_POINTS_EARNED, 0);
    }

    public void setTotalPointsEarned(Integer pointsEarned) {
        editor.putInt(TOTAL_POINTS_EARNED, pointsEarned);
        editor.commit();
    }

    public Integer getTotalPointsRedeemed() {
        return pref.getInt(TOTAL_POINTS_REDEEMED, 0);
    }

    public void setTotalPointsRedeemed(Integer pointsRedeemed) {
        editor.putInt(TOTAL_POINTS_REDEEMED, pointsRedeemed);
        editor.commit();
    }

    public boolean isFirstTime() {
        return pref.getBoolean(IS_IT_FIRST_TIME, false);
    }

    public void setIsFirstTime(boolean isFirstTime) {
        editor.putBoolean(IS_IT_FIRST_TIME, isFirstTime);
        editor.commit();
    }

}

package com.insider.sanjuanisland;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.insider.sanjuanisland.database.DataBaseAdapter;
import com.insider.sanjuanisland.database.TableCreation;
import com.insider.sanjuanisland.models.AbtModel;
import com.insider.sanjuanisland.models.AllCategoriesModel;
import com.insider.sanjuanisland.models.ClientDetailsModel;
import com.insider.sanjuanisland.models.LocationsModel;
import com.insider.sanjuanisland.models.Parent_Child_Relationships_Model;
import com.insider.sanjuanisland.utils.CommonMethod;
import com.insider.sanjuanisland.utils.ConnectionDetector;
import com.insider.sanjuanisland.utils.Session;
import com.insider.sanjuanisland.utils.Webservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HomeScreenActivity extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout exploreButton, points_button, rewards_button;
    ImageView bg_image, header_logo, rewards_button_img;
    DataBaseAdapter mDataBaseAdapter;
    ConnectionDetector mConnectionDetector;
    ProgressDialog mProgressDialog;
    Webservice mWebservice;
    Session mSession;
    CommonMethod mCommonMethod;
    TextView header_description;
    boolean isResultSuccess = false, isImageUpdated = false;
    ClientDetailsModel mClientDetailsModel;
    Button yourAccButton,aboutButton, tipsButton;
    Integer pageNumber = 1, getAllLocationRelationPageNo = 1;
    String[] ids;
    String dateFormat = "MM/dd/yyyy HH:mm:ss a";
    int screenWidth, screenHeight;
    private static final int REQUEST_WRITE_STORAGE = 112;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        //Hide Action bar
        getSupportActionBar().hide();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        mDataBaseAdapter = new DataBaseAdapter(this);
        mWebservice = new Webservice(this);
        mSession = new Session(this);
        mCommonMethod = new CommonMethod(this);
        mProgressDialog = new ProgressDialog(HomeScreenActivity.this);
        mConnectionDetector = new ConnectionDetector(this);
        bg_image = (ImageView) findViewById(R.id.bg_image);
        header_logo = (ImageView) findViewById(R.id.header_logo);
        exploreButton = (RelativeLayout) findViewById(R.id.explore_button);
        exploreButton.setOnClickListener(this);
        points_button = (RelativeLayout) findViewById(R.id.points_button);
        points_button.setOnClickListener(this);
        rewards_button = (RelativeLayout) findViewById(R.id.rewards_button);
        rewards_button.setOnClickListener(this);
        yourAccButton = (Button) findViewById(R.id.your_acc_button);
        yourAccButton.setOnClickListener(this);
        aboutButton = (Button) findViewById(R.id.about_button);
        aboutButton.setOnClickListener(this);
        tipsButton = (Button) findViewById(R.id.tips_button);
        tipsButton.setOnClickListener(this);
        rewards_button_img = (ImageView) findViewById(R.id.rewards_button_img);

        header_description = (TextView) findViewById(R.id.header_description);
        header_description.setMovementMethod(new ScrollingMovementMethod());
        mClientDetailsModel = mDataBaseAdapter.getClientDetails();

        if (getIntent().getBooleanExtra("IsFromHome", true)) {
            if (mConnectionDetector.isConnectingToInternet()) {
                JSONObject mJsonObject = new JSONObject();
                try {
                    mJsonObject.put("key", mCommonMethod.mKey);
                    mJsonObject.put("userId", mSession.getUserId());
                    mJsonObject.put("clientId", mCommonMethod.mClientId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Call the Async Task
                new UpdatingLocationsAsync().execute(mJsonObject.toString());
            }else {
                mCommonMethod.alertDialog(getResources().getString(R.string.no_internet_abt_page_text), null);
                if (mClientDetailsModel != null) {
                    if (ActivityCompat.checkSelfPermission(HomeScreenActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(HomeScreenActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_WRITE_STORAGE);
                    }else {
                        loadImages();
                    }
                }
            }
        }else if (mClientDetailsModel != null) {
            Log.i("Date",mClientDetailsModel.getUpdated());
            if (ActivityCompat.checkSelfPermission(HomeScreenActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(HomeScreenActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);
            }else {
                loadImages();
            }
        }

    }

    //Parse and check app is registered successfully or not
    private boolean registerAppUseParse(String result) {
        boolean isResultSuccess = false;
        try {
            JSONObject mJsonObject = new JSONObject(result);
            if (mJsonObject.getBoolean("Success")) {
                isResultSuccess = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return isResultSuccess;
    }

    //Load the background and logo images
    private void loadImages(){
        if (mCommonMethod.checkDrawableImageAvail(mClientDetailsModel.getBkg_Image())) {
            Drawable mDrawable = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                mDrawable = getResources().getDrawable(mCommonMethod.getDrawableID(mClientDetailsModel.getBkg_Image()),getTheme());
            }else{
                mDrawable = getResources().getDrawable(mCommonMethod.getDrawableID(mClientDetailsModel.getBkg_Image()));
            }
            bg_image.setImageDrawable(mDrawable);
        } else if (mCommonMethod.checkImageAvail(mClientDetailsModel.getBkg_Image(), mClientDetailsModel.getUpdated(), dateFormat)) {
            try {
                File picPath = mCommonMethod.getOutputPath(mClientDetailsModel.getBkg_Image(), mClientDetailsModel.getUpdated(), dateFormat);
                bg_image.setImageBitmap(mCommonMethod.decodeFile(picPath));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (!mCommonMethod.checkImageAvail(mClientDetailsModel.getBkg_Image(), mClientDetailsModel.getUpdated(), dateFormat)) {
            mCommonMethod.setImageToView(mClientDetailsModel.getBkg_Image(), mClientDetailsModel.getUpdated(), bg_image, dateFormat);
        }


        if (mCommonMethod.checkDrawableImageAvail(mClientDetailsModel.getLogo_Image())) {
            Drawable mDrawable = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                mDrawable = getResources().getDrawable(mCommonMethod.getDrawableID(mClientDetailsModel.getLogo_Image()),getTheme());
            }else{
                mDrawable = getResources().getDrawable(mCommonMethod.getDrawableID(mClientDetailsModel.getLogo_Image()));
            }
            header_logo.setImageDrawable(mDrawable);
        } else if (mCommonMethod.checkImageAvail(mClientDetailsModel.getLogo_Image(), mClientDetailsModel.getUpdated(), dateFormat)) {
            try {
                File picPath = mCommonMethod.getOutputPath(mClientDetailsModel.getLogo_Image(), mClientDetailsModel.getUpdated(), dateFormat);
                header_logo.setImageBitmap(mCommonMethod.decodeFile(picPath));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (!mCommonMethod.checkImageAvail(mClientDetailsModel.getLogo_Image(), mClientDetailsModel.getUpdated(), dateFormat)) {
            mCommonMethod.setImageToView(mClientDetailsModel.getLogo_Image(), mClientDetailsModel.getUpdated(), header_logo, dateFormat);
        }

        header_description.setText(mClientDetailsModel.getDescription());
    }

    //Parse and get the client details
    private ClientDetailsModel getClientDetailsParse(String result) {
        ClientDetailsModel mClientDetailsModel = new ClientDetailsModel();
        try {
            JSONObject mJsonObject = new JSONObject(result);
            if (mJsonObject.getBoolean("success")) {
                mClientDetailsModel.setClient_ID(mJsonObject.getInt("Client_ID"));
                mClientDetailsModel.setClient_Status_ID(mJsonObject.getInt("Client_Status_ID"));
                mClientDetailsModel.setClient_Name(mJsonObject.getString("Client_Name"));
                mClientDetailsModel.setApp_Name(mJsonObject.getString("App_Name"));
                mClientDetailsModel.setLogo_Image(mJsonObject.getString("Logo_Image"));
                mClientDetailsModel.setBkg_Image(mJsonObject.getString("Bkg_Image"));
                mClientDetailsModel.setDescription(mJsonObject.getString("Description"));
                mClientDetailsModel.setReward_Code(mJsonObject.getInt("Reward_Code"));
                mClientDetailsModel.setMap_GPS(mJsonObject.getString("Map_GPS"));
                mClientDetailsModel.setMap_Radius(mJsonObject.getString("Map_Radius"));
                mClientDetailsModel.setCreated(mJsonObject.getString("Created"));
                mClientDetailsModel.setUpdated(mJsonObject.getString("Updated"));
                mClientDetailsModel.setMessage(mJsonObject.getString("Message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mClientDetailsModel;
    }

    //Parse and get the Abt details
    private AbtModel getAbtDetailsParse(String result) {
        AbtModel mAbtModel = new AbtModel();
        try {
            JSONObject mJsonObject = new JSONObject(result);
            if (mJsonObject.getBoolean("Success")) {
                if(mJsonObject.getBoolean("UpdatedContentAvavilable")){
                    mAbtModel.setTitle(mJsonObject.getString("Title"));
                    mAbtModel.setContent(mJsonObject.getString("Content"));
                    mAbtModel.setCreated(mJsonObject.getString("Created"));
                    mAbtModel.setUpdated(mJsonObject.getString("Updated"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mAbtModel;
    }

    //Parse and get all the Location details
    private List<LocationsModel> getAllLocationDetailsParse(String result) {
        List<LocationsModel> locationsModelList = new ArrayList<>();
        try {
            JSONObject mJsonObject = new JSONObject(result);
            if (mJsonObject.getBoolean("Success")) {
                JSONArray mJsonArray = mJsonObject.getJSONArray("LocationModelResult");
                ids = new String[mJsonArray.length()];
                for (int i = 0; i < mJsonArray.length(); i++) {
                    JSONObject object = mJsonArray.getJSONObject(i);
                    LocationsModel mLocationsModel = new LocationsModel();
                    mLocationsModel.setLocation_ID(object.getInt("Location_ID"));
                    ids[i] = String.valueOf(object.getInt("Location_ID"));
                    mLocationsModel.setClient_ID(object.getInt("Client_ID"));
                    mLocationsModel.setLocation_Name(object.getString("Location_Name"));
                    mLocationsModel.setLocation_Status_ID(object.getInt("Location_Status_ID"));
                    mLocationsModel.setMap_GPS(object.getString("Map_GPS"));
                    mLocationsModel.setDescription(object.getString("Description"));
                    mLocationsModel.setPoint_Collection_Radius(object.getString("Point_Collection_Radius"));
                    mLocationsModel.setPhoto(object.getString("Photo"));
                    mLocationsModel.setAddress1(object.getString("Address1"));
                    mLocationsModel.setAddress2(object.getString("Address2"));
                    mLocationsModel.setCity(object.getString("City"));
                    mLocationsModel.setState(object.getString("State"));
                    mLocationsModel.setCountry(object.getString("Country"));
                    mLocationsModel.setZip(object.getString("Zip"));
                    mLocationsModel.setPhone(object.getString("Phone"));
                    mLocationsModel.setEmail(object.getString("Email"));
                    mLocationsModel.setURL(object.getString("URL"));
                    mLocationsModel.setFacebook_URL(object.getString("Facebook_URL"));
                    mLocationsModel.setPinterest_URL(object.getString("Pinterest_URL"));
                    mLocationsModel.setTwitter_URL(object.getString("Twitter_URL"));
                    mLocationsModel.setInstaGram_URL(object.getString("InstaGram_URL"));
                    mLocationsModel.setParent_Location(object.getBoolean("Parent_Location"));
                    mLocationsModel.setCategory_ID(object.getInt("Category_ID"));
                    mLocationsModel.setReward_Points_Earned(object.getInt("Reward_Points_Earned"));
                    mLocationsModel.setLocation_Type(object.getInt("Location_Type"));
                    mLocationsModel.setReward_Description(object.getString("Reward_Description"));
                    mLocationsModel.setCreated(object.getString("Created"));
                    mLocationsModel.setReward_Points_Required(object.getInt("Reward_Points_Required"));
                    mLocationsModel.setUpdated(object.getString("Updated"));
                    locationsModelList.add(mLocationsModel);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return locationsModelList;
    }

    //Parse and get all the Category details
    private List<AllCategoriesModel> getAllCategoriesParse(String result) {
        List<AllCategoriesModel> categoriesModelsList = new ArrayList<>();
        try {
            JSONObject mJsonObject = new JSONObject(result);
            if (mJsonObject.getBoolean("Success")) {
                JSONArray mJsonArray = mJsonObject.getJSONArray("CategoryList");
                for (int i = 0; i < mJsonArray.length(); i++) {
                    JSONObject object = mJsonArray.getJSONObject(i);
                    AllCategoriesModel mAllCategoriesModel = new AllCategoriesModel();
                    mAllCategoriesModel.setCategory_ID(object.getInt("Category_ID"));
                    mAllCategoriesModel.setCategory_Name(object.getString("Category_Name"));
                    mAllCategoriesModel.setClient_ID(object.getInt("Client_ID"));
                    categoriesModelsList.add(mAllCategoriesModel);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return categoriesModelsList;
    }

    //Parse and get all the GetAllLocationRelation details
    private List<Parent_Child_Relationships_Model> getAllLocationRelationParse(String result) {
        List<Parent_Child_Relationships_Model> parentChildRelationshipsModelList = new ArrayList<>();
        try {
            JSONObject mJsonObject = new JSONObject(result);
            if (mJsonObject.getBoolean("Success")) {
                JSONArray mJsonArray = mJsonObject.getJSONArray("LocationRelations");
                for (int i = 0; i < mJsonArray.length(); i++) {
                    JSONObject object = mJsonArray.getJSONObject(i);
                    Parent_Child_Relationships_Model mParent_child_relationships_model = new Parent_Child_Relationships_Model();
                    mParent_child_relationships_model.setParent_Child_Relationship_ID(object.getInt("Parent_Child_Relationship_ID"));
                    mParent_child_relationships_model.setParent_Location_ID(object.getInt("Parent_Location_ID"));
                    mParent_child_relationships_model.setChild_Location_ID(object.getInt("Child_Location_ID"));
                    parentChildRelationshipsModelList.add(mParent_child_relationships_model);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parentChildRelationshipsModelList;
    }

    //Parse and get all the Location details success or not
    private boolean getAllLocationResponseCheck(String result) {
        try {
            JSONObject mJsonObject = new JSONObject(result);
            if (mJsonObject.getBoolean("Success")) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        Integer id = v.getId();
        switch (id) {
            case R.id.explore_button:
                Intent exploreIntent = new Intent(HomeScreenActivity.this,
                        ExploreActivity.class);
                exploreIntent.putExtra("IsFromHome", true);
                startActivity(exploreIntent);
                break;
            case R.id.points_button:
                if(mSession.isUserLoggedIn()){
                    Intent pointsHistoryIntent = new Intent(HomeScreenActivity.this, PointsHistoryActivity.class);
                    pointsHistoryIntent.putExtra("IsFromHome", true);
                    startActivity(pointsHistoryIntent);
                }else {
                    Intent pointsLoginIntent = new Intent(HomeScreenActivity.this, LoginActivity.class);
                    pointsLoginIntent.putExtra("IsFromHome", true);
                    pointsLoginIntent.putExtra("gotoPoints", true);
                    startActivity(pointsLoginIntent);
                }
                break;
            case R.id.rewards_button:
                Intent exploreListIntent = new Intent(HomeScreenActivity.this, ExploreListActivity.class);
                exploreListIntent.putExtra("IsFromHome", true);
                exploreListIntent.putExtra("ListRewards", true);
                startActivity(exploreListIntent);
                break;
            case R.id.about_button:
                Intent aboutIntent = new Intent(HomeScreenActivity.this, AboutAndTipsActivity.class);
                aboutIntent.putExtra("IsAbout", true);
                aboutIntent.putExtra("IsFromHome", true);
                startActivity(aboutIntent);
                break;
            case R.id.tips_button:
                Intent tipsIntent = new Intent(HomeScreenActivity.this, AboutAndTipsActivity.class);
                tipsIntent.putExtra("IsAbout", false);
                tipsIntent.putExtra("IsFromHome", true);
                startActivity(tipsIntent);
                break;
            case R.id.your_acc_button:
                if(mSession.isUserLoggedIn()){
                    Intent userProfileIntent = new Intent(HomeScreenActivity.this, UserProfileActivity.class);
                    userProfileIntent.putExtra("IsFromHome", true);
                    startActivity(userProfileIntent);
                }else {
                    Intent loginIntent = new Intent(HomeScreenActivity.this, LoginActivity.class);
                    loginIntent.putExtra("IsFromHome", true);
                    startActivity(loginIntent);
                }
                break;
        }
    }



    //Update Locations Async
    public class UpdatingLocationsAsync extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mProgressDialog.setTitle(getResources().getString(R.string.app_name));
            mProgressDialog.setMessage(getResources().getString(R.string.main_screen_progress_text));
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String registerAppUserResult = null, mGetClientDetailsResult = null, mGetAllCategories = null;
            //Register app web service
            registerAppUserResult = mWebservice.registerAppUse(params[0]);
            isResultSuccess = registerAppUseParse(registerAppUserResult);
            if (isResultSuccess) {
                mGetClientDetailsResult = mWebservice.GetClientDetails(params[0]);
                mClientDetailsModel = getClientDetailsParse(mGetClientDetailsResult);
                if (mClientDetailsModel != null) {
                    String mUpdatedDate = mDataBaseAdapter.getDate();
                    if (mUpdatedDate.equalsIgnoreCase("")) {
                        isImageUpdated = true;
                        mDataBaseAdapter.insertClientDetails(mClientDetailsModel);
                    } else {
                        Log.i("mUpdatedDate", mUpdatedDate);
                        if (!mUpdatedDate.equalsIgnoreCase(mClientDetailsModel.getUpdated())) {
                            isImageUpdated = true;
                            mDataBaseAdapter.updateClientDetails(mClientDetailsModel);
                        }
                    }
                }


                //get all locations
                String mLocationId = mDataBaseAdapter.getLocationIdByMaxDate();
                String mMaxDate = mDataBaseAdapter.getMaxDate(mLocationId);
                getAllLocationValues(mMaxDate,pageNumber);
                //get all relations
                JSONObject mAbtJsonObject = new JSONObject();
                try {
                    mAbtJsonObject.put("key", mCommonMethod.mKey);
                    mAbtJsonObject.put("clientId", mCommonMethod.mClientId);
                    mAbtJsonObject.put("date", mDataBaseAdapter.getAbtUpdatedDate(TableCreation.About.TABLE_NAME));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mDataBaseAdapter.deleteRecords(TableCreation.Parent_Child_Relationships.TABLE_NAME);
                getAllLocationRelationValues(getAllLocationRelationPageNo);
                //get all categories
                JSONObject mJsonObject = new JSONObject();
                try {
                    mJsonObject.put("key", mCommonMethod.mKey);
                    mJsonObject.put("clientId", mCommonMethod.mClientId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mGetAllCategories = mWebservice.GetAllCategories(mJsonObject.toString());
                List<AllCategoriesModel> allCategoriesModelList = getAllCategoriesParse(mGetAllCategories);
                if(allCategoriesModelList!=null && allCategoriesModelList.size()>0) {
                    mDataBaseAdapter.deleteRecords(TableCreation.AllCategories.TABLE_NAME);
                    mDataBaseAdapter.insertAllCategories(allCategoriesModelList);
                }

                //For About Details
                String abtResult = mWebservice.GetAbtTipInformation(mAbtJsonObject.toString(), "GetAboutContent");
                if (!abtResult.equalsIgnoreCase("") && !abtResult.equalsIgnoreCase("Error")) {
                    Log.i("abtResult", abtResult);
                    AbtModel mAbtModel = getAbtDetailsParse(abtResult);
                    if(mAbtModel.getTitle()!=null) {
                        mDataBaseAdapter.deleteRecords(TableCreation.About.TABLE_NAME);
                        mDataBaseAdapter.insertAbtDetails(mAbtModel, TableCreation.About.TABLE_NAME);
                    }
                }
                //For Tips and support
                JSONObject mTipJsonObject = new JSONObject();
                try {
                    mTipJsonObject.put("key", mCommonMethod.mKey);
                    mTipJsonObject.put("clientId", mCommonMethod.mClientId);
                    mTipJsonObject.put("date", mDataBaseAdapter.getAbtUpdatedDate(TableCreation.TipsAndSupport.TABLE_NAME));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String tipsResult = mWebservice.GetAbtTipInformation(mAbtJsonObject.toString(), "GetTSContent");
                if(!tipsResult.equalsIgnoreCase("")&&!tipsResult.equalsIgnoreCase("Error")) {
                    Log.i("abtResult", tipsResult);
                    AbtModel mAbtModel = getAbtDetailsParse(tipsResult);
                    if(mAbtModel.getTitle()!=null){
                        mDataBaseAdapter.deleteRecords(TableCreation.TipsAndSupport.TABLE_NAME);
                        mDataBaseAdapter.insertAbtDetails(mAbtModel, TableCreation.TipsAndSupport.TABLE_NAME);
                    }
                }
            }
            return registerAppUserResult;
        }


        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Log.i("result", result);
                if (ActivityCompat.checkSelfPermission(HomeScreenActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(HomeScreenActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_WRITE_STORAGE);
                }else {
                    loadImages();
                }
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //reload my activity with permission granted or use the features what required the permission
                    loadImages();
                } else {
                    Toast.makeText(HomeScreenActivity.this,
                            "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
        }

    }


    @Override
    protected void onDestroy() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        super.onDestroy();
    }

    public void getAllLocationValues(String maxDate, Integer mPageNumber) {
        String mGetAllLocationsResult = null;
        JSONObject mJsonObject = new JSONObject();
        try {
            mJsonObject.put("key", mCommonMethod.mKey);
            mJsonObject.put("clientId", mCommonMethod.mClientId);
            mJsonObject.put("numberofRecordsPerPage", "100");
            mJsonObject.put("pageNumber", mPageNumber);
            mJsonObject.put("date", maxDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("pageNumber",""+mPageNumber);
        Log.i("mUpdatedCountryDate", maxDate);

        mGetAllLocationsResult = mWebservice.GetAllLocations(mJsonObject.toString());
        if (getAllLocationResponseCheck(mGetAllLocationsResult)) {
            List<LocationsModel> locationsModelList = getAllLocationDetailsParse(mGetAllLocationsResult);
            if (locationsModelList != null && locationsModelList.size() > 0) {
                //showProgress("Fetching Locations "+"("+locationsModelList.size()+")");
                if(locationsModelList.size()<=100) {
                    mDataBaseAdapter.deleteRecords(ids);
                    mDataBaseAdapter.insertLocationDetails(locationsModelList);
                }
                if(locationsModelList.size()==100) {
                    pageNumber++;
                    getAllLocationValues(maxDate, pageNumber);
                }
            }
        }
    }

    public void getAllLocationRelationValues(Integer mPageNumber) {
        String mGetAllLocationRelation = null;
        JSONObject mJsonObject = new JSONObject();
        try {
            mJsonObject.put("key", mCommonMethod.mKey);
            mJsonObject.put("clientId", mCommonMethod.mClientId);
            mJsonObject.put("numberofRecordsPerPage", "100");
            mJsonObject.put("pageNumber", mPageNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.i("getAllLocationRelationPageNo",""+getAllLocationRelationPageNo);

        mGetAllLocationRelation = mWebservice.GetAllLocationRelation(mJsonObject.toString());
        if (getAllLocationResponseCheck(mGetAllLocationRelation)) {
            List<Parent_Child_Relationships_Model> parentChildRelationshipsModelList = getAllLocationRelationParse(mGetAllLocationRelation);
            if (parentChildRelationshipsModelList != null && parentChildRelationshipsModelList.size() > 0) {
                if(parentChildRelationshipsModelList.size()<=100) {
                    mDataBaseAdapter.insertAllLocationRelation(parentChildRelationshipsModelList);
                }
                if(parentChildRelationshipsModelList.size()==100) {
                    getAllLocationRelationPageNo++;
                    getAllLocationRelationValues(getAllLocationRelationPageNo);
                }
            }
        }
    }

}

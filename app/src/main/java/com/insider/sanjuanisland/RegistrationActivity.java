package com.insider.sanjuanisland;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.insider.sanjuanisland.database.DataBaseAdapter;
import com.insider.sanjuanisland.models.ClientDetailsModel;
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

public class RegistrationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener  {

    ProgressDialog mProgressDialog;
    Webservice mWebservice;
    ImageView bg_image;
    ConnectionDetector mConnectionDetector;
    ClientDetailsModel mClientDetailsModel;
    DataBaseAdapter mDataBaseAdapter;
    Session mSession;
    CommonMethod mCommonMethod;
    DrawerLayout drawer;
    private LinearLayout frame;
    List<String> mCountryNames = new ArrayList<>();
    List<String> mCountryIds = new ArrayList<>();
    List<String> mStateNames = new ArrayList<>();
    List<Integer> mStateIds = new ArrayList<>();
    List<String> mStateCode = new ArrayList<>();
    String selectedCountry, selectedCountryId, selectedStateSpinnerId;
    Spinner countrySpinner, stateSpinner;
    LinearLayout state_container, state_spinner_container, register_sections_container;
    RelativeLayout create_account_button;
    EditText email_edit_text, password_edit_text, re_enter_password_edit_text,
            first_name_edit_text, last_name_edit_text, phone_edit_text, state_edit_text,
            city_edit_text;
    String dateFormat = "MM/dd/yyyy HH:mm:ss a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (getIntent().getBooleanExtra("IsFromHome", false)) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        frame = (LinearLayout)findViewById(R.id.content_frame);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        /*Align current page based on Navigation drawer width*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float moveFactor = (drawerView.getWidth() * slideOffset);
                frame.setTranslationX(-moveFactor);
                drawer.bringChildToFront(drawerView);
                drawer.requestLayout();
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        if (getIntent().getBooleanExtra("IsFromHome", false)) {

            toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // event when click home button
                    onBackPressed();
                }
            });
        }
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        toggle.setDrawerIndicatorEnabled(false);

        //slide Menu
        TextView lblListHome = (TextView) header.findViewById(R.id.lblListHome);
        lblListHome.setOnClickListener(this);
        TextView lblListExplore = (TextView) header.findViewById(R.id.lblListExplore);
        lblListExplore.setOnClickListener(this);
        TextView lblListMapExplore = (TextView) header.findViewById(R.id.lblListMapExplore);
        lblListMapExplore.setOnClickListener(this);
        TextView lblListExploreList = (TextView) header.findViewById(R.id.lblListExploreList);
        lblListExploreList.setOnClickListener(this);
        TextView lblListRewards = (TextView) header.findViewById(R.id.lblListRewards);
        lblListRewards.setOnClickListener(this);
        TextView lblListRewardsList = (TextView) header.findViewById(R.id.lblListRewardsList);
        lblListRewardsList.setOnClickListener(this);
        TextView lblListMapRewards = (TextView) header.findViewById(R.id.lblListMapRewards);
        lblListMapRewards.setOnClickListener(this);
        TextView lblListYourAcc = (TextView) header.findViewById(R.id.lblListYourAcc);
        lblListYourAcc.setOnClickListener(this);
        TextView lblListAbout = (TextView) header.findViewById(R.id.lblListAbout);
        lblListAbout.setOnClickListener(this);
        TextView lblListTipsSupport = (TextView) header.findViewById(R.id.lblListTipsSupport);
        lblListTipsSupport.setOnClickListener(this);
        TextView lblListPoints = (TextView) header.findViewById(R.id.lblListPoints);
        lblListPoints.setOnClickListener(this);
        //Initialize variables and Objects
        mDataBaseAdapter = new DataBaseAdapter(this);
        mWebservice = new Webservice(this);
        mSession = new Session(this);
        mCommonMethod = new CommonMethod(this);
        mProgressDialog = new ProgressDialog(RegistrationActivity.this);
        mConnectionDetector = new ConnectionDetector(this);
        mClientDetailsModel = mDataBaseAdapter.getClientDetails();
        bg_image = (ImageView) findViewById(R.id.bg_image);
        countrySpinner = (Spinner) findViewById(R.id.country_spinner);
        stateSpinner = (Spinner) findViewById(R.id.state_spinner);
        state_container = (LinearLayout) findViewById(R.id.state_container);
        state_spinner_container = (LinearLayout) findViewById(R.id.state_spinner_container);
        create_account_button = (RelativeLayout) findViewById(R.id.create_account_button);
        create_account_button.setOnClickListener(this);
        email_edit_text = (EditText) findViewById(R.id.email_edit_text);
        password_edit_text = (EditText) findViewById(R.id.password_edit_text);
        first_name_edit_text = (EditText) findViewById(R.id.first_name_edit_text);
        last_name_edit_text = (EditText) findViewById(R.id.last_name_edit_text);
        re_enter_password_edit_text = (EditText) findViewById(R.id.re_enter_password_edit_text);
        phone_edit_text = (EditText) findViewById(R.id.phone_edit_text);
        state_edit_text = (EditText) findViewById(R.id.state_edit_text);
        city_edit_text = (EditText) findViewById(R.id.city_edit_text);
        register_sections_container = (LinearLayout) findViewById(R.id.register_sections_container);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int temp_y = (int) ((width)/2.0f);
        int testheight =(int) (width*0.65);
        // Gets the layout params that will allow you to resize the layout
        ViewGroup.LayoutParams params = register_sections_container.getLayoutParams();
        // Changes the height and width to the specified *pixels*
        params.width = temp_y;
        if(getResources().getBoolean(R.bool.isTab)) {
            params.width = temp_y;
            register_sections_container.setLayoutParams(params);
        }else{
            params.width = width;
            register_sections_container.setLayoutParams(params);
        }
        //Loading the background image
        if (mClientDetailsModel != null) {
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
        }

        if (mConnectionDetector.isConnectingToInternet()) {
            JSONObject mJsonObject = new JSONObject();
            try {
                mJsonObject.put("key", mCommonMethod.mKey);
                mJsonObject.put("clientId", mCommonMethod.mClientId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Call the Async Task
            new GetCountryandStatesAsync().execute(mJsonObject.toString());
        }
        else {
            mCommonMethod.alertDialog(getResources().getString(R.string.no_internet_abt_page_text), null);
        }
        //Country spinner
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCountryId = mCountryIds.get(position);
                if(mCountryIds.get(position).equalsIgnoreCase("US")){
                    state_spinner_container.setVisibility(View.VISIBLE);
                    state_container.setVisibility(View.GONE);
                }else {
                    state_spinner_container.setVisibility(View.GONE);
                    state_container.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //State Spinner
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStateSpinnerId = mStateCode.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //City Keyboard Done Listener
        city_edit_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    //do something
                    registrationValidation();
                }
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.explore, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_menu:
                if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                    drawer.closeDrawer(Gravity.RIGHT);
                } else {
                    drawer.openDrawer(Gravity.RIGHT);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lblListHome:
                Intent homeIntent = new Intent(RegistrationActivity.this, HomeScreenActivity.class);
                homeIntent.putExtra("IsFromHome", false);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homeIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListExplore:
                Intent listExploreIntent = new Intent(RegistrationActivity.this, ExploreActivity.class);
                listExploreIntent.putExtra("IsFromHome", false);
                listExploreIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(listExploreIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListMapExplore:
                Intent lblListMapExploreIntent = new Intent(RegistrationActivity.this, ExploreActivity.class);
                lblListMapExploreIntent.putExtra("IsFromHome", false);
                lblListMapExploreIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(lblListMapExploreIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListExploreList:
                Intent lblListExploreListIntent = new Intent(RegistrationActivity.this, ExploreListActivity.class);
                lblListExploreListIntent.putExtra("IsFromHome", false);
                lblListExploreListIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(lblListExploreListIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListRewards:
                Intent lblListRewardsListIntent = new Intent(RegistrationActivity.this, ExploreListActivity.class);
                lblListRewardsListIntent.putExtra("IsFromHome", false);
                lblListRewardsListIntent.putExtra("ListRewards", true);
                lblListRewardsListIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(lblListRewardsListIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListRewardsList:
                Intent listRewardsIntent = new Intent(RegistrationActivity.this, ExploreListActivity.class);
                listRewardsIntent.putExtra("IsFromHome", false);
                listRewardsIntent.putExtra("ListRewards", true);
                listRewardsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(listRewardsIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListMapRewards:
                Intent listMapRewardsIntent = new Intent(RegistrationActivity.this, ExploreActivity.class);
                listMapRewardsIntent.putExtra("IsFromHome", false);
                listMapRewardsIntent.putExtra("ListRewards", true);
                listMapRewardsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(listMapRewardsIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListYourAcc:
                if(mSession.isUserLoggedIn()){
                    Intent userProfileIntent = new Intent(RegistrationActivity.this, UserProfileActivity.class);
                    userProfileIntent.putExtra("IsFromHome", false);
                    userProfileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(userProfileIntent);
                }else {
                    Intent loginIntent = new Intent(RegistrationActivity.this, LoginActivity.class);
                    loginIntent.putExtra("IsFromHome", false);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(loginIntent);
                }
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListAbout:
                Intent lblListAboutIntent = new Intent(RegistrationActivity.this, AboutAndTipsActivity.class);
                lblListAboutIntent.putExtra("IsFromHome", false);
                lblListAboutIntent.putExtra("IsAbout", true);
                lblListAboutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(lblListAboutIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListTipsSupport:
                Intent lblListTipsSupportIntent = new Intent(RegistrationActivity.this, AboutAndTipsActivity.class);
                lblListTipsSupportIntent.putExtra("IsFromHome", false);
                lblListTipsSupportIntent.putExtra("IsAbout", false);
                lblListTipsSupportIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(lblListTipsSupportIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.create_account_button:
                registrationValidation();
                break;
            case R.id.lblListPoints:
                if(mSession.isUserLoggedIn()){
                    Intent pointsHistoryIntent = new Intent(RegistrationActivity.this, PointsHistoryActivity.class);
                    pointsHistoryIntent.putExtra("IsFromHome", false);
                    pointsHistoryIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(pointsHistoryIntent);
                    finish();
                    drawer.closeDrawer(GravityCompat.END);
                }else {
                    Intent pointsLoginIntent = new Intent(RegistrationActivity.this, LoginActivity.class);
                    pointsLoginIntent.putExtra("IsFromHome", false);
                    pointsLoginIntent.putExtra("gotoPoints", true);
                    pointsLoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(pointsLoginIntent);
                    finish();
                    drawer.closeDrawer(GravityCompat.END);
                }
                break;
        }
    }

    //Login Validation and call web service
    private void registrationValidation() {
        if (email_edit_text.getText().toString().trim().equalsIgnoreCase("")) {
            mCommonMethod.alertDialog(getResources().getString(R.string.reg_no_email_message), null);
        }else if (password_edit_text.getText().toString().trim().equalsIgnoreCase("")) {
            mCommonMethod.alertDialog(getResources().getString(R.string.reg_no_pass_message), null);
        }else if (re_enter_password_edit_text.getText().toString().trim().equalsIgnoreCase("")) {
            mCommonMethod.alertDialog(getResources().getString(R.string.reg_no_confirm_pass_message), null);
        }else if (first_name_edit_text.getText().toString().trim().equalsIgnoreCase("")) {
            mCommonMethod.alertDialog(getResources().getString(R.string.reg_no_first_name_message), null);
        }else if (city_edit_text.getText().toString().trim().equalsIgnoreCase("")) {
            mCommonMethod.alertDialog(getResources().getString(R.string.reg_no_city_message), null);
        }else if (!mCommonMethod.checkEmail(email_edit_text.getText().toString())) {
            mCommonMethod.alertDialog(getResources().getString(R.string.reg_valid_email_message), null);
        }else if (!password_edit_text.getText().toString().equalsIgnoreCase(re_enter_password_edit_text.getText().toString())) {
            mCommonMethod.alertDialog(getResources().getString(R.string.reg_pass_mismatch_message), null);
        }else if(!selectedCountryId.equalsIgnoreCase("US")&&state_edit_text.getText().toString().equalsIgnoreCase("")){
                mCommonMethod.alertDialog(getResources().getString(R.string.reg_no_state_message), null);
        }else {
            //Check the Internet Connection
            if (mConnectionDetector
                    .isConnectingToInternet()) {

                JSONObject mJsonObject = new JSONObject();
                try {
                    mJsonObject.put("key", mCommonMethod.mKey);
                    mJsonObject.put("email", email_edit_text.getText().toString().trim());
                    mJsonObject.put("clientId", mCommonMethod.mClientId);
                    mJsonObject.put("password", password_edit_text.getText().toString().trim());
                    mJsonObject.put("fname", first_name_edit_text.getText().toString().trim());
                    mJsonObject.put("lname", last_name_edit_text.getText().toString().trim());
                    mJsonObject.put("phone", phone_edit_text.getText().toString().trim());
                    if(selectedCountryId.equalsIgnoreCase("US")){
                        mJsonObject.put("country", "US");
                        mJsonObject.put("state", selectedStateSpinnerId);
                    }else {
                        mJsonObject.put("country", selectedCountryId);
                        mJsonObject.put("state", state_edit_text.getText().toString());
                    }
                    mJsonObject.put("city", city_edit_text.getText().toString().trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Calling Registration Web Service
                new RegisterService().execute(mJsonObject.toString(),
                        "UserRegistration");
            } else {
                mCommonMethod.alertDialog(getResources().getString(R.string.no_internet_abt_page_text), null);
            }
        }

    }

    //Update Information Async
    public class RegisterService extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setTitle(getResources().getString(R.string.app_name));
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = null;
            //Login web service
            result = mWebservice.UserLogin(params[0], params[1]);

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Log.i("result", result);
                parseRegistrationInfo(result);
            }
            mProgressDialog.dismiss();
        }
    }

    //Parse and set the Registration Info
    private void parseRegistrationInfo(String result) {
        try {
            JSONObject mJsonObject = new JSONObject(result);
            if (mJsonObject.getBoolean("Success")) {
                mSession.setUserInfo(mJsonObject.getString("UserId"),mJsonObject.getString("ClientName"),
                        mJsonObject.getString("User_Status"),mJsonObject.getString("Email"),mJsonObject.getString("First_Name"),
                        mJsonObject.getString("Last_Name"),mJsonObject.getString("Phone"),mJsonObject.getString("Country"),
                        mJsonObject.getString("State"),mJsonObject.getString("City"),mJsonObject.getInt("Total_Points_Earned")
                        ,mJsonObject.getInt("Total_Points_Redeemed"));
                if (getIntent().getBooleanExtra("gotoExplore", false)) {
                    finish();
                }else  if (getIntent().getBooleanExtra("gotoPoints", false)) {
                    Intent pointsHistoryIntent = new Intent(RegistrationActivity.this, PointsHistoryActivity.class);
                    pointsHistoryIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    pointsHistoryIntent.putExtra("IsFromHome", true);
                    startActivity(pointsHistoryIntent);
                    finish();
                }else {
                    Intent userProfileIntent = new Intent(RegistrationActivity.this, UserProfileActivity.class);
                    userProfileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(userProfileIntent);
                    finish();
                }
            } else if (!mJsonObject.getBoolean("Success")) {
                mCommonMethod.alertDialog(mJsonObject.getString("Message"), null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Update Information Async
    public class GetCountryandStatesAsync extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setTitle(getResources().getString(R.string.app_name));
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String countriesResult = null, statesResult = null;
            //Countries web service
            countriesResult = mWebservice.GetCountriesStates(params[0], "GetCountries");
            parseCountriesInfo(countriesResult);
            //States web service
            statesResult = mWebservice.GetCountriesStates(params[0], "GetStates");
            parseStatesInfo(statesResult);
            return statesResult;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Log.i("result", result);
                if (mCountryNames != null && mCountryNames.size() > 0) {
                    // Creating adapter for spinner
                    ArrayAdapter<String> countryDataAdapter = new ArrayAdapter<String>(RegistrationActivity.this,
                            R.layout.spinner_item, mCountryNames);
                    // Drop down layout style - list view with radio button
                    countryDataAdapter.setDropDownViewResource(R.layout.spinner_item);
                    // attaching data adapter to spinner
                    countrySpinner.setAdapter(countryDataAdapter);

                    int mIndex = mCountryNames.indexOf(selectedCountry);
                    try {
                        if (mIndex != -1) {
                            countrySpinner.setSelection(mIndex);
                        }
                    } catch (ArrayIndexOutOfBoundsException ae) {
                        ae.printStackTrace();
                    }
                }
                if (mStateNames != null && mStateNames.size() > 0) {
                    // Creating adapter for spinner
                    ArrayAdapter<String> statesDataAdapter = new ArrayAdapter<String>(RegistrationActivity.this,
                            R.layout.spinner_item, mStateNames);
                    // Drop down layout style - list view with radio button
                    statesDataAdapter.setDropDownViewResource(R.layout.spinner_item);
                    // attaching data adapter to spinner
                    stateSpinner.setAdapter(statesDataAdapter);
                }

            }
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
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

    //Parse and set the Countries Info
    private void parseCountriesInfo(String result) {
        try {
            JSONObject mJsonObject = new JSONObject(result);
            if (mJsonObject.getBoolean("Success")) {
                JSONArray mCountry = mJsonObject.getJSONArray("CountryList");
                for (int i = 0; i < mCountry.length(); i++) {
                    JSONObject mSingleCountry = mCountry.getJSONObject(i);
                    mCountryNames.add(mSingleCountry.getString("name"));
                    mCountryIds.add(mSingleCountry.getString("id"));
                    if(mSingleCountry.getString("id").equalsIgnoreCase("US")){
                        selectedCountry = mSingleCountry.getString("name");
                    }
                }
            } else if (!mJsonObject.getBoolean("Success")) {
                mCommonMethod.alertDialog(mJsonObject.getString("Message"), null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Parse and set the States Info
    private void parseStatesInfo(String result) {
        try {
            JSONObject mJsonObject = new JSONObject(result);
            if (mJsonObject.getBoolean("Success")) {
                JSONArray mStates = mJsonObject.getJSONArray("StateList");
                for (int i = 0; i < mStates.length(); i++) {
                    JSONObject mSingleState = mStates.getJSONObject(i);
                    mStateNames.add(mSingleState.getString("name"));
                    mStateIds.add(mSingleState.getInt("id"));
                    mStateCode.add(mSingleState.getString("code"));
                }
            } else if (!mJsonObject.getBoolean("Success")) {
                mCommonMethod.alertDialog(mJsonObject.getString("Message"), null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

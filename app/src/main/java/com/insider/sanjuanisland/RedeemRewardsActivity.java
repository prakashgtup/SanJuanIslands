package com.insider.sanjuanisland;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.insider.sanjuanisland.database.DataBaseAdapter;
import com.insider.sanjuanisland.models.ClientDetailsModel;
import com.insider.sanjuanisland.models.LocationsModel;
import com.insider.sanjuanisland.utils.CommonMethod;
import com.insider.sanjuanisland.utils.ConnectionDetector;
import com.insider.sanjuanisland.utils.Session;
import com.insider.sanjuanisland.utils.Webservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class RedeemRewardsActivity extends AppCompatActivity implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener{
    ProgressDialog mProgressDialog;
    Webservice mWebservice;
    ImageView bg_image;
    ConnectionDetector mConnectionDetector;
    ClientDetailsModel mClientDetailsModel;
    DataBaseAdapter mDataBaseAdapter;
    Session mSession;
    CommonMethod mCommonMethod;
    EditText redeem_edit_text;
    String dateFormat = "MM/dd/yyyy HH:mm:ss a";
    Button no1Btn, no2Btn, no3Btn, no4Btn, no5Btn, no6Btn, no7Btn, no8Btn, no9Btn;
    TextView Clear_txt, user_point_info_txt, points_subtitle, points_description, redeem_rewards_button_txt;
    Integer mTotal_Points_Earned = 0, mTotal_Points_Redeemed = 0, mTotal_Points_Avail = 0;
    LocationsModel mLocationsModel = null;
    LinearLayout point_sections_container;
    DrawerLayout drawer;
    private LinearLayout frame;
    RelativeLayout redeem_rewards_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_rewards);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // event when click home button
                onBackPressed();
            }
        });

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
        //Initialize variables and objects
        mDataBaseAdapter = new DataBaseAdapter(this);
        mWebservice = new Webservice(this);
        mSession = new Session(this);
        mCommonMethod = new CommonMethod(this);
        mProgressDialog = new ProgressDialog(RedeemRewardsActivity.this);
        mConnectionDetector = new ConnectionDetector(this);
        mClientDetailsModel = mDataBaseAdapter.getClientDetails();
        bg_image = (ImageView) findViewById(R.id.bg_image);
        redeem_edit_text = (EditText) findViewById(R.id.redeem_edit_text);
        redeem_edit_text.setInputType(InputType.TYPE_NULL);
        Clear_txt = (TextView) findViewById(R.id.Clear_txt);
        Clear_txt.setOnClickListener(this);
        redeem_rewards_button = (RelativeLayout) findViewById(R.id.redeem_rewards_button);
        redeem_rewards_button.setOnClickListener(this);
        redeem_rewards_button_txt = (TextView) findViewById(R.id.redeem_rewards_button_txt);
        user_point_info_txt = (TextView) findViewById(R.id.user_point_info_txt);
        points_subtitle = (TextView) findViewById(R.id.points_subtitle);
        points_description = (TextView) findViewById(R.id.points_description);
        point_sections_container = (LinearLayout) findViewById(R.id.point_sections_container);
        no1Btn = (Button) findViewById(R.id.no1);
        no1Btn.setOnClickListener(this);
        no2Btn = (Button) findViewById(R.id.no2);
        no2Btn.setOnClickListener(this);
        no3Btn = (Button) findViewById(R.id.no3);
        no3Btn.setOnClickListener(this);
        no4Btn = (Button) findViewById(R.id.no4);
        no4Btn.setOnClickListener(this);
        no5Btn = (Button) findViewById(R.id.no5);
        no5Btn.setOnClickListener(this);
        no6Btn = (Button) findViewById(R.id.no6);
        no6Btn.setOnClickListener(this);
        no7Btn = (Button) findViewById(R.id.no7);
        no7Btn.setOnClickListener(this);
        no8Btn = (Button) findViewById(R.id.no8);
        no8Btn.setOnClickListener(this);
        no9Btn = (Button) findViewById(R.id.no9);
        no9Btn.setOnClickListener(this);
        //Load background image
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
        //get the object value
        mLocationsModel = getIntent().getParcelableExtra("Location");

        if (mConnectionDetector.isConnectingToInternet()) {
            JSONObject mJsonObject = new JSONObject();
            try {
                mJsonObject.put("key", mCommonMethod.mKey);
                mJsonObject.put("userId", mSession.getUserId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Call the Async Task
            new GetDataAsync().execute(mJsonObject.toString());
        }else {
            mCommonMethod.alertDialog(getResources().getString(R.string.no_internet_abt_page_text), null);
        }

    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
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
        if (id == R.id.action_menu) {
            if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                drawer.closeDrawer(Gravity.RIGHT);
            } else {
                drawer.openDrawer(Gravity.RIGHT);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Editable str =  redeem_edit_text.getText();
        switch (v.getId()){
            case R.id.lblListHome:
                Intent homeIntent = new Intent(RedeemRewardsActivity.this, HomeScreenActivity.class);
                homeIntent.putExtra("IsFromHome", false);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homeIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListExplore:
                Intent listExploreIntent = new Intent(RedeemRewardsActivity.this, ExploreActivity.class);
                listExploreIntent.putExtra("IsFromHome", false);
                listExploreIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(listExploreIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListMapExplore:
                Intent lblListMapExploreIntent = new Intent(RedeemRewardsActivity.this, ExploreActivity.class);
                lblListMapExploreIntent.putExtra("IsFromHome", false);
                lblListMapExploreIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(lblListMapExploreIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListExploreList:
                Intent lblListExploreListIntent = new Intent(RedeemRewardsActivity.this, ExploreListActivity.class);
                lblListExploreListIntent.putExtra("IsFromHome", false);
                lblListExploreListIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(lblListExploreListIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListRewards:
                Intent lblListRewardsListIntent = new Intent(RedeemRewardsActivity.this, ExploreListActivity.class);
                lblListRewardsListIntent.putExtra("IsFromHome", false);
                lblListRewardsListIntent.putExtra("ListRewards", true);
                lblListRewardsListIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(lblListRewardsListIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListRewardsList:
                Intent listRewardsIntent = new Intent(RedeemRewardsActivity.this, ExploreListActivity.class);
                listRewardsIntent.putExtra("IsFromHome", false);
                listRewardsIntent.putExtra("ListRewards", true);
                listRewardsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(listRewardsIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListMapRewards:
                Intent listMapRewardsIntent = new Intent(RedeemRewardsActivity.this, ExploreActivity.class);
                listMapRewardsIntent.putExtra("IsFromHome", false);
                listMapRewardsIntent.putExtra("ListRewards", true);
                listMapRewardsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(listMapRewardsIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListYourAcc:
                if(mSession.isUserLoggedIn()){
                    Intent userProfileIntent = new Intent(RedeemRewardsActivity.this, UserProfileActivity.class);
                    userProfileIntent.putExtra("IsFromHome", false);
                    userProfileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(userProfileIntent);
                }else {
                    Intent loginIntent = new Intent(RedeemRewardsActivity.this, LoginActivity.class);
                    loginIntent.putExtra("IsFromHome", false);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(loginIntent);
                }
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListAbout:
                Intent lblListAboutIntent = new Intent(RedeemRewardsActivity.this, AboutAndTipsActivity.class);
                lblListAboutIntent.putExtra("IsFromHome", false);
                lblListAboutIntent.putExtra("IsAbout", true);
                lblListAboutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(lblListAboutIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListTipsSupport:
                Intent lblListTipsSupportIntent = new Intent(RedeemRewardsActivity.this, AboutAndTipsActivity.class);
                lblListTipsSupportIntent.putExtra("IsFromHome", false);
                lblListTipsSupportIntent.putExtra("IsAbout", false);
                lblListTipsSupportIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(lblListTipsSupportIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListPoints:
                if(mSession.isUserLoggedIn()){
                    Intent pointsHistoryIntent = new Intent(RedeemRewardsActivity.this, PointsHistoryActivity.class);
                    pointsHistoryIntent.putExtra("IsFromHome", false);
                    pointsHistoryIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(pointsHistoryIntent);
                    finish();
                    drawer.closeDrawer(GravityCompat.END);
                }else {
                    Intent pointsLoginIntent = new Intent(RedeemRewardsActivity.this, LoginActivity.class);
                    pointsLoginIntent.putExtra("IsFromHome", false);
                    pointsLoginIntent.putExtra("gotoPoints", true);
                    pointsLoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(pointsLoginIntent);
                    finish();
                    drawer.closeDrawer(GravityCompat.END);
                }
                break;
            case R.id.no1:
                str = str.append("1");
                redeem_edit_text.setText(str);
                break;
            case R.id.no2:
                str = str.append("2");
                redeem_edit_text.setText(str);
                break;
            case R.id.no3:
                str = str.append("3");
                redeem_edit_text.setText(str);
                break;
            case R.id.no4:
                str = str.append("4");
                redeem_edit_text.setText(str);
                break;
            case R.id.no5:
                str = str.append("5");
                redeem_edit_text.setText(str);
                break;
            case R.id.no6:
                str = str.append("6");
                redeem_edit_text.setText(str);
                break;
            case R.id.no7:
                str = str.append("7");
                redeem_edit_text.setText(str);
                break;
            case R.id.no8:
                str = str.append("8");
                redeem_edit_text.setText(str);
                break;
            case R.id.no9:
                str = str.append("9");
                redeem_edit_text.setText(str);
                break;
            case R.id.Clear_txt:
                int length = str.length();
                if (length > 0) {
                    str.delete(length - 1, length);
                }
                break;
            case R.id.redeem_rewards_button:
                if(!redeem_edit_text.getText().toString().equalsIgnoreCase("")) {
                    Integer mCodeEntered = Integer.parseInt(redeem_edit_text.getText().toString());
                    if (mClientDetailsModel.getReward_Code().equals(mCodeEntered)) {
                        if (mConnectionDetector.isConnectingToInternet()) {
                            JSONObject mJsonObject = new JSONObject();
                            try {
                                mJsonObject.put("key", mCommonMethod.mKey);
                                mJsonObject.put("userId", mSession.getUserId());
                                mJsonObject.put("clientId", mCommonMethod.mClientId);
                                mJsonObject.put("LocationId", mLocationsModel.getLocation_ID());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //Call the Async Task
                            new RedeemPointsAsync().execute(mJsonObject.toString());
                        }else {
                            mCommonMethod.alertDialog(getResources().getString(R.string.no_internet_abt_page_text), null);
                        }
                    }else{
                        mCommonMethod.alertDialog(getResources().getString(R.string.code_not_accept_message),null);
                    }
                }
                break;
        }
    }



    //Update Information Async
    public class GetDataAsync extends AsyncTask<String, String, String> {
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
            String registerAppUserResult = null, mGetClientDetailsResult = null;
            //Register app web service
            registerAppUserResult = mWebservice.GetAbtTipInformation(params[0], "GetUserLocationAchievements");

            return registerAppUserResult;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Log.i("result", result);
                getClientDetailsParse(result);
            }
            mProgressDialog.dismiss();
        }
    }

    //Redeem Points
    public class RedeemPointsAsync extends AsyncTask<String, String, String> {
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
            String registerAppUserResult = null, mGetClientDetailsResult = null;
            //Register app web service
            registerAppUserResult = mWebservice.GetAbtTipInformation(params[0], "RewardsRedemption");

            return registerAppUserResult;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Log.i("result", result);
                getRedeemResultParse(result);
            }
            mProgressDialog.dismiss();
        }
    }
    //Parse and set the title and content
    private void getRedeemResultParse(String result) {
        try {
            JSONObject mJsonObject = new JSONObject(result);

            if (mJsonObject.getBoolean("Success")) {
                if(mLocationsModel.getLocation_ID().equals(mJsonObject.getInt("LocationId"))) {
                    Integer pointsAvail = mTotal_Points_Avail - mLocationsModel.getReward_Points_Required();
                    mSession.setTotalPointsEarned(pointsAvail);
                    String mMessage = "Reward Redemption Confirmed: " + mLocationsModel.getReward_Points_Required() + " points" +
                            " have been deducted from " + mSession.getFirstName() + " account. " + "They now have " + pointsAvail +
                            " points available. " + "Please hand the phone back to the customer and apply the designated rewards" +
                            " offer defined by your establishment.";
                    mCommonMethod.alertDialog(mMessage,null);
                } else {
                    mCommonMethod.alertDialog(mJsonObject.getString("Message"),null);
                }
            } else if (!mJsonObject.getBoolean("Success")) {
                mCommonMethod.alertDialog(mJsonObject.getString("Message"),null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    //Parse and set the title and content
    private void getClientDetailsParse(String result) {
        try {
            JSONObject mJsonObject = new JSONObject(result);

            if (mJsonObject.getBoolean("Success")) {


            }else  if (!mJsonObject.getBoolean("Success")) {
                //mCommonMethod.alertDialog(mJsonObject.getString("Message"),null);
            }

            mTotal_Points_Redeemed =mJsonObject.getInt("TotalRedeemedPoints");

            JSONArray PointsEarnedList = mJsonObject.getJSONArray("PointsEarnedList");
            if(PointsEarnedList!=null&& PointsEarnedList.length()>0){
                for(int i=0;i<PointsEarnedList.length();i++){
                    JSONObject singlePointEarnedItem = PointsEarnedList.getJSONObject(i);
                    Log.i("Loc",singlePointEarnedItem.getString("LocationId"));
                        if(singlePointEarnedItem.getString("TotalPointsEarned")!=null) {
                            mLocationsModel.setReward_Points_Earned(Integer.parseInt(singlePointEarnedItem.getString("TotalPointsEarned")));
                            mTotal_Points_Earned = mTotal_Points_Earned+Integer.parseInt(singlePointEarnedItem.getString("TotalPointsEarned"));
                        }
                }

            }

            if(mSession.isUserLoggedIn()){
                mTotal_Points_Avail = mTotal_Points_Earned - mTotal_Points_Redeemed;
                Log.i("mTotal_Points_Avail",mTotal_Points_Avail+"");
                String mTitle = mSession.getFirstName()+" - "+mTotal_Points_Avail+ " points" + " available";
                user_point_info_txt.setText(mTitle);
                mSession.setTotalPointsEarned(mTotal_Points_Earned);
                mSession.setTotalPointsRedeemed(mTotal_Points_Redeemed);
                if(mLocationsModel!=null){
                    points_subtitle.setText(mLocationsModel.getLocation_Name());
                    Log.i("points for Location",mLocationsModel.getReward_Points_Required()+"");
                    String mDescription;
                    if(mTotal_Points_Avail<mLocationsModel.getReward_Points_Required()){
                        mDescription = "Sorry, "+mSession.getFirstName()+", "+getResources().getString(R.string.not_able_to_redeem);
                        point_sections_container.setVisibility(View.GONE);
                    }else{
                        mDescription = mSession.getFirstName()+" - "+"Please hand your phone to the rewards location representative to redeem your reward. "+
                                mLocationsModel.getLocation_Name()+" - "+mSession.getFirstName()+" has "+mTotal_Points_Avail+ " points available. "+
                                "Please enter your assigned redemption code below to deduct "+mLocationsModel.getReward_Points_Required()+
                                " from this user’s account and apply towards your designated rewards offer at your establishment. Please enter the code only once and wait for the confirmation screen to appear.";
                        point_sections_container.setVisibility(View.VISIBLE);
                    }
                    points_description.setText(mDescription);
                    redeem_rewards_button_txt.setText("REDEEM "+mLocationsModel.getReward_Points_Required()+" POINTS");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

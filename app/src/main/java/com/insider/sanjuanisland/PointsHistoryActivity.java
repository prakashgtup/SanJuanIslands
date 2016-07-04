package com.insider.sanjuanisland;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.List;

public class PointsHistoryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener  {

    ProgressDialog mProgressDialog;
    Webservice mWebservice;
    ImageView bg_image;
    ConnectionDetector mConnectionDetector;
    ClientDetailsModel mClientDetailsModel;
    DataBaseAdapter mDataBaseAdapter;
    Session mSession;
    CommonMethod mCommonMethod;
    TextView points_description ;
    DrawerLayout drawer;
    private LinearLayout frame;
    RelativeLayout discover_places_button;
    String dateFormat = "MM/dd/yyyy HH:mm:ss a";
    ListView mExploreListView;
    List<LocationsModel> mLocationsList = new ArrayList<>();
    ExploreListAdapter mExploreListAdapter;
    Integer mTotal_Points_Earned = 0, mTotal_Points_Redeemed = 0, mTotal_Points_Avail = 0;
    TextView rewards_points_earned_txt, rewards_points_redeemed_txt,rewards_points_avail_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_history);
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
        mProgressDialog = new ProgressDialog(PointsHistoryActivity.this);
        mConnectionDetector = new ConnectionDetector(this);
        mClientDetailsModel = mDataBaseAdapter.getClientDetails();
        bg_image = (ImageView) findViewById(R.id.bg_image);
        points_description = (TextView) findViewById(R.id.points_description);
        discover_places_button = (RelativeLayout) findViewById(R.id.discover_places_button);
        discover_places_button.setOnClickListener(this);
        mExploreListView = (ListView) findViewById(R.id.explore_listview);
        rewards_points_earned_txt = (TextView) findViewById(R.id.rewards_points_earned_txt);
        rewards_points_redeemed_txt = (TextView) findViewById(R.id.rewards_points_redeemed_txt);
        rewards_points_avail_txt = (TextView) findViewById(R.id.rewards_points_avail_txt);
        //Load background Image
        if (mClientDetailsModel != null) {
            if (mCommonMethod.checkDrawableImageAvail(mClientDetailsModel.getBkg_Image())) {
                Drawable mDrawable = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
        } else {
            mCommonMethod.alertDialog(getResources().getString(R.string.no_internet_abt_page_text), null);
        }
        //check the internet connection
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
        switch (v.getId()){
            case R.id.lblListHome:
                Intent homeIntent = new Intent(PointsHistoryActivity.this, HomeScreenActivity.class);
                homeIntent.putExtra("IsFromHome", false);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homeIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListExplore:
                Intent listExploreIntent = new Intent(PointsHistoryActivity.this, ExploreActivity.class);
                listExploreIntent.putExtra("IsFromHome", false);
                listExploreIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(listExploreIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListMapExplore:
                Intent lblListMapExploreIntent = new Intent(PointsHistoryActivity.this, ExploreActivity.class);
                lblListMapExploreIntent.putExtra("IsFromHome", false);
                lblListMapExploreIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(lblListMapExploreIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListExploreList:
                Intent lblListExploreListIntent = new Intent(PointsHistoryActivity.this, ExploreListActivity.class);
                lblListExploreListIntent.putExtra("IsFromHome", false);
                lblListExploreListIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(lblListExploreListIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListRewards:
                Intent lblListRewardsListIntent = new Intent(PointsHistoryActivity.this, ExploreListActivity.class);
                lblListRewardsListIntent.putExtra("IsFromHome", false);
                lblListRewardsListIntent.putExtra("ListRewards", true);
                lblListRewardsListIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(lblListRewardsListIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListRewardsList:
                Intent listRewardsIntent = new Intent(PointsHistoryActivity.this, ExploreListActivity.class);
                listRewardsIntent.putExtra("IsFromHome", false);
                listRewardsIntent.putExtra("ListRewards", true);
                listRewardsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(listRewardsIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListMapRewards:
                Intent listMapRewardsIntent = new Intent(PointsHistoryActivity.this, ExploreActivity.class);
                listMapRewardsIntent.putExtra("IsFromHome", false);
                listMapRewardsIntent.putExtra("ListRewards", true);
                listMapRewardsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(listMapRewardsIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListYourAcc:
                if(mSession.isUserLoggedIn()){
                    Intent userProfileIntent = new Intent(PointsHistoryActivity.this, UserProfileActivity.class);
                    userProfileIntent.putExtra("IsFromHome", false);
                    userProfileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(userProfileIntent);
                }else {
                    Intent loginIntent = new Intent(PointsHistoryActivity.this, LoginActivity.class);
                    loginIntent.putExtra("IsFromHome", false);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(loginIntent);
                }
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListAbout:
                Intent lblListAboutIntent = new Intent(PointsHistoryActivity.this, AboutAndTipsActivity.class);
                lblListAboutIntent.putExtra("IsFromHome", false);
                lblListAboutIntent.putExtra("IsAbout", true);
                lblListAboutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(lblListAboutIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListTipsSupport:
                Intent lblListTipsSupportIntent = new Intent(PointsHistoryActivity.this, AboutAndTipsActivity.class);
                lblListTipsSupportIntent.putExtra("IsFromHome", false);
                lblListTipsSupportIntent.putExtra("IsAbout", false);
                lblListTipsSupportIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(lblListTipsSupportIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.discover_places_button:
                Intent exploreListIntent = new Intent(PointsHistoryActivity.this, ExploreListActivity.class);
                exploreListIntent.putExtra("IsFromHome", true);
                exploreListIntent.putExtra("ListRewards", true);
                startActivity(exploreListIntent);
                break;
            case R.id.lblListPoints:
                drawer.closeDrawer(GravityCompat.END);
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
                   LocationsModel mLocationsModel =mDataBaseAdapter.getLocationsDetail(singlePointEarnedItem.getInt("LocationId"));
                   if(mLocationsModel!=null) {
                       Log.i("Loc", mLocationsModel.getLocation_Name());
                       if(singlePointEarnedItem.getString("TotalPointsEarned")!=null) {
                           mLocationsModel.setReward_Points_Earned(Integer.parseInt(singlePointEarnedItem.getString("TotalPointsEarned")));
                           mLocationsList.add(mLocationsModel);
                           mTotal_Points_Earned = mTotal_Points_Earned+Integer.parseInt(singlePointEarnedItem.getString("TotalPointsEarned"));
                       }
                   }
               }
                mExploreListAdapter = new ExploreListAdapter(PointsHistoryActivity.this, mLocationsList);
                mExploreListView.setAdapter(mExploreListAdapter);

            }

            if(mSession.isUserLoggedIn()){
                mTotal_Points_Avail = mTotal_Points_Earned - mTotal_Points_Redeemed;
                Log.i("mTotal_Points_Avail",mTotal_Points_Avail+"");
                points_description.setText(mSession.getFirstName()+" - "+"you have "+mTotal_Points_Avail+" points available. "+
                        getResources().getString(R.string.point_history_description));
                rewards_points_earned_txt.setText(String.valueOf(mTotal_Points_Earned));
                rewards_points_redeemed_txt.setText(String.valueOf(mTotal_Points_Redeemed));
                rewards_points_avail_txt.setText(String.valueOf(mTotal_Points_Avail));
                mSession.setTotalPointsEarned(mTotal_Points_Earned);
                mSession.setTotalPointsRedeemed(mTotal_Points_Redeemed);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Points History list adapter
    public class ExploreListAdapter extends BaseAdapter {

        LocationsModel mLocationsModel = null;
        List<LocationsModel> mCheckList;
        int i = 0;
        List<Boolean> mSelectedTemp;
        /***********
         * Declare Used Variables
         *********/
        private Activity activity;
        private LayoutInflater inflater = null;

        /*************
         * ExploreListAdapter Constructor
         *****************/
        public ExploreListAdapter(Activity a, List<LocationsModel> checkList) {
            /********** Take passed values **********/
            activity = a;
            /***********  Layout inflator to call external xml layout () ***********/
            inflater = LayoutInflater.from(a);
            mCheckList = checkList;
        }

        public void refresh() {
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mCheckList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /******
         * Depends upon data size called for each row , Create each ListView row
         *****/
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            final ViewHolder holder;

            try {
                if (vi == null) {
                    /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
                    vi = inflater.inflate(R.layout.points_history_list_item, null);
                    /****** View Holder Object to contain tabitem.xml file elements ******/
                    holder = new ViewHolder();
                    holder.location_name_txt = (TextView) vi.findViewById(R.id.location_name_txt);
                    holder.point_txt = (TextView) vi.findViewById(R.id.point_txt);
                    holder.bgLayout = (LinearLayout) vi.findViewById(R.id.list_item_container);
                    /************  Set holder with LayoutInflater ************/
                    vi.setTag(holder);
                } else {
                    holder = (ViewHolder) vi.getTag();
                }
                /***** Get each Model object from Arraylist ********/
                mLocationsModel = mCheckList.get(position);
                /************  Set Model values in Holder elements ***********/
                holder.location_name_txt.setText(mLocationsModel.getLocation_Name());
                holder.point_txt.setText(String.valueOf(mLocationsModel.getReward_Points_Earned()));


                if(position%2==0){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        holder.bgLayout.setBackgroundColor(getResources().getColor(R.color.user_profile_points_more_white_half,activity.getTheme()));
                    }else {
                        holder.bgLayout.setBackgroundColor(getResources().getColor(R.color.user_profile_points_more_white_half));
                    }
                }else {
                    holder.bgLayout.setBackgroundColor(Color.TRANSPARENT);
                }



            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            return vi;
        }

        /*********
         * Create a holder Class to contain inflated xml file elements
         *********/
        public class ViewHolder {
            public TextView location_name_txt,point_txt;
            public LinearLayout bgLayout;
        }
    }

}

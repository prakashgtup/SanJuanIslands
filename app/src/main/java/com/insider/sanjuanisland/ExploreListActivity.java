package com.insider.sanjuanisland;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.insider.sanjuanisland.database.DataBaseAdapter;
import com.insider.sanjuanisland.models.ClientDetailsModel;
import com.insider.sanjuanisland.models.LocationsModel;
import com.insider.sanjuanisland.utils.CommonMethod;
import com.insider.sanjuanisland.utils.ConnectionDetector;
import com.insider.sanjuanisland.utils.Session;
import com.insider.sanjuanisland.utils.Webservice;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class ExploreListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener {
    private static final String TAG = "ExploreListActivity";
    ProgressDialog mProgressDialog;
    Webservice mWebservice;
    ImageView bg_image;
    ConnectionDetector mConnectionDetector;
    ClientDetailsModel mClientDetailsModel;
    DataBaseAdapter mDataBaseAdapter;
    Session mSession;
    CommonMethod mCommonMethod;
    DrawerLayout drawer;
    String dateFormat = "MM/dd/yyyy HH:mm:ss a";
    List<LocationsModel> mLocationsList = new ArrayList<>();
    ExploreListAdapter mExploreListAdapter;
    ListView mExploreListView;
    TextView rewards_description;
    RelativeLayout view_explore_map_btn;
    private LinearLayout frame;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    // Declaring a Location Manager
    protected LocationManager locationManager;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (getIntent().getBooleanExtra("IsFromHome", false)) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        frame = (LinearLayout) findViewById(R.id.content_frame);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        /*align current activity based on navigation drawer width*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
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
        View header = navigationView.getHeaderView(0);
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
        //Initialize the variables and Objects
        mDataBaseAdapter = new DataBaseAdapter(this);
        mWebservice = new Webservice(this);
        mSession = new Session(this);
        mCommonMethod = new CommonMethod(this);
        mProgressDialog = new ProgressDialog(ExploreListActivity.this);
        mConnectionDetector = new ConnectionDetector(this);
        mClientDetailsModel = mDataBaseAdapter.getClientDetails();
        bg_image = (ImageView) findViewById(R.id.bg_image);
        mExploreListView = (ListView) findViewById(R.id.explore_listview);
        view_explore_map_btn = (RelativeLayout) findViewById(R.id.view_explore_map_btn);
        rewards_description = (TextView) findViewById(R.id.rewards_description);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showSettingsAlert();
        }
        //Connect and get the Location
        mGoogleApiClient.connect();


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
        }  else {
            mCommonMethod.alertDialog(getResources().getString(R.string.no_internet_abt_page_text), null);
        }
        //List view single item click
        mExploreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LocationsModel locationsModel = mLocationsList.get(position);
                Intent exploreDetailIntent = new Intent(ExploreListActivity.this, ExploreDetailsActivity.class);
                exploreDetailIntent.putExtra("Location", locationsModel);
                startActivity(exploreDetailIntent);
            }
        });
        //show locations in map
        view_explore_map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent exploreMapIntent = new Intent(ExploreListActivity.this, ExploreActivity.class);
                exploreMapIntent.putExtra("IsFromHome", true);
                if (getIntent().getBooleanExtra("ListRewards", false)) {
                    exploreMapIntent.putExtra("ListRewards", true);
                }
                if (getIntent().getBooleanExtra("ListChildExplore", false)) {
                    exploreMapIntent.putExtra("ListChildExplore", true);
                    exploreMapIntent.putExtra("ParentLocationId",
                            getIntent().getIntExtra("ParentLocationId",0));
                }
                startActivity(exploreMapIntent);
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
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.explore, menu);
        return true;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ExploreListActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle(getResources().getString(R.string.app_name));

        // Setting Dialog Message
        alertDialog.setMessage("Turn on Location Service to Allow \""+getResources().getString(R.string.app_name)+"\" to Determine Your Location.");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                noDistanceLocation();
            }
        });
        alertDialog.setCancelable(false);
        // Showing Alert Message
        alertDialog.show();
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
                Intent homeIntent = new Intent(ExploreListActivity.this, HomeScreenActivity.class);
                homeIntent.putExtra("IsFromHome", false);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homeIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListExplore:
                Intent listExploreIntent = new Intent(ExploreListActivity.this, ExploreActivity.class);
                listExploreIntent.putExtra("IsFromHome", false);
                listExploreIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(listExploreIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListMapExplore:
                Intent lblListMapExploreIntent = new Intent(ExploreListActivity.this, ExploreActivity.class);
                lblListMapExploreIntent.putExtra("IsFromHome", false);
                lblListMapExploreIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(lblListMapExploreIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListExploreList:
                Intent lblListExploreListIntent = new Intent(ExploreListActivity.this, ExploreListActivity.class);
                lblListExploreListIntent.putExtra("IsFromHome", false);
                lblListExploreListIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(lblListExploreListIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListRewards:
                Intent lblListRewardsListIntent = new Intent(ExploreListActivity.this, ExploreListActivity.class);
                lblListRewardsListIntent.putExtra("IsFromHome", false);
                lblListRewardsListIntent.putExtra("ListRewards", true);
                lblListRewardsListIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(lblListRewardsListIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListRewardsList:
                Intent listRewardsIntent = new Intent(ExploreListActivity.this, ExploreListActivity.class);
                listRewardsIntent.putExtra("IsFromHome", false);
                listRewardsIntent.putExtra("ListRewards", true);
                listRewardsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(listRewardsIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListMapRewards:
                Intent listMapRewardsIntent = new Intent(ExploreListActivity.this, ExploreActivity.class);
                listMapRewardsIntent.putExtra("IsFromHome", false);
                listMapRewardsIntent.putExtra("ListRewards", true);
                listMapRewardsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(listMapRewardsIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListYourAcc:
                if (mSession.isUserLoggedIn()) {
                    Intent userProfileIntent = new Intent(ExploreListActivity.this, UserProfileActivity.class);
                    userProfileIntent.putExtra("IsFromHome", false);
                    userProfileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(userProfileIntent);
                } else {
                    Intent loginIntent = new Intent(ExploreListActivity.this, LoginActivity.class);
                    loginIntent.putExtra("IsFromHome", false);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(loginIntent);
                }
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListAbout:
                Intent lblListAboutIntent = new Intent(ExploreListActivity.this, AboutAndTipsActivity.class);
                lblListAboutIntent.putExtra("IsFromHome", false);
                lblListAboutIntent.putExtra("IsAbout", true);
                lblListAboutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(lblListAboutIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListTipsSupport:
                Intent lblListTipsSupportIntent = new Intent(ExploreListActivity.this, AboutAndTipsActivity.class);
                lblListTipsSupportIntent.putExtra("IsFromHome", false);
                lblListTipsSupportIntent.putExtra("IsAbout", false);
                lblListTipsSupportIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(lblListTipsSupportIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListPoints:
                if (mSession.isUserLoggedIn()) {
                    Intent pointsHistoryIntent = new Intent(ExploreListActivity.this, PointsHistoryActivity.class);
                    pointsHistoryIntent.putExtra("IsFromHome", false);
                    pointsHistoryIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(pointsHistoryIntent);
                    finish();
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    Intent pointsLoginIntent = new Intent(ExploreListActivity.this, LoginActivity.class);
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(this, "Kindly check your permission", Toast.LENGTH_LONG).show();
            return;
        }
        getlocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) this
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return location;
                            }
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    private void getlocation() {
        if (mGoogleApiClient.isConnected()) {
            Log.i("", "Connected");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        123);
                return;
            }
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                if (getIntent().getBooleanExtra("ListRewards", false)) {
                    mLocationsList = mDataBaseAdapter.getListRewardsLocationsList();
                    rewards_description.setVisibility(View.VISIBLE);
                    if (mSession.isUserLoggedIn()) {
                        Integer mTotal_Points_Earned = 0, mTotal_Points_Redeemed = 0, mTotal_Points_Avail = 0;
                        mTotal_Points_Earned = mSession.getTotalPointsEarned();
                        mTotal_Points_Redeemed = mSession.getTotalPointsRedeemed();
                        mTotal_Points_Avail = mTotal_Points_Earned - mTotal_Points_Redeemed;
                        Log.i("mTotal_Points_Avail", mTotal_Points_Avail + "");
                        rewards_description.setText(mSession.getFirstName() + " - " + "you have " + mTotal_Points_Avail + " points available. " +
                                getResources().getString(R.string.rewards_description_after_login));
                    } else {
                        rewards_description.setText(getResources().getString(R.string.rewards_description));
                    }

                } else if (getIntent().getBooleanExtra("ListChildExplore", false)) {
                    mLocationsList = mDataBaseAdapter.getLocationsListByParent(getIntent().getIntExtra("ParentLocationId", 0));
                    rewards_description.setVisibility(View.GONE);
                } else {
                    mLocationsList = mDataBaseAdapter.getExploreLocationsList();
                    rewards_description.setVisibility(View.GONE);
                }
                setDistance(mLocationsList);
                Collections.sort(mLocationsList, new CustomComparator());
                if (mLocationsList != null && mLocationsList.size() > 0) {
                    mExploreListAdapter = new ExploreListAdapter(ExploreListActivity.this, mLocationsList);
                    mExploreListView.setAdapter(mExploreListAdapter);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 123: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    getlocation();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    //if location not found show without distance calculation
    private void noDistanceLocation() {
        if (getIntent().getBooleanExtra("ListRewards", false)) {
            mLocationsList = mDataBaseAdapter.getListRewardsLocationsList();
            rewards_description.setVisibility(View.VISIBLE);
            if (mSession.isUserLoggedIn()) {
                Integer mTotal_Points_Earned = 0, mTotal_Points_Redeemed = 0, mTotal_Points_Avail = 0;
                mTotal_Points_Earned = mSession.getTotalPointsEarned();
                mTotal_Points_Redeemed = mSession.getTotalPointsRedeemed();
                mTotal_Points_Avail = mTotal_Points_Earned - mTotal_Points_Redeemed;
                Log.i("mTotal_Points_Avail", mTotal_Points_Avail + "");
                rewards_description.setText(mSession.getFirstName() + " - " + "you have " + mTotal_Points_Avail + " points available. " +
                        getResources().getString(R.string.rewards_description_after_login));
            } else {
                rewards_description.setText(getResources().getString(R.string.rewards_description));
            }

        } else if (getIntent().getBooleanExtra("ListChildExplore", false)) {
            mLocationsList = mDataBaseAdapter.getLocationsListByParent(getIntent().getIntExtra("ParentLocationId", 0));
            rewards_description.setVisibility(View.GONE);
        } else {
            mLocationsList = mDataBaseAdapter.getExploreLocationsList();
            rewards_description.setVisibility(View.GONE);
        }
        setDistance(mLocationsList);
        if (mLocationsList != null && mLocationsList.size() > 0) {
            mExploreListAdapter = new ExploreListAdapter(ExploreListActivity.this, mLocationsList);
            mExploreListView.setAdapter(mExploreListAdapter);
        }
    }

    public void setDistance(List<LocationsModel> tempLocationsModelList) {
        if (tempLocationsModelList != null && tempLocationsModelList.size() > 0) {
            for (LocationsModel mTempLocationModel : tempLocationsModelList) {
                mTempLocationModel.setCategoryName(mDataBaseAdapter.getCategory_Name(mTempLocationModel.getCategory_ID()));
                if (mTempLocationModel.isParent_Location()) {
                    List<LocationsModel> mChildList = mDataBaseAdapter.getLocationsListByParent(mTempLocationModel.getLocation_ID());
                    if (mChildList != null && mChildList.size() > 0) {
                        mTempLocationModel.setNoChildLocations(mChildList.size());
                    }
                } else {
                    mTempLocationModel.setNoChildLocations(0);
                }
                if (mLastLocation != null) {
                    String[] locationsArray = mTempLocationModel.getMap_GPS().split(",");
                    Log.i("Current" + " Latitude", "" + mLastLocation.getLatitude());
                    Log.i("Current" + " Longtitude", "" + mLastLocation.getLongitude());
                    //distance in miles
                    double distance = mCommonMethod.calculateDistance(mLastLocation.getLatitude(), mLastLocation.getLongitude(),
                            Double.parseDouble(locationsArray[0]), Double.parseDouble(locationsArray[1]));
                    Log.i("distance in miles", distance + "");
                    mTempLocationModel.setDistance(distance);
                }else{
                    mTempLocationModel.setDistance(0);
                }
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            mLastLocation = getLocation();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    //Create Location List Item
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
                    vi = inflater.inflate(R.layout.explore_list_item, null);
                    /****** View Holder Object to contain tabitem.xml file elements ******/
                    holder = new ViewHolder();
                    holder.titleTxt = (TextView) vi.findViewById(R.id.explore_item_title);
                    holder.descriptionTxt = (TextView) vi.findViewById(R.id.explore_item__description);
                    holder.milesTxt = (TextView) vi.findViewById(R.id.miletxt);
                    holder.milesValuetxt = (TextView) vi.findViewById(R.id.distance);
                    holder.bgLayout = (RelativeLayout) vi.findViewById(R.id.list_item_container);
                    holder.points_layout_child = (LinearLayout) vi.findViewById(R.id.points_layout_child);
                    holder.collect_point_txt = (TextView) vi.findViewById(R.id.collect_point_txt);
                    holder.reward_points_txt = (TextView) vi.findViewById(R.id.reward_points_txt);
                    holder.or_txt = (TextView) vi.findViewById(R.id.or_txt);
                    holder.explore_item_locations_count = (TextView) vi.findViewById(R.id.explore_item_locations_count);
                    /************  Set holder with LayoutInflater ************/
                    vi.setTag(holder);
                } else {
                    holder = (ViewHolder) vi.getTag();
                }
                /***** Get each Model object from Arraylist ********/
                mLocationsModel = mCheckList.get(position);
                /************  Set Model values in Holder elements ***********/
                holder.titleTxt.setText(mLocationsModel.getLocation_Name());

                if (!mLocationsModel.isParent_Location()) {
                    holder.descriptionTxt.setText(mLocationsModel.getCategoryName());
                    holder.points_layout_child.setVisibility(View.VISIBLE);
                    if (mLocationsModel.getLocation_Type().equals(1)) {
                        String mCollect;
                        mCollect = "Collect: " + mLocationsModel.getReward_Points_Earned() + " points";
                        holder.collect_point_txt.setText(mCollect);
                        holder.collect_point_txt.setVisibility(View.VISIBLE);
                        holder.reward_points_txt.setVisibility(View.GONE);
                        holder.or_txt.setVisibility(View.GONE);
                    } else if (mLocationsModel.getLocation_Type().equals(2)) {
                        String mReward;
                        mReward = "Redeem Reward: " + mLocationsModel.getReward_Points_Required() + " points";
                        holder.reward_points_txt.setText(mReward);
                        holder.collect_point_txt.setVisibility(View.GONE);
                        holder.reward_points_txt.setVisibility(View.VISIBLE);
                        holder.or_txt.setVisibility(View.GONE);
                    } else if (mLocationsModel.getLocation_Type().equals(3)) {
                        String mCollect;
                        mCollect = "Collect: " + mLocationsModel.getReward_Points_Earned() + " points";
                        holder.collect_point_txt.setText(mCollect);
                        String mReward;
                        mReward = "Redeem Reward: " + mLocationsModel.getReward_Points_Required() + " points";
                        holder.reward_points_txt.setText(mReward);
                        holder.collect_point_txt.setVisibility(View.VISIBLE);
                        holder.reward_points_txt.setVisibility(View.VISIBLE);
                        holder.or_txt.setVisibility(View.VISIBLE);
                    }else{
                        holder.points_layout_child.setVisibility(View.GONE);
                    }
                        holder.explore_item_locations_count.setVisibility(View.GONE);
                } else {
                    holder.explore_item_locations_count.setVisibility(View.VISIBLE);
                    holder.explore_item_locations_count.setText(mLocationsModel.getNoChildLocations() + " locations");
                    holder.descriptionTxt.setVisibility(View.GONE);
                    holder.points_layout_child.setVisibility(View.GONE);
                }

                if (position % 2 == 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        holder.bgLayout.setBackgroundColor(getResources().getColor(R.color.user_profile_points_more_white_half, activity.getTheme()));
                    } else {
                        holder.bgLayout.setBackgroundColor(getResources().getColor(R.color.user_profile_points_more_white_half));
                    }
                } else {
                    holder.bgLayout.setBackgroundColor(Color.TRANSPARENT);
                }

                //Log.i("distance in miles", mLocationsModel.getDistance() + "");
                //System.out.println(NumberFormat.getNumberInstance(Locale.US).format(mLocationsModel.getDistance()));
                //holder.milesTxt.setText(String.format("%.2f", mLocationsModel.getDistance()));
                if (mLastLocation != null) {
                    holder.milesValuetxt.setText(NumberFormat.getNumberInstance(Locale.US).format(mLocationsModel.getDistance()));
                }else {
                    holder.milesTxt.setVisibility(View.GONE);
                    holder.milesValuetxt.setVisibility(View.GONE);
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
            public TextView titleTxt, descriptionTxt, milesTxt, milesValuetxt, collect_point_txt, reward_points_txt,
                    explore_item_locations_count, or_txt;
            public LinearLayout points_layout_child;
            public RelativeLayout bgLayout;
        }
    }

    public class CustomComparator implements Comparator<LocationsModel> {
        @Override
        public int compare(LocationsModel o1, LocationsModel o2) {
            return Double.compare(o1.getDistance(), o2.getDistance());
        }
    }


}

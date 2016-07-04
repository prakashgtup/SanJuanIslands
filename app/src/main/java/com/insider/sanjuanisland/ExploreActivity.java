package com.insider.sanjuanisland;

import android.Manifest;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.insider.sanjuanisland.database.DataBaseAdapter;
import com.insider.sanjuanisland.models.ClientDetailsModel;
import com.insider.sanjuanisland.models.LocationsModel;
import com.insider.sanjuanisland.models.SingleMapItem;
import com.insider.sanjuanisland.utils.CommonMethod;
import com.insider.sanjuanisland.utils.ConnectionDetector;
import com.insider.sanjuanisland.utils.ImageValidator;
import com.insider.sanjuanisland.utils.Session;
import com.insider.sanjuanisland.utils.Webservice;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExploreActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        View.OnClickListener, ClusterManager.OnClusterClickListener<SingleMapItem>,
        ClusterManager.OnClusterItemClickListener<SingleMapItem> {
    private static CommonMethod mCommonMethod;
    private static LocationsModel mParentLocationsModel = null;
    private final List<Marker> mMarkerRainbow = new ArrayList<Marker>();
    private final Random mRandom = new Random();
    ProgressDialog mProgressDialog;
    Webservice mWebservice;
    ConnectionDetector mConnectionDetector;
    DataBaseAdapter mDataBaseAdapter;
    Session mSession;
    DrawerLayout drawer;
    List<LocationsModel> mLocationsList = new ArrayList<>();
    RelativeLayout view_explore_list_btn;
    ClientDetailsModel mClientDetailsModel;
    private LinearLayout frame;
    private ClusterManager<SingleMapItem> mClusterManager;
    private GoogleMap mMap;
    /**
     * Keeps track of the last selected marker (though it may no longer be selected).  This is
     * useful for refreshing the info window.
     */
    private Marker mLastSelectedMarker;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lblListHome:
                Intent homeIntent = new Intent(ExploreActivity.this, HomeScreenActivity.class);
                homeIntent.putExtra("IsFromHome", false);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homeIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListExplore:
                Intent listExploreIntent = new Intent(ExploreActivity.this, ExploreActivity.class);
                listExploreIntent.putExtra("IsFromHome", false);
                listExploreIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(listExploreIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListMapExplore:
                Intent lblListMapExploreIntent = new Intent(ExploreActivity.this, ExploreActivity.class);
                lblListMapExploreIntent.putExtra("IsFromHome", false);
                lblListMapExploreIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(lblListMapExploreIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListExploreList:
                Intent lblListExploreListIntent = new Intent(ExploreActivity.this, ExploreListActivity.class);
                lblListExploreListIntent.putExtra("IsFromHome", false);
                lblListExploreListIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(lblListExploreListIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListRewards:
                Intent lblListRewardsListIntent = new Intent(ExploreActivity.this, ExploreListActivity.class);
                lblListRewardsListIntent.putExtra("IsFromHome", false);
                lblListRewardsListIntent.putExtra("ListRewards", true);
                lblListRewardsListIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(lblListRewardsListIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListRewardsList:
                Intent listRewardsIntent = new Intent(ExploreActivity.this, ExploreListActivity.class);
                listRewardsIntent.putExtra("IsFromHome", false);
                listRewardsIntent.putExtra("ListRewards", true);
                listRewardsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(listRewardsIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListMapRewards:
                Intent listMapRewardsIntent = new Intent(ExploreActivity.this, ExploreActivity.class);
                listMapRewardsIntent.putExtra("IsFromHome", false);
                listMapRewardsIntent.putExtra("ListRewards", true);
                listMapRewardsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(listMapRewardsIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListYourAcc:
                if (mSession.isUserLoggedIn()) {
                    Intent userProfileIntent = new Intent(ExploreActivity.this, UserProfileActivity.class);
                    userProfileIntent.putExtra("IsFromHome", false);
                    userProfileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(userProfileIntent);
                } else {
                    Intent loginIntent = new Intent(ExploreActivity.this, LoginActivity.class);
                    loginIntent.putExtra("IsFromHome", false);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(loginIntent);
                }
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListAbout:
                Intent lblListAboutIntent = new Intent(ExploreActivity.this, AboutAndTipsActivity.class);
                lblListAboutIntent.putExtra("IsFromHome", false);
                lblListAboutIntent.putExtra("IsAbout", true);
                lblListAboutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(lblListAboutIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListTipsSupport:
                Intent lblListTipsSupportIntent = new Intent(ExploreActivity.this, AboutAndTipsActivity.class);
                lblListTipsSupportIntent.putExtra("IsFromHome", false);
                lblListTipsSupportIntent.putExtra("IsAbout", false);
                lblListTipsSupportIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(lblListTipsSupportIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListPoints:
                if (mSession.isUserLoggedIn()) {
                    Intent pointsHistoryIntent = new Intent(ExploreActivity.this, PointsHistoryActivity.class);
                    pointsHistoryIntent.putExtra("IsFromHome", false);
                    pointsHistoryIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(pointsHistoryIntent);
                    finish();
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    Intent pointsLoginIntent = new Intent(ExploreActivity.this, LoginActivity.class);
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
    public boolean onClusterClick(Cluster<SingleMapItem> cluster) {
        //by clicking the Clustering marker map will zoom
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cluster.getItems().iterator().next().getPosition(), 12.0f));
        return true;
    }

    @Override
    public boolean onClusterItemClick(SingleMapItem singleMapItem) {
        if (mLocationsList != null && mLocationsList.size() > 0) {
            for (int l = 0; l < mLocationsList.size(); l++) {
                LocationsModel mLocationsModel = mLocationsList.get(l);
                if (mLocationsModel.getLocation_ID().equals(singleMapItem.getLocationID())) {
                    mParentLocationsModel = mLocationsModel;
                }
            }
        }
        FragmentManager fm = getFragmentManager();
        ParentDialogFragment dialogFragment = new ParentDialogFragment();
        dialogFragment.show(fm, "");
        return false;
    }

    //For Add text to marker and Convert as Bitmap image
    private Bitmap writeTextOnDrawable(int drawableId, String text) {

        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
                .copy(Bitmap.Config.ARGB_8888, true);

        Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTypeface(tf);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(convertToPixels(getApplicationContext(), 12));

        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);

        Canvas canvas = new Canvas(bm);

        //If the text is bigger than the canvas , reduce the font size
        if (textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
            paint.setTextSize(convertToPixels(getApplicationContext(), 7));        //Scaling needs to be used for different dpi's

        //Calculate the positions
        int xPos = (canvas.getWidth() / 2) - 2;     //-2 is for regulating the x position offset

        //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

        canvas.drawText(text, xPos, yPos - 2, paint);

        return bm;
    }

    //Supported for Convert dp into pixels
    public int convertToPixels(Context context, int nDP) {
        final float conversionScale = context.getResources().getDisplayMetrics().density;
        return (int) ((nDP * conversionScale) + 0.5f);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
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
        //Align page to left based on navigation drawer width
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

        mDataBaseAdapter = new DataBaseAdapter(this);
        mWebservice = new Webservice(this);
        mSession = new Session(this);
        mCommonMethod = new CommonMethod(this);
        mProgressDialog = new ProgressDialog(ExploreActivity.this);
        mConnectionDetector = new ConnectionDetector(this);
        mClientDetailsModel = mDataBaseAdapter.getClientDetails();

        loadMap();
        toggle.setDrawerIndicatorEnabled(false);

        view_explore_list_btn = (RelativeLayout) findViewById(R.id.view_explore_list_btn);
        view_explore_list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent exploreListIntent = new Intent(ExploreActivity.this, ExploreListActivity.class);
                exploreListIntent.putExtra("IsFromHome", true);
                if (getIntent().getBooleanExtra("ListRewards", false)) {
                    exploreListIntent.putExtra("ListRewards", true);
                }
                if (getIntent().getBooleanExtra("ListChildExplore", false)) {
                    exploreListIntent.putExtra("ListChildExplore", true);
                    exploreListIntent.putExtra("ParentLocationId",
                            getIntent().getIntExtra("ParentLocationId", 0));
                }
                startActivity(exploreListIntent);
            }
        });

    }
    //Initializing the map
    private void loadMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_NORMAL).compassEnabled(true);
        mapFragment.newInstance(options);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(Gravity.RIGHT);
        return true;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        mMap.setMyLocationEnabled(true);
        mClusterManager = new ClusterManager<SingleMapItem>(this, mMap);
        mClusterManager.setRenderer(new PersonRenderer());
        // Add lots of markers to the map.
        addMarkersToMap();
        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);

        mClusterManager
                .setOnClusterClickListener(new ClusterManager.OnClusterClickListener<SingleMapItem>() {
                    @Override
                    public boolean onClusterClick(final Cluster<SingleMapItem> cluster) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                cluster.getPosition(), (float) Math.floor(mMap
                                        .getCameraPosition().zoom + 1)), 300,
                                null);
                        return true;
                    }
                });
        // Override the default content description on the view, for accessibility mode.
        // Ideally this string would be localised.
        mMap.setContentDescription("Map with lots of markers.");
        mClusterManager.cluster();
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
                    loadMap();
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

    private void addMarkersToMap() {
        // Move the camera to the first marker
        LatLng firstMarkerPos = null;
        float mapZoom = 0;
        if (mClientDetailsModel != null) {
            mapZoom = Float.valueOf(mClientDetailsModel.getMap_Radius());
            String[] locationsArray = mClientDetailsModel.getMap_GPS().split(",");
            if (locationsArray.length > 0) {
                firstMarkerPos = new LatLng(Double.parseDouble(locationsArray[0]), Double.parseDouble(locationsArray[1]));
            }
        }

        if (getIntent().getBooleanExtra("ListRewards", false)) {
            mLocationsList = mDataBaseAdapter.getListRewardsLocationsList();
        } else if (getIntent().getBooleanExtra("ListChildExplore", false)) {
            mLocationsList = mDataBaseAdapter.getLocationsListByParent(getIntent().getIntExtra("ParentLocationId", 0));
        } else {
            mLocationsList = mDataBaseAdapter.getExploreLocationsList();
        }
        if (mLocationsList != null && mLocationsList.size() > 0) {
            for (int l = 0; l < mLocationsList.size(); l++) {
                LocationsModel mLocationsModel = mLocationsList.get(l);
                mLocationsModel.setCategoryName(mDataBaseAdapter.getCategory_Name(mLocationsModel.getCategory_ID()));
                if (mLocationsModel.isParent_Location()) {
                    List<LocationsModel> mChildList = mDataBaseAdapter.getLocationsListByParent(mLocationsModel.getLocation_ID());
                    if (mChildList != null && mChildList.size() > 0) {
                        mLocationsModel.setNoChildLocations(mChildList.size());
                    }
                } else {
                    mLocationsModel.setNoChildLocations(0);
                }

                if (mLocationsModel.getMap_GPS() != null &&
                        !mLocationsModel.getMap_GPS().equalsIgnoreCase("")) {
                    String[] locationsArray = mLocationsModel.getMap_GPS().split(",");
                    if (locationsArray.length > 0) {
                        if (mLocationsModel.isParent_Location()) {
                            mClusterManager.addItem(new SingleMapItem(new LatLng(Double.parseDouble(locationsArray[0]), Double.parseDouble(locationsArray[1])),
                                    mLocationsModel.getLocation_Name(), R.drawable.pin_parent, mLocationsModel.isParent_Location(), mLocationsModel.getLocation_ID()));
                        } else {
                            mClusterManager.addItem(new SingleMapItem(new LatLng(Double.parseDouble(locationsArray[0]), Double.parseDouble(locationsArray[1])),
                                    mLocationsModel.getLocation_Name(), R.drawable.pin_standard, mLocationsModel.isParent_Location(), mLocationsModel.getLocation_ID()));
                        }
                    }
                }
            }
        }

        //if its a group of child set parent location as center of the marker
        if (getIntent().getBooleanExtra("ListChildExplore", false)) {
            LocationsModel mLocationsModel = getIntent().getParcelableExtra("Location");
            if (mLocationsModel != null) {
                String[] locationsArray = mLocationsModel.getMap_GPS().split(",");
                if (locationsArray.length > 0) {
                    firstMarkerPos = new LatLng(Double.parseDouble(locationsArray[0]), Double.parseDouble(locationsArray[1]));
                }
            }
        }
        //Move the camera to the user's location and zoom in!
        if (firstMarkerPos != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(firstMarkerPos, mapZoom));
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        // This causes the marker at Adelaide to change color and alpha.

        return false;
    }

    //Used to show marker pop-up for information about that location
    public static class ParentDialogFragment extends DialogFragment {
        ImageView mLocationImage;
        ProgressBar spinner;
        RelativeLayout moreInfoBtn, exploreBtn, child_more_info_button;
        LinearLayout parent_layout_bottom, child_layout_bottom, points_layout_child;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Dialog dialog = new Dialog(getActivity(), R.style.MyThemeDialogCustom);
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            return dialog;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.parent_dialog_layout, container, false);
            //getDialog().setTitle("Simple Dialog");
            TextView titleTxt = (TextView) rootView.findViewById(R.id.parent_title);
            TextView descriptionTxt = (TextView) rootView.findViewById(R.id.parent_description);
            TextView category = (TextView) rootView.findViewById(R.id.parent_category);
            TextView collect_point_txt = (TextView) rootView.findViewById(R.id.collect_point_txt);
            TextView reward_points_txt = (TextView) rootView.findViewById(R.id.reward_points_txt);
            TextView or_txt = (TextView) rootView.findViewById(R.id.or_txt);
            TextView no_image_avail = (TextView) rootView.findViewById(R.id.no_image_avail);
            //descriptionTxt.setMovementMethod(new ScrollingMovementMethod());
            ImageView dismiss = (ImageView) rootView.findViewById(R.id.close_image);
            mLocationImage = (ImageView) rootView.findViewById(R.id.location_image);
            spinner = (ProgressBar) rootView.findViewById(R.id.gl_prgs);
            moreInfoBtn = (RelativeLayout) rootView.findViewById(R.id.more_info_button);
            exploreBtn = (RelativeLayout) rootView.findViewById(R.id.explore_button);
            child_more_info_button = (RelativeLayout) rootView.findViewById(R.id.child_more_info_button);
            parent_layout_bottom = (LinearLayout) rootView.findViewById(R.id.parent_layout_bottom);
            child_layout_bottom = (LinearLayout) rootView.findViewById(R.id.child_layout_bottom);
            points_layout_child = (LinearLayout) rootView.findViewById(R.id.points_layout_child);
            if (mParentLocationsModel != null) {
                if (!mParentLocationsModel.isParent_Location()) {
                    parent_layout_bottom.setVisibility(View.GONE);
                    child_layout_bottom.setVisibility(View.VISIBLE);
                    category.setVisibility(View.VISIBLE);
                    category.setText(mParentLocationsModel.getCategoryName());
                    points_layout_child.setVisibility(View.VISIBLE);
                    if (mParentLocationsModel.getLocation_Type().equals(1)) {
                        String mCollect;
                        mCollect = "Collect: " + mParentLocationsModel.getReward_Points_Earned() + " points";
                        collect_point_txt.setText(mCollect);
                        collect_point_txt.setVisibility(View.VISIBLE);
                        reward_points_txt.setVisibility(View.GONE);
                        or_txt.setVisibility(View.GONE);
                    } else if (mParentLocationsModel.getLocation_Type().equals(2)) {
                        String mReward;
                        mReward = "Redeem Reward: " + mParentLocationsModel.getReward_Points_Required() + " points";
                        reward_points_txt.setText(mReward);
                        collect_point_txt.setVisibility(View.GONE);
                        reward_points_txt.setVisibility(View.VISIBLE);
                        or_txt.setVisibility(View.GONE);
                    } else if (mParentLocationsModel.getLocation_Type().equals(3)) {
                        String mCollect;
                        mCollect = "Collect: " + mParentLocationsModel.getReward_Points_Earned() + " points";
                        collect_point_txt.setText(mCollect);
                        String mReward;
                        mReward = "Redeem Reward: " + mParentLocationsModel.getReward_Points_Required() + " points";
                        reward_points_txt.setText(mReward);
                        collect_point_txt.setVisibility(View.VISIBLE);
                        reward_points_txt.setVisibility(View.VISIBLE);
                        or_txt.setVisibility(View.VISIBLE);
                    } else {
                        points_layout_child.setVisibility(View.GONE);
                    }
                } else {
                    parent_layout_bottom.setVisibility(View.VISIBLE);
                    child_layout_bottom.setVisibility(View.GONE);
                    category.setVisibility(View.VISIBLE);
                    category.setText(mParentLocationsModel.getNoChildLocations() + " locations");
                    points_layout_child.setVisibility(View.GONE);
                }
                titleTxt.setText(mParentLocationsModel.getLocation_Name());
                descriptionTxt.setText(mParentLocationsModel.getDescription());

                exploreBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent exploreMapIntent = new Intent(getActivity(), ExploreActivity.class);
                        exploreMapIntent.putExtra("IsFromHome", true);
                        exploreMapIntent.putExtra("ListChildExplore", true);
                        exploreMapIntent.putExtra("ParentLocationId", mParentLocationsModel.getLocation_ID());
                        exploreMapIntent.putExtra("Location", mParentLocationsModel);
                        startActivity(exploreMapIntent);
                    }
                });

                moreInfoBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent exploreDetailIntent = new Intent(getActivity(), ExploreDetailsActivity.class);
                        exploreDetailIntent.putExtra("Location", mParentLocationsModel);
                        startActivity(exploreDetailIntent);
                    }
                });

                child_more_info_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent exploreDetailIntent = new Intent(getActivity(), ExploreDetailsActivity.class);
                        exploreDetailIntent.putExtra("Location", mParentLocationsModel);
                        startActivity(exploreDetailIntent);
                    }
                });

                Display display = getActivity().getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = width / 2;
                int temp_y = (int) ((width) / 2.0f);
                int testheight = (int) (width * 0.65);
                int testwidth = (int) (width / 1.25f);
                mLocationImage.setLayoutParams(new FrameLayout.LayoutParams(width, temp_y));

                String dateFormat = "MM/dd/yyyy HH:mm:ss.SSSSSS";
                if (!mParentLocationsModel.getPhoto().equalsIgnoreCase("")) {
                    if (new ImageValidator().validate(mParentLocationsModel.getPhoto())) {
                        if (mCommonMethod.checkDrawableImageAvail(mParentLocationsModel.getPhoto())) {
                            Drawable mDrawable = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                mDrawable = getResources().getDrawable(mCommonMethod.getDrawableID(mParentLocationsModel.getPhoto()), getActivity().getTheme());
                            } else {
                                mDrawable = getResources().getDrawable(mCommonMethod.getDrawableID(mParentLocationsModel.getPhoto()));
                            }
                            mLocationImage.setImageDrawable(mDrawable);
                            spinner.setVisibility(View.GONE);
                        } else if (mCommonMethod.checkImageAvail(mParentLocationsModel.getPhoto(), mParentLocationsModel.getUpdated(), dateFormat)) {
                            try {
                                File picPath = mCommonMethod.getOutputPath(mParentLocationsModel.getPhoto(), mParentLocationsModel.getUpdated(), dateFormat);
                                mLocationImage.setImageBitmap(mCommonMethod.decodeFile(picPath));
                                spinner.setVisibility(View.GONE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            mCommonMethod.setImageToView(mParentLocationsModel.getPhoto(), mParentLocationsModel.getUpdated(), mLocationImage, dateFormat);
                            spinner.setVisibility(View.GONE);
                        }
                    } else {
                        spinner.setVisibility(View.GONE);
                        no_image_avail.setVisibility(View.VISIBLE);
                    }
                }

            }

            dismiss.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });


            // Setting dialogview
            Window window = getDialog().getWindow();
            window.setGravity(Gravity.TOP);
            getDialog().setCanceledOnTouchOutside(false);
            //window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            // after that, setting values for x and y works "naturally"
            WindowManager.LayoutParams params = window.getAttributes();
            params.y = 100;
            window.setAttributes(params);
            return rootView;
        }

    }

    /**
     * Draws profile photos inside markers (using IconGenerator).
     * When there are multiple people in the cluster, draw multiple photos (using MultiDrawable).
     */
    private class PersonRenderer extends DefaultClusterRenderer<SingleMapItem> {
        private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        // private final int mDimension;

        public PersonRenderer() {
            super(getApplicationContext(), mMap, mClusterManager);

            View multiProfile = getLayoutInflater().inflate(R.layout.multi_profile, null);
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);

            mImageView = new ImageView(getApplicationContext());
            //mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
            // mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            // int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
            // mImageView.setPadding(padding, padding, padding, padding);
            mIconGenerator.setContentView(mImageView);
        }

        @Override
        protected void onBeforeClusterItemRendered(SingleMapItem singleMapItem, MarkerOptions markerOptions) {
            // Draw a single person.
            // Set the info window to show their name.
            if (singleMapItem.getParent()) {
                //mImageView.setImageResource(R.drawable.pin_parent);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_parent));
            } else {
                // mImageView.setImageResource(R.drawable.pin_standard);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_standard));
            }
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<SingleMapItem> cluster, MarkerOptions markerOptions) {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).


            Bitmap icon = writeTextOnDrawable(R.drawable.pin_group, String.valueOf(cluster.getSize()));
            // mClusterImageView.setImageResource(R.drawable.pin_parent);

            //Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));

        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }
    }

}

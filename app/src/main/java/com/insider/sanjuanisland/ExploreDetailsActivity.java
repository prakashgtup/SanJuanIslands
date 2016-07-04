package com.insider.sanjuanisland;

import android.Manifest;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.insider.sanjuanisland.database.DataBaseAdapter;
import com.insider.sanjuanisland.models.LocationsModel;
import com.insider.sanjuanisland.utils.CommonMethod;
import com.insider.sanjuanisland.utils.ConnectionDetector;
import com.insider.sanjuanisland.utils.ImageResizer;
import com.insider.sanjuanisland.utils.ImageValidator;
import com.insider.sanjuanisland.utils.Session;
import com.insider.sanjuanisland.utils.Webservice;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class ExploreDetailsActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "ExploreDetailsActivity";
    private static final int REQUEST_WRITE_STORAGE = 112;
    ProgressDialog mProgressDialog;
    Webservice mWebservice;
    ConnectionDetector mConnectionDetector;
    DataBaseAdapter mDataBaseAdapter;
    Session mSession;
    CommonMethod mCommonMethod;
    LocationsModel mLocationsModel = null;
    ImageView explore_image, facebook_img, twitter_img, instagram_img, pinterest_img;
    RelativeLayout collect_point_button, explore_button, redeem_rewards_button, share_button;
    TextView titleTxt, category_name_txt, address_txt, telephone_txt, email_txt, website_txt,
            explore_description, reward_description, collect_point_txt, reward_points_txt, or_txt, collect_point_button_txt,
            redeem_rewards_button_txt, no_image_avail;
    LinearLayout address_container, telephone_container, email_container, website_container, follow_us_container, points_layout_child;
    ProgressBar explore_prgs;
    View address_container_view, telephone_container_view, email_container_view, website_container_view,
            follow_us_container_view, reward_description_container_view;
    String dateFormat = "MM/dd/yyyy HH:mm:ss.SSSSSS";
    String[] locationsArray;
    DrawerLayout drawer;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LinearLayout frame;
    int imageWidth, imageHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        frame = (LinearLayout) findViewById(R.id.content_frame);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        /*Align page based on the drawer size*/
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
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // event when click home button
                onBackPressed();
            }
        });

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
        mProgressDialog = new ProgressDialog(ExploreDetailsActivity.this);
        mConnectionDetector = new ConnectionDetector(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        initialize();
        // Fetching data from a parcelable object passed from MainActivity
        mLocationsModel = getIntent().getParcelableExtra("Location");

        if (mLocationsModel != null) {
            Log.i("Location Name", mLocationsModel.getLocation_Name());
            Log.i("Location Date", mLocationsModel.getUpdated());
            //Loading Location Image
            if (!mLocationsModel.getPhoto().equalsIgnoreCase("")) {
                if (new ImageValidator().validate(mLocationsModel.getPhoto())) {
                    if (mCommonMethod.checkDrawableImageAvail(mLocationsModel.getPhoto())) {
                        Drawable mDrawable = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            mDrawable = getResources().getDrawable(mCommonMethod.getDrawableID(mLocationsModel.getPhoto()), getTheme());
                        } else {
                            mDrawable = getResources().getDrawable(mCommonMethod.getDrawableID(mLocationsModel.getPhoto()));
                        }
                        explore_image.setImageDrawable(mDrawable);
                        explore_prgs.setVisibility(View.GONE);
                    } else if (mCommonMethod.checkImageAvail(mLocationsModel.getPhoto(), mLocationsModel.getUpdated(), dateFormat)) {
                        try {
                            File picPath = mCommonMethod.getOutputPath(mLocationsModel.getPhoto(), mLocationsModel.getUpdated(), dateFormat);
                            Log.i("picPath", picPath.toString());
                            ImageResizer imageResizer = new ImageResizer();
                            Bitmap imageBitmap = null;
                            if(imageBitmap!=null){
                                imageBitmap.recycle();
                                imageBitmap = null;
                            }
                            imageBitmap = imageResizer.decodeSampledBitmapFromFile(picPath.toString(),imageWidth,imageHeight);
                            //explore_image.setImageBitmap(mCommonMethod.decodeFile(picPath));
                            explore_image.setImageBitmap(imageBitmap);
                            explore_prgs.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        mCommonMethod.setImageToView(mLocationsModel.getPhoto(), mLocationsModel.getUpdated(), explore_image, dateFormat);
                        explore_prgs.setVisibility(View.GONE);
                    }
                } else {
                    explore_prgs.setVisibility(View.GONE);
                    no_image_avail.setVisibility(View.VISIBLE);
                }
            }
            //Add Title Text
            titleTxt.setText(mLocationsModel.getLocation_Name());
            if (!mLocationsModel.isParent_Location()) {
                category_name_txt.setText(mLocationsModel.getCategoryName());
                points_layout_child.setVisibility(View.VISIBLE);
                if (mLocationsModel.getLocation_Type().equals(1)) {
                    String mCollect;
                    mCollect = "Collect: " + mLocationsModel.getReward_Points_Earned() + " points";
                    collect_point_txt.setText(mCollect);
                    collect_point_txt.setVisibility(View.VISIBLE);
                    reward_points_txt.setVisibility(View.GONE);
                    or_txt.setVisibility(View.GONE);
                } else if (mLocationsModel.getLocation_Type().equals(2)) {
                    String mReward;
                    mReward = "Redeem Reward: " + mLocationsModel.getReward_Points_Required() + " points";
                    reward_points_txt.setText(mReward);
                    collect_point_txt.setVisibility(View.GONE);
                    reward_points_txt.setVisibility(View.VISIBLE);
                    or_txt.setVisibility(View.GONE);
                } else if (mLocationsModel.getLocation_Type().equals(3)) {
                    String mCollect;
                    mCollect = "Collect: " + mLocationsModel.getReward_Points_Earned() + " points";
                    collect_point_txt.setText(mCollect);
                    String mReward;
                    mReward = "Redeem Reward: " + mLocationsModel.getReward_Points_Required() + " points";
                    reward_points_txt.setText(mReward);
                    collect_point_txt.setVisibility(View.VISIBLE);
                    reward_points_txt.setVisibility(View.VISIBLE);
                    or_txt.setVisibility(View.VISIBLE);
                } else {
                    points_layout_child.setVisibility(View.GONE);
                }
            } else {
                category_name_txt.setText(mLocationsModel.getNoChildLocations() + " locations");
                points_layout_child.setVisibility(View.GONE);
            }
            //Add Address
            if (mLocationsModel.getAddress1().equalsIgnoreCase("") &&
                    mLocationsModel.getAddress2().equalsIgnoreCase("")) {
                address_container.setVisibility(View.GONE);
                address_container_view.setVisibility(View.GONE);
            } else {
                address_container.setVisibility(View.VISIBLE);
                StringBuilder address = new StringBuilder();
                address.append(mLocationsModel.getAddress1());
                if (!mLocationsModel.getAddress2().equalsIgnoreCase("")) {
                    address.append("\n" + mLocationsModel.getAddress2());
                }
                if (!mLocationsModel.getCity().equalsIgnoreCase("")) {
                    address.append("\n" + mLocationsModel.getCity());
                }
                if (!mLocationsModel.getState().equalsIgnoreCase("")) {
                    address.append(", " + mLocationsModel.getState());
                }
                if (!mLocationsModel.getZip().equalsIgnoreCase("")) {
                    address.append(", " + mLocationsModel.getZip());
                }
                address_txt.setText(address.toString());
            }
            //add telephone no
            if (mLocationsModel.getPhone().equalsIgnoreCase("")) {
                telephone_container.setVisibility(View.GONE);
                telephone_container_view.setVisibility(View.GONE);
            } else {
                telephone_container.setVisibility(View.VISIBLE);
                telephone_txt.setText(mLocationsModel.getPhone());
                //make telephone call
                telephone_txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mLocationsModel.getPhone()));
                        try {
                            startActivity(in);
                        } catch (android.content.ActivityNotFoundException ex) {
                            ex.printStackTrace();
                            mCommonMethod.alertDialog("No Apps Found", null);
                        }
                    }
                });
            }
            //set email text
            if (mLocationsModel.getEmail().equalsIgnoreCase("")) {
                email_container.setVisibility(View.GONE);
                email_container_view.setVisibility(View.GONE);
            } else {
                email_container.setVisibility(View.VISIBLE);
                email_txt.setText(mLocationsModel.getEmail());
                //send email
                email_txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] TO = {mLocationsModel.getEmail()};
                        String[] CC = {""};
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);

                        emailIntent.setData(Uri.parse("mailto:"));
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                        emailIntent.putExtra(Intent.EXTRA_CC, CC);
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "");

                        try {
                            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
            //add website url
            if (mLocationsModel.getURL().equalsIgnoreCase("")) {
                website_container.setVisibility(View.GONE);
                website_container_view.setVisibility(View.GONE);
            } else {
                website_container.setVisibility(View.VISIBLE);
                website_txt.setText(mLocationsModel.getURL());
                //open website in browser
                website_txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = mLocationsModel.getURL();
                        if (!url.startsWith("http://") && !url.startsWith("https://")) {
                            url = "http://" + url;
                        }
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent);
                    }
                });
            }
            //add description values
            explore_description.setText(mLocationsModel.getDescription());
            //add facebook url
            if (mLocationsModel.getFacebook_URL().equalsIgnoreCase("")) {
                facebook_img.setVisibility(View.GONE);
            } else {
                facebook_img.setVisibility(View.VISIBLE);
                facebook_img.setTag(mLocationsModel.getFacebook_URL());
                facebook_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = mLocationsModel.getFacebook_URL();
                        if (!url.startsWith("http://") && !url.startsWith("https://")) {
                            url = "http://" + url;
                        }
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent);
                    }
                });
            }
            //add twitter url
            if (mLocationsModel.getTwitter_URL().equalsIgnoreCase("")) {
                twitter_img.setVisibility(View.GONE);
            } else {
                twitter_img.setVisibility(View.VISIBLE);
                twitter_img.setTag(mLocationsModel.getTwitter_URL());
                twitter_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = mLocationsModel.getTwitter_URL();
                        if (!url.startsWith("http://") && !url.startsWith("https://")) {
                            url = "http://" + url;
                        }
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent);
                    }
                });
            }
            //add instagram url
            if (mLocationsModel.getInstaGram_URL().equalsIgnoreCase("")) {
                instagram_img.setVisibility(View.GONE);
            } else {
                instagram_img.setVisibility(View.VISIBLE);
                instagram_img.setTag(mLocationsModel.getInstaGram_URL());
                instagram_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = mLocationsModel.getInstaGram_URL();
                        if (!url.startsWith("http://") && !url.startsWith("https://")) {
                            url = "http://" + url;
                        }
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent);
                    }
                });
            }
            //add pinterest url
            if (mLocationsModel.getPinterest_URL().equalsIgnoreCase("")) {
                pinterest_img.setVisibility(View.GONE);
            } else {
                pinterest_img.setVisibility(View.VISIBLE);
                pinterest_img.setTag(mLocationsModel.getPinterest_URL());
                pinterest_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = mLocationsModel.getPinterest_URL();
                        if (!url.startsWith("http://") && !url.startsWith("https://")) {
                            url = "http://" + url;
                        }
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent);
                    }
                });
            }
            //Hide and show the follows us container
            if (mLocationsModel.getFacebook_URL().equalsIgnoreCase("") &&
                    mLocationsModel.getTwitter_URL().equalsIgnoreCase("") &&
                    mLocationsModel.getInstaGram_URL().equalsIgnoreCase("") &&
                    mLocationsModel.getPinterest_URL().equalsIgnoreCase("")) {
                follow_us_container.setVisibility(View.GONE);
                follow_us_container_view.setVisibility(View.GONE);
            }

            if (mLocationsModel.isParent_Location()) {
                collect_point_button.setVisibility(View.GONE);
                explore_button.setVisibility(View.VISIBLE);
            } else {
                collect_point_button.setVisibility(View.VISIBLE);
                explore_button.setVisibility(View.GONE);
            }

            Log.i("Location_Type", mLocationsModel.getLocation_Type() + "");
            if (mLocationsModel.getLocation_Type().equals(1)) {
                reward_description_container_view.setVisibility(View.GONE);
                reward_description.setVisibility(View.GONE);
                redeem_rewards_button.setVisibility(View.GONE);
            } else if (mLocationsModel.getLocation_Type().equals(2)) {
                if (!mLocationsModel.isParent_Location()) {
                    collect_point_button.setVisibility(View.INVISIBLE);
                    collect_point_button.setEnabled(false);
                    explore_button.setVisibility(View.GONE);
                }
                reward_description_container_view.setVisibility(View.VISIBLE);
                reward_description.setVisibility(View.VISIBLE);
                redeem_rewards_button.setVisibility(View.VISIBLE);
                redeem_rewards_button_txt.setText("REDEEM " + mLocationsModel.getReward_Points_Required() + " POINTS");
                reward_description.setText(mLocationsModel.getReward_Description());
            } else if (mLocationsModel.getLocation_Type().equals(3)) {
                reward_description_container_view.setVisibility(View.VISIBLE);
                reward_description.setVisibility(View.VISIBLE);
                redeem_rewards_button.setVisibility(View.VISIBLE);
                redeem_rewards_button_txt.setText("REDEEM " + mLocationsModel.getReward_Points_Required() + " POINTS");
                reward_description.setText(mLocationsModel.getReward_Description());
            } else {
                reward_description_container_view.setVisibility(View.GONE);
                reward_description.setVisibility(View.GONE);
                redeem_rewards_button.setVisibility(View.GONE);
                collect_point_button.setVisibility(View.INVISIBLE);
                collect_point_button.setEnabled(false);
                explore_button.setVisibility(View.GONE);
            }
        }

        locationsArray = mLocationsModel.getMap_GPS().split(",");

        collect_point_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSession.isUserLoggedIn()) {

                    //distance in miles
                    double distance = mCommonMethod.calculateDistance(mLastLocation.getLatitude(), mLastLocation.getLongitude(),
                            Double.parseDouble(locationsArray[0]), Double.parseDouble(locationsArray[1]));

//                    double distance = mCommonMethod.calculateDistance(Double.parseDouble(locationsArray[0]), Double.parseDouble(locationsArray[1]),
//                            Double.parseDouble(locationsArray[0]), Double.parseDouble(locationsArray[1]));
                    //m =mi/0.00062137

                    double metersDistance = distance / 0.00062137;
                    Log.i("metersDistance", "" + metersDistance);
                    Log.i(TAG, mLocationsModel.getPoint_Collection_Radius());
                    double pointCollectionRadius = Double.parseDouble(mLocationsModel.getPoint_Collection_Radius());

                    if (metersDistance <= pointCollectionRadius) {
                        if (mConnectionDetector.isConnectingToInternet()) {
                            JSONObject mJsonObject = new JSONObject();
                            try {
                                mJsonObject.put("key", mCommonMethod.mKey);
                                mJsonObject.put("LocationId", mLocationsModel.getLocation_ID());
                                mJsonObject.put("userId", mSession.getUserId());
                                mJsonObject.put("clientId", mCommonMethod.mClientId);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //Call the Async Task
                            new CollectPointsService().execute(mJsonObject.toString(), "RecordUserLocationAchievement");
                        } else {
                            mCommonMethod.alertDialog(getResources().getString(R.string.no_internet_abt_page_text), null);
                        }
                    } else {
                        mCommonMethod.alertDialog(getResources().getString(R.string.collect_point_alert_msg), null);
                    }
                } else {
                    FragmentManager fm = getFragmentManager();
                    LoginDialogFragment dialogFragment = new LoginDialogFragment();
                    dialogFragment.show(fm, "");
                }
            }
        });

        redeem_rewards_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSession.isUserLoggedIn()) {
                    //distance in miles
                    double distance = mCommonMethod.calculateDistance(mLastLocation.getLatitude(), mLastLocation.getLongitude(),
                            Double.parseDouble(locationsArray[0]), Double.parseDouble(locationsArray[1]));
//                    double distance = mCommonMethod.calculateDistance(Double.parseDouble(locationsArray[0]), Double.parseDouble(locationsArray[1]),
//                            Double.parseDouble(locationsArray[0]), Double.parseDouble(locationsArray[1]));
                    //m =mi/0.00062137
                    double metersDistance = distance / 0.00062137;
                    Log.i("metersDistance", "" + metersDistance);
                    Log.i(TAG, mLocationsModel.getPoint_Collection_Radius());
                    double pointCollectionRadius = Double.parseDouble(mLocationsModel.getPoint_Collection_Radius());

                    if (metersDistance <= pointCollectionRadius) {
                        Intent redeemIntent = new Intent(ExploreDetailsActivity.this, RedeemRewardsActivity.class);
                        redeemIntent.putExtra("Location", mLocationsModel);
                        startActivity(redeemIntent);
                    } else {
                        mCommonMethod.alertDialog(getResources().getString(R.string.redeem_point_alert_msg), null);
                    }
                } else {
                    FragmentManager fm = getFragmentManager();
                    LoginDialogFragment dialogFragment = new LoginDialogFragment();
                    dialogFragment.show(fm, "");
                }
            }
        });
        //explore the child items
        explore_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent exploreMapIntent = new Intent(ExploreDetailsActivity.this, ExploreActivity.class);
                exploreMapIntent.putExtra("IsFromHome", true);
                exploreMapIntent.putExtra("ListChildExplore", true);
                exploreMapIntent.putExtra("ParentLocationId", mLocationsModel.getLocation_ID());
                exploreMapIntent.putExtra("Location", mLocationsModel);
                startActivity(exploreMapIntent);
            }
        });
        //share the item
        share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(ExploreDetailsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ExploreDetailsActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_WRITE_STORAGE);
                } else {
                    requestShare();
                }

            }
        });

    }

    //share information's to other app's
    private void requestShare() {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        String shareContent = mLocationsModel.getLocation_Name() + "\n" + mLocationsModel.getCategoryName() + "\n" + mLocationsModel.getDescription() + "\n"
                + mLocationsModel.getURL();
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
        if (!mLocationsModel.getPhoto().equalsIgnoreCase("")) {
            if (new ImageValidator().validate(mLocationsModel.getPhoto())) {
                if (mCommonMethod.checkDrawableImageAvail(mLocationsModel.getPhoto())) {
                    Uri imageUri = null;
                    try {
                        imageUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(),
                                BitmapFactory.decodeResource(getResources(), mCommonMethod.getDrawableID(mLocationsModel.getPhoto())), null, null));
                        sendIntent.setType("image/*");
                        sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    } catch (NullPointerException e) {
                    }
                } else if (mCommonMethod.checkImageAvail(mLocationsModel.getPhoto(), mLocationsModel.getUpdated(), dateFormat)) {
                    try {
                        File picPath = mCommonMethod.getOutputPath(mLocationsModel.getPhoto(), mLocationsModel.getUpdated(), dateFormat);

                        if (picPath.exists()) {
                            Uri photoUri = Uri.fromFile(picPath);
                            sendIntent.setType("image/*");
                            sendIntent.putExtra(Intent.EXTRA_STREAM, photoUri);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            } else {
                sendIntent.setType("text/plain");
            }
        }
        try {
            startActivity(sendIntent);
        } catch (android.content.ActivityNotFoundException ex) {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //reload my activity with permission granted or use the features what required the permission
                    requestShare();
                } else {
                    Toast.makeText(ExploreDetailsActivity.this,
                            "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    //initialize the variables
    private void initialize() {
        explore_image = (ImageView) findViewById(R.id.explore_image);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        imageWidth = size.x;
        imageHeight = (int) ((imageWidth) / 2.0f);
        explore_image.setLayoutParams(new FrameLayout.LayoutParams(imageWidth, imageHeight));
        no_image_avail = (TextView) findViewById(R.id.no_image_avail);
        explore_prgs = (ProgressBar) findViewById(R.id.explore_prgs);
        titleTxt = (TextView) findViewById(R.id.title);
        category_name_txt = (TextView) findViewById(R.id.category_name_txt);
        address_container = (LinearLayout) findViewById(R.id.address_container);
        address_container_view = (View) findViewById(R.id.address_container_view);
        address_txt = (TextView) findViewById(R.id.address_txt);
        telephone_container = (LinearLayout) findViewById(R.id.telephone_container);
        telephone_container_view = (View) findViewById(R.id.telephone_container_view);
        telephone_txt = (TextView) findViewById(R.id.telephone_txt);
        email_container = (LinearLayout) findViewById(R.id.email_container);
        email_txt = (TextView) findViewById(R.id.email_txt);
        website_container = (LinearLayout) findViewById(R.id.website_container);
        website_txt = (TextView) findViewById(R.id.website_txt);
        explore_description = (TextView) findViewById(R.id.explore_description);
        follow_us_container = (LinearLayout) findViewById(R.id.follow_us_container);
        facebook_img = (ImageView) findViewById(R.id.facebook_img);
        twitter_img = (ImageView) findViewById(R.id.twitter_img);
        instagram_img = (ImageView) findViewById(R.id.instagram_img);
        pinterest_img = (ImageView) findViewById(R.id.pinterest_img);
        reward_description = (TextView) findViewById(R.id.reward_description);
        reward_description_container_view = (View) findViewById(R.id.reward_description_container_view);
        collect_point_button = (RelativeLayout) findViewById(R.id.collect_point_button);
        explore_button = (RelativeLayout) findViewById(R.id.explore_button);
        redeem_rewards_button = (RelativeLayout) findViewById(R.id.redeem_rewards_button);
        email_container_view = (View) findViewById(R.id.email_container_view);
        website_container_view = (View) findViewById(R.id.website_container_view);
        follow_us_container_view = (View) findViewById(R.id.follow_us_container_view);
        share_button = (RelativeLayout) findViewById(R.id.share_button);
        points_layout_child = (LinearLayout) findViewById(R.id.points_layout_child);
        collect_point_txt = (TextView) findViewById(R.id.collect_point_txt);
        reward_points_txt = (TextView) findViewById(R.id.reward_points_txt);
        or_txt = (TextView) findViewById(R.id.or_txt);
        collect_point_button_txt = (TextView) findViewById(R.id.collect_point_button_txt);
        redeem_rewards_button_txt = (TextView) findViewById(R.id.redeem_rewards_button_txt);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
                return;
            }
            String msg = "Location = "
                    + LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lblListHome:
                Intent homeIntent = new Intent(ExploreDetailsActivity.this, HomeScreenActivity.class);
                homeIntent.putExtra("IsFromHome", false);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homeIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListExplore:
                Intent listExploreIntent = new Intent(ExploreDetailsActivity.this, ExploreActivity.class);
                listExploreIntent.putExtra("IsFromHome", false);
                listExploreIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(listExploreIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListMapExplore:
                Intent lblListMapExploreIntent = new Intent(ExploreDetailsActivity.this, ExploreActivity.class);
                lblListMapExploreIntent.putExtra("IsFromHome", false);
                lblListMapExploreIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(lblListMapExploreIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListExploreList:
                Intent lblListExploreListIntent = new Intent(ExploreDetailsActivity.this, ExploreListActivity.class);
                lblListExploreListIntent.putExtra("IsFromHome", false);
                lblListExploreListIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(lblListExploreListIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListRewards:
                Intent lblListRewardsListIntent = new Intent(ExploreDetailsActivity.this, ExploreListActivity.class);
                lblListRewardsListIntent.putExtra("IsFromHome", false);
                lblListRewardsListIntent.putExtra("ListRewards", true);
                lblListRewardsListIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(lblListRewardsListIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListRewardsList:
                Intent listRewardsIntent = new Intent(ExploreDetailsActivity.this, ExploreListActivity.class);
                listRewardsIntent.putExtra("IsFromHome", false);
                listRewardsIntent.putExtra("ListRewards", true);
                listRewardsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(listRewardsIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListMapRewards:
                Intent listMapRewardsIntent = new Intent(ExploreDetailsActivity.this, ExploreActivity.class);
                listMapRewardsIntent.putExtra("IsFromHome", false);
                listMapRewardsIntent.putExtra("ListRewards", true);
                listMapRewardsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(listMapRewardsIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListYourAcc:
                if (mSession.isUserLoggedIn()) {
                    Intent userProfileIntent = new Intent(ExploreDetailsActivity.this, UserProfileActivity.class);
                    userProfileIntent.putExtra("IsFromHome", false);
                    userProfileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(userProfileIntent);
                } else {
                    Intent loginIntent = new Intent(ExploreDetailsActivity.this, LoginActivity.class);
                    loginIntent.putExtra("IsFromHome", false);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(loginIntent);
                }
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListAbout:
                Intent lblListAboutIntent = new Intent(ExploreDetailsActivity.this, AboutAndTipsActivity.class);
                lblListAboutIntent.putExtra("IsFromHome", false);
                lblListAboutIntent.putExtra("IsAbout", true);
                lblListAboutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(lblListAboutIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListTipsSupport:
                Intent lblListTipsSupportIntent = new Intent(ExploreDetailsActivity.this, AboutAndTipsActivity.class);
                lblListTipsSupportIntent.putExtra("IsFromHome", false);
                lblListTipsSupportIntent.putExtra("IsAbout", false);
                lblListTipsSupportIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(lblListTipsSupportIntent);
                finish();
                drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.lblListPoints:
                if (mSession.isUserLoggedIn()) {
                    Intent pointsHistoryIntent = new Intent(ExploreDetailsActivity.this, PointsHistoryActivity.class);
                    pointsHistoryIntent.putExtra("IsFromHome", false);
                    pointsHistoryIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(pointsHistoryIntent);
                    finish();
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    Intent pointsLoginIntent = new Intent(ExploreDetailsActivity.this, LoginActivity.class);
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

    //Parse and get the ChangePassword response
    private void getChangePasswordParse(String result) {
        try {
            JSONObject mJsonObject = new JSONObject(result);
            if (mJsonObject.getBoolean("Success")) {
                mCommonMethod.alertDialog(mJsonObject.getString("Message"), null);
            } else if (!mJsonObject.getBoolean("Success")) {
                mCommonMethod.alertDialog(mJsonObject.getString("Message"), null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Login or Registration Dialog
    public static class LoginDialogFragment extends DialogFragment {
        RelativeLayout Login_button, create_account_button;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Dialog dialog = new Dialog(getActivity(), R.style.MyThemeDialogCustom);
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            return dialog;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.login_dialog_layout, container, false);
            //getDialog().setTitle("Simple Dialog");
            TextView titleTxt = (TextView) rootView.findViewById(R.id.parent_title);
            TextView descriptionTxt = (TextView) rootView.findViewById(R.id.parent_description);
            //descriptionTxt.setMovementMethod(new ScrollingMovementMethod());
            ImageView dismiss = (ImageView) rootView.findViewById(R.id.close_image);
            Login_button = (RelativeLayout) rootView.findViewById(R.id.Login_button);
            create_account_button = (RelativeLayout) rootView.findViewById(R.id.create_account_button);

            Login_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    Intent pointsLoginIntent = new Intent(getActivity(), LoginActivity.class);
                    pointsLoginIntent.putExtra("IsFromHome", false);
                    pointsLoginIntent.putExtra("gotoExplore", true);
                    startActivity(pointsLoginIntent);
                }
            });

            create_account_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    Intent pointsLoginIntent = new Intent(getActivity(), RegistrationActivity.class);
                    pointsLoginIntent.putExtra("IsFromHome", false);
                    pointsLoginIntent.putExtra("gotoExplore", true);
                    startActivity(pointsLoginIntent);
                }
            });


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

    //Update Information Async
    public class CollectPointsService extends AsyncTask<String, String, String> {
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
            //forgot password web service
            result = mWebservice.GetAbtTipInformation(params[0], params[1]);

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Log.i("result", result);
                getChangePasswordParse(result);
            }
            mProgressDialog.dismiss();
        }
    }
}

package com.insider.sanjuanisland.utils;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.insider.sanjuanisland.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 *
 */
public class CommonMethod {
    public static final String mKey = "85web145";
    public static final String mClientId = "1";
    public static final String mTempPassword = "********";
    Context mContext;
    private Session mSession;
    private Double EARTH_RADIUS = 3961.00; // Radius in Kilometers default

    public CommonMethod(Context context) {
        mContext = context;
    }

    public static Double toRadians(Double degree) {
        // Value degree * Pi/180
        Double res = degree * 3.1415926 / 180;
        return res;
    }

    public void alertDialog(String message, final View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                mContext);
        closeKeyPad(view);
        // set title
        alertDialogBuilder.setTitle(mContext.getResources().getString(R.string.app_name));
        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        if (view != null) {
                            view.requestFocus();
                            openKeyPad(view);
                        }
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public Double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        Double Radius = EARTH_RADIUS; //6371.00;
        Double dLat = toRadians(lat2 - lat1);
        Double dLon = toRadians(lon2 - lon1);
        Double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(toRadians(lat1)) * Math.cos(toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        Double c = 2 * Math.asin(Math.sqrt(a));
        return Radius * c;
    }

    //Opening device Keypad
    public void openKeyPad(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    //Close device Keypad
    public void closeKeyPad(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    view.getWindowToken(), 0);
        }

    }

    //Used to check the Email Address
    public boolean checkEmail(String email) {
        // Matches the pattern with the given string
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    //check the file directory
    public File getOutputPath(String imageUrl, String updatedDateTime, String dateFormat) {
        File mediaFile = null;
        String fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1, imageUrl.length());
        String fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
        try {
            File fileDir = null;
            StorageHelper storageHelper = new StorageHelper();
            if (storageHelper.isExternalStorageReadableAndWritable()) {
                fileDir = new File(Environment.getExternalStorageDirectory() + "/" + mContext.getResources().getString(R.string.image_folder_name));
            } else {
                fileDir = new File(mContext.getFilesDir() + "/" + mContext.getResources().getString(R.string.image_folder_name));
            }
            if (!fileDir.exists()) {
                fileDir.mkdir();
            }
            Date date = new Date();
            String convertDate = "";
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
            SimpleDateFormat convertformat = new SimpleDateFormat("ddMMyyyy_HHmmss");
            try {
                date = format.parse(updatedDateTime);
                convertDate = convertformat.format(date);
                System.out.println("convertDate-------->" + convertDate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            mediaFile = new File(fileDir.getPath() + "/" + fileNameWithoutExtension + convertDate + ".png");
            mediaFile.createNewFile();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mediaFile;
    }

    //check the file directory
    public void saveImage(File picPath, Bitmap bitmap) {
        try {
            FileOutputStream fo = new FileOutputStream(picPath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fo);
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //To Check Image is available in Local Storage
    public boolean checkImageAvail(String imageUrl, String updatedDateTime, String dateFormat) {
        File mediaFile = null;
        String fileName = "", fileNameWithoutExtension = "";
        try {
            fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1, imageUrl.length());
            fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
        } catch (IndexOutOfBoundsException i) {
            i.printStackTrace();
        }
        try {
            File fileDir = new File(Environment.getExternalStorageDirectory() + "/" + mContext.getResources().getString(R.string.image_folder_name) + "/");
            if (!fileDir.exists()) {
                return false;
            }
            Date date = new Date();
            String convertDate = "";
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
            SimpleDateFormat convertformat = new SimpleDateFormat("ddMMyyyy_HHmmss");
            try {
                date = format.parse(updatedDateTime);
                convertDate = convertformat.format(date);
                System.out.println("convertDate-------->" + convertDate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            mediaFile = new File(fileDir.getPath() + "/" + fileNameWithoutExtension + convertDate + ".png");
            if (!mediaFile.exists()) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    //To check whether the image is available in Drawable or not
    public boolean checkDrawableImageAvail(String imageUrl) {
        String fileName = "", fileNameWithoutExtension = "";
        try {
            fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1, imageUrl.length());
            fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
            if (Character.isDigit(fileNameWithoutExtension.charAt(0))) {
                fileNameWithoutExtension = "san_" + fileNameWithoutExtension;
            }
        } catch (IndexOutOfBoundsException i) {
            i.printStackTrace();
        }
        fileNameWithoutExtension = fileNameWithoutExtension.toLowerCase();
        fileNameWithoutExtension = fileNameWithoutExtension.replace(" ", "_");
        fileNameWithoutExtension = fileNameWithoutExtension.replaceAll("[^A-Za-z0-9 ]", "_");
        Log.i("fileName", fileNameWithoutExtension);
        int id = mContext.getResources().getIdentifier(fileNameWithoutExtension, "drawable", mContext.getPackageName());
        if (id == -1 || id == 0) {
            return false;
        }
        Log.i("id", id + "");
        return true;
    }

    //check the file directory
    public int getDrawableID(String imageUrl) {
        File mediaFile = null;
        String fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1, imageUrl.length());
        String fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
        if (Character.isDigit(fileNameWithoutExtension.charAt(0))) {
            fileNameWithoutExtension = "san_" + fileNameWithoutExtension;
        }
        fileNameWithoutExtension = fileNameWithoutExtension.toLowerCase();
        fileNameWithoutExtension = fileNameWithoutExtension.replace(" ", "_");
        fileNameWithoutExtension = fileNameWithoutExtension.replaceAll("[^A-Za-z0-9 ]", "_");
        Log.i("fileName", fileNameWithoutExtension);
        int id = mContext.getResources().getIdentifier(fileNameWithoutExtension, "drawable", mContext.getPackageName());
        return id;
    }

    //Resizing the images
    public Bitmap decodeFile(File f) {
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale++;
            }

            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }


    //Set Image to image view
    public void setImageToView(final String imagePath, final String updatedDate, final ImageView imageView, final String dateformat) {
        RequestManager
                .getInstance(mContext)
                .doRequest()
                .getImageLoader()
                .get(imagePath, new ImageLoader.ImageListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {

                    }

                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                        if (response.getBitmap() != null) {

                            try {
                                File picPath = getOutputPath(imagePath, updatedDate, dateformat);
                                Log.i("Insert capture path-->", "" + picPath);
                                saveImage(picPath, response.getBitmap());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (imageView != null) {
                                imageView.setImageBitmap(response.getBitmap());
                            }
                        }
                    }
                });
    }


}

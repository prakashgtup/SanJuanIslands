package com.insider.sanjuanisland.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Webservice class which contains all web service methods.
 */
public class Webservice {

    Context mContext;
    String mPrimaryLocationServiceUrl = "http://admin.468insider.com/svc/SandCastleLocationService.svc/";
    String mPrimaryUserServiceUrl = "http://admin.468insider.com/svc/SandCastleUserService.svc/";

    public Webservice(Context context) {
        mContext = context;
    }

    public String registerAppUse(String json) {
        String result = null;
        try {
            StringBuilder response = new StringBuilder();
            HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(
                    mPrimaryLocationServiceUrl + "RegisterAppUse/").openConnection();
            localHttpURLConnection.setRequestMethod("POST");
            localHttpURLConnection.setRequestProperty("Content-type",
                    "application/json");
            localHttpURLConnection.setRequestProperty("Accept",
                    "*");
            localHttpURLConnection.setDoInput(true);
            localHttpURLConnection.setDoOutput(true);
            localHttpURLConnection.setUseCaches(false);
            localHttpURLConnection.connect();
            DataOutputStream localOutputStreamWriter;

            byte[] postData = json.getBytes("UTF-8");

            Log.i("request", json);
            localOutputStreamWriter = new DataOutputStream(
                    localHttpURLConnection.getOutputStream());
            localOutputStreamWriter.writeBytes(json);
            //localOutputStreamWriter.write(postData);
            localOutputStreamWriter.flush();
            localOutputStreamWriter.close();
            System.out.println("code...for" + " "
                    + localHttpURLConnection.getResponseCode());
            BufferedReader localBufferedReader = new BufferedReader(
                    new InputStreamReader(localHttpURLConnection.getInputStream()));
            String data = "";
            while ((data = localBufferedReader.readLine()) != null) {
                response.append(data);
            }
            Log.i("response", response.toString());
            result = response.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = "Error";
        }
        return result;
    }

    public String GetClientDetails(String json) {
        String result = null;
        try {
            StringBuilder response = new StringBuilder();
            HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(
                    mPrimaryLocationServiceUrl + "GetClientDetails/").openConnection();
            localHttpURLConnection.setRequestMethod("POST");
            localHttpURLConnection.setRequestProperty("Content-type",
                    "application/json");
            localHttpURLConnection.setRequestProperty("Accept",
                    "*");
            localHttpURLConnection.setDoInput(true);
            localHttpURLConnection.setDoOutput(true);
            localHttpURLConnection.setUseCaches(false);
            localHttpURLConnection.connect();
            DataOutputStream localOutputStreamWriter;

            byte[] postData = json.getBytes("UTF-8");

            Log.i("request", json);
            localOutputStreamWriter = new DataOutputStream(
                    localHttpURLConnection.getOutputStream());
            localOutputStreamWriter.writeBytes(json);
            //localOutputStreamWriter.write(postData);
            localOutputStreamWriter.flush();
            localOutputStreamWriter.close();
            System.out.println("code...for" + " "
                    + localHttpURLConnection.getResponseCode());
            BufferedReader localBufferedReader = new BufferedReader(
                    new InputStreamReader(localHttpURLConnection.getInputStream()));
            String data = "";
            while ((data = localBufferedReader.readLine()) != null) {
                response.append(data);
            }
            Log.i("response", response.toString());
            result = response.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = "Error";
        }
        return result;
    }

    public String GetAllLocations(String json) {
        String result = null;
        try {
            StringBuilder response = new StringBuilder();
            HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(
                    mPrimaryLocationServiceUrl + "GetAllLocations/").openConnection();
            localHttpURLConnection.setRequestMethod("POST");
            localHttpURLConnection.setRequestProperty("Content-type",
                    "application/json");
            localHttpURLConnection.setRequestProperty("Accept",
                    "*");
            localHttpURLConnection.setDoInput(true);
            localHttpURLConnection.setDoOutput(true);
            localHttpURLConnection.setUseCaches(false);
            localHttpURLConnection.connect();
            DataOutputStream localOutputStreamWriter;

            byte[] postData = json.getBytes("UTF-8");

            Log.i("request", json);
            localOutputStreamWriter = new DataOutputStream(
                    localHttpURLConnection.getOutputStream());
            localOutputStreamWriter.writeBytes(json);
            //localOutputStreamWriter.write(postData);
            localOutputStreamWriter.flush();
            localOutputStreamWriter.close();
            System.out.println("code...for" + " "
                    + localHttpURLConnection.getResponseCode());
            BufferedReader localBufferedReader = new BufferedReader(
                    new InputStreamReader(localHttpURLConnection.getInputStream()));
            String data = "";
            while ((data = localBufferedReader.readLine()) != null) {
                response.append(data);
            }
            Log.i("response", response.toString());
            result = response.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = "Error";
        }
        return result;
    }

    public String GetAllCategories(String json) {
        String result = null;
        try {
            StringBuilder response = new StringBuilder();
            HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(
                    mPrimaryLocationServiceUrl + "GetAllCategories/").openConnection();
            localHttpURLConnection.setRequestMethod("POST");
            localHttpURLConnection.setRequestProperty("Content-type",
                    "application/json");
            localHttpURLConnection.setRequestProperty("Accept",
                    "*");
            localHttpURLConnection.setDoInput(true);
            localHttpURLConnection.setDoOutput(true);
            localHttpURLConnection.setUseCaches(false);
            localHttpURLConnection.connect();
            DataOutputStream localOutputStreamWriter;

            byte[] postData = json.getBytes("UTF-8");

            Log.i("request", json);
            localOutputStreamWriter = new DataOutputStream(
                    localHttpURLConnection.getOutputStream());
            localOutputStreamWriter.writeBytes(json);
            //localOutputStreamWriter.write(postData);
            localOutputStreamWriter.flush();
            localOutputStreamWriter.close();
            System.out.println("code...for" + " "
                    + localHttpURLConnection.getResponseCode());
            BufferedReader localBufferedReader = new BufferedReader(
                    new InputStreamReader(localHttpURLConnection.getInputStream()));
            String data = "";
            while ((data = localBufferedReader.readLine()) != null) {
                response.append(data);
            }
            Log.i("response", response.toString());
            result = response.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = "Error";
        }
        return result;
    }

    public String GetAllLocationRelation(String json) {
        String result = null;
        try {
            StringBuilder response = new StringBuilder();
            HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(
                    mPrimaryLocationServiceUrl + "GetAllLocationRelation/").openConnection();
            localHttpURLConnection.setRequestMethod("POST");
            localHttpURLConnection.setRequestProperty("Content-type",
                    "application/json");
            localHttpURLConnection.setRequestProperty("Accept",
                    "*");
            localHttpURLConnection.setDoInput(true);
            localHttpURLConnection.setDoOutput(true);
            localHttpURLConnection.setUseCaches(false);
            localHttpURLConnection.connect();
            DataOutputStream localOutputStreamWriter;

            byte[] postData = json.getBytes("UTF-8");

            Log.i("request", json);
            localOutputStreamWriter = new DataOutputStream(
                    localHttpURLConnection.getOutputStream());
            localOutputStreamWriter.writeBytes(json);
            //localOutputStreamWriter.write(postData);
            localOutputStreamWriter.flush();
            localOutputStreamWriter.close();
            System.out.println("code...for" + " "
                    + localHttpURLConnection.getResponseCode());
            BufferedReader localBufferedReader = new BufferedReader(
                    new InputStreamReader(localHttpURLConnection.getInputStream()));
            String data = "";
            while ((data = localBufferedReader.readLine()) != null) {
                response.append(data);
            }
            Log.i("response", response.toString());
            result = response.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = "Error";
        }
        return result;
    }

    public String GetAbtTipInformation(String json,String methodName) {
        String result = null;
        try {
            StringBuilder response = new StringBuilder();
            HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(
                    mPrimaryLocationServiceUrl + methodName+"/").openConnection();
            localHttpURLConnection.setRequestMethod("POST");
            localHttpURLConnection.setRequestProperty("Content-type",
                    "application/json");
            localHttpURLConnection.setRequestProperty("Accept",
                    "*");
            localHttpURLConnection.setDoInput(true);
            localHttpURLConnection.setDoOutput(true);
            localHttpURLConnection.setUseCaches(false);
            localHttpURLConnection.connect();
            DataOutputStream localOutputStreamWriter;

            byte[] postData = json.getBytes("UTF-8");

            Log.i("request", json);
            localOutputStreamWriter = new DataOutputStream(
                    localHttpURLConnection.getOutputStream());
            localOutputStreamWriter.writeBytes(json);
            //localOutputStreamWriter.write(postData);
            localOutputStreamWriter.flush();
            localOutputStreamWriter.close();
            System.out.println("code...for" + " "
                    + localHttpURLConnection.getResponseCode());
            BufferedReader localBufferedReader = new BufferedReader(
                    new InputStreamReader(localHttpURLConnection.getInputStream()));
            String data = "";
            while ((data = localBufferedReader.readLine()) != null) {
                response.append(data);
            }
            Log.i("response", response.toString());
            result = response.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = "Error";
        }
        return result;
    }

    public String UserLogin(String json,String methodName) {
        String result = null;
        try {
            StringBuilder response = new StringBuilder();
            HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(
                    mPrimaryUserServiceUrl + methodName+"/").openConnection();
            localHttpURLConnection.setRequestMethod("POST");
            localHttpURLConnection.setRequestProperty("Content-type",
                    "application/json");
            localHttpURLConnection.setRequestProperty("Accept",
                    "*");
            localHttpURLConnection.setDoInput(true);
            localHttpURLConnection.setDoOutput(true);
            localHttpURLConnection.setUseCaches(false);
            localHttpURLConnection.connect();
            DataOutputStream localOutputStreamWriter;

            byte[] postData = json.getBytes("UTF-8");

            Log.i("request", json);
            localOutputStreamWriter = new DataOutputStream(
                    localHttpURLConnection.getOutputStream());
            localOutputStreamWriter.writeBytes(json);
            //localOutputStreamWriter.write(postData);
            localOutputStreamWriter.flush();
            localOutputStreamWriter.close();
            System.out.println("code...for" + " "
                    + localHttpURLConnection.getResponseCode());
            BufferedReader localBufferedReader = new BufferedReader(
                    new InputStreamReader(localHttpURLConnection.getInputStream()));
            String data = "";
            while ((data = localBufferedReader.readLine()) != null) {
                response.append(data);
            }
            Log.i("response", response.toString());
            result = response.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = "Error";
        }
        return result;
    }

    public String GetCountriesStates(String json,String methodName) {
        String result = null;
        try {
            StringBuilder response = new StringBuilder();
            HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(
                    mPrimaryLocationServiceUrl + methodName+"/").openConnection();
            localHttpURLConnection.setRequestMethod("POST");
            localHttpURLConnection.setRequestProperty("Content-type",
                    "application/json");
            localHttpURLConnection.setRequestProperty("Accept",
                    "*");
            localHttpURLConnection.setDoInput(true);
            localHttpURLConnection.setDoOutput(true);
            localHttpURLConnection.setUseCaches(false);
            localHttpURLConnection.connect();
            DataOutputStream localOutputStreamWriter;

            byte[] postData = json.getBytes("UTF-8");

            Log.i("request", json);
            localOutputStreamWriter = new DataOutputStream(
                    localHttpURLConnection.getOutputStream());
            localOutputStreamWriter.writeBytes(json);
            //localOutputStreamWriter.write(postData);
            localOutputStreamWriter.flush();
            localOutputStreamWriter.close();
            System.out.println("code...for" + " "
                    + localHttpURLConnection.getResponseCode());
            BufferedReader localBufferedReader = new BufferedReader(
                    new InputStreamReader(localHttpURLConnection.getInputStream()));
            String data = "";
            while ((data = localBufferedReader.readLine()) != null) {
                response.append(data);
            }
            Log.i("response", response.toString());
            result = response.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = "Error";
        }
        return result;
    }

    public String syncCreditPoints(String json) {
        String result = null;
        try {
            StringBuilder response = new StringBuilder();
            HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(
                    mPrimaryLocationServiceUrl + "credit_points.php").openConnection();
            localHttpURLConnection.setRequestMethod("POST");
            localHttpURLConnection.setRequestProperty("Content-type",
                    "application/json");
            localHttpURLConnection.setRequestProperty("Accept",
                    "*");
            localHttpURLConnection.setDoInput(true);
            localHttpURLConnection.setDoOutput(true);
            localHttpURLConnection.setUseCaches(false);
            localHttpURLConnection.connect();
            DataOutputStream localOutputStreamWriter;

            byte[] postData = json.getBytes("UTF-8");

            Log.i("request", json);
            localOutputStreamWriter = new DataOutputStream(
                    localHttpURLConnection.getOutputStream());
            localOutputStreamWriter.writeBytes(json);
            //localOutputStreamWriter.write(postData);
            localOutputStreamWriter.flush();
            localOutputStreamWriter.close();
            System.out.println("code...for" + " "
                    + localHttpURLConnection.getResponseCode());
            BufferedReader localBufferedReader = new BufferedReader(
                    new InputStreamReader(localHttpURLConnection.getInputStream()));
            String data = "";
            while ((data = localBufferedReader.readLine()) != null) {
                response.append(data);
            }
            Log.i("response", response.toString());
            result = response.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = "Error";
        }
        return result;
    }


}

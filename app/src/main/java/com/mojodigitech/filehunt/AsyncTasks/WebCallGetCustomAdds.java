package com.mojodigitech.filehunt.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.mojodigitech.filehunt.AddsUtility.AddConstants;
import com.mojodigitech.filehunt.AddsUtility.JsonParser;
import com.mojodigitech.filehunt.AddsUtility.OkhttpMethods;
import com.mojodigitech.filehunt.AddsUtility.SharedPreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class WebCallGetCustomAdds extends AsyncTask<String,String,String>{

    private Context mContext;

    SharedPreferenceUtil addprefs ;
    cumstomAddListener listener;

    public WebCallGetCustomAdds(Context mContext, cumstomAddListener listener) {
        this.mContext = mContext;
        this.listener=listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        addprefs = new SharedPreferenceUtil(mContext);
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            JSONObject requestObj= AddConstants.prepareCustomAddJsonRequest(mContext, AddConstants.VENDOR_ID);
            //Log.e( "CUSTOM_ADD", requestObj.toString());
            return OkhttpMethods.CallApi(mContext,AddConstants.CUSTOM_ADD_API_URL,requestObj.toString());

        } catch (IOException e) {
            e.printStackTrace();
            return ""+e.getMessage();
        }
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e("JsonResponse", ""+s);

        // prepare dummy response for time being
        //s="{\"status\":true,\"isCampaignRunning\":\"1\",\"message\":\"Success\",\"data\":{\"appName\":\"Lock Screen\",\"landingUrl\":\"https://khulasa-news.com/shekhar-kapoor-on-pm-modis-tweetdev-anand-opposed-emergency/\",\"banner\":\"http://development.bdigimedia.com/riccha_dev/App-Ad-Mgmt/app_campaigns/Lock Screen/IMG-20190112-WA0022.jpg\",\"country_code\":\"IN\",\"facebook\":\"0\"}}";
        // prepare dummy response for time being

        try {
            int responseCode = addprefs.getIntValue(AddConstants.API_RESPONSE_CODE, 0);
            if (s != null && responseCode == 200) {
                //
                JSONObject mainJson = new JSONObject(s);
                if (mainJson.has("status")) {
                    String status = JsonParser.getkeyValue_Str(mainJson, "status");
                    if(status.equalsIgnoreCase("true"))
                    {
                        String isCampaignRunning = JsonParser.getkeyValue_Str(mainJson, "isCampaignRunning");
                        if(isCampaignRunning.equalsIgnoreCase("1"))
                        {
                            JSONObject dataObject=mainJson.getJSONObject("data");
                            String landingUrl=dataObject.getString("landingUrl");
                            String banner=dataObject.getString("banner");
                            String country_code=dataObject.getString("country_code");
                            //String facebookAddStatus=dataObject.getString("facebook");
                            if(addprefs!=null) {
                                addprefs.setValue(AddConstants.CUSTOM_ADD_RUNNING, isCampaignRunning);
                                addprefs.setValue(AddConstants.LANDING_URL, landingUrl);
                                addprefs.setValue(AddConstants.BANNER_PATH, banner);
                                addprefs.setValue(AddConstants.COUNTRY_CODE, country_code);
                                //addprefs.setValue(AddConstants.FACEBOOKADDSTATUS, facebookAddStatus);
                                listener.onRefreshAdd();
                            }

                        }
                        else
                            {

                                if(addprefs !=null) {
                                    addprefs.setValue(AddConstants.CUSTOM_ADD_RUNNING, isCampaignRunning);
                                }

                            }
                    }

                }
            }
            else {

                //  in case  some other error  occurs  on server then set campaing to  false here  set  varibale

                addprefs.setValue(AddConstants.CUSTOM_ADD_RUNNING, "0");


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public  interface cumstomAddListener
    {
        public void  onRefreshAdd();
    }
}
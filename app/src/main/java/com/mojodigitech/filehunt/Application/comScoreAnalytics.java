package com.mojodigitech.filehunt.Application;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.webkit.WebView;

import androidx.multidex.MultiDexApplication;

import com.comscore.Analytics;
import com.comscore.PublisherConfiguration;

/* comScore library used for  App Analytics report  at the place of MyApplication
as per the confirmation of Riccha ma'am and Karan Sir
* changes done by rachna on 3-02-23
library added in libs folder and then imported in to the build.gradle.

*/

public class comScoreAnalytics extends MultiDexApplication {
    private static comScoreAnalytics mInstance;

    /*@Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }*/
    public void onCreate() {
        super.onCreate();
        //Realm.init(this);
        mInstance = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String processName = getProcessName(this);
            //Log.d("ProcessName", ""+processName);
            if (!"com.mojodigitech.filehunt".equals(processName)) {
                    WebView.setDataDirectorySuffix(processName);

            }
        }

        PublisherConfiguration publisher = new PublisherConfiguration.Builder()
                .publisherId("37812790")
                .build();

        Analytics.getConfiguration().addClient(publisher);
        Analytics.getConfiguration().enableImplementationValidationMode();
        Analytics.start(getApplicationContext());


       /* SharedPreferenceUtil addPref=new SharedPreferenceUtil(getApplicationContext());
        String appId=addPref.getStringValue(AddConstants.APP_ID, AddConstants.NOT_FOUND);
        if(appId !=null && !appId.equalsIgnoreCase(AddConstants.NOT_FOUND) )
            MobileAds.initialize(this, appId); //actual app id

        //for fb adds
        //https://developers.facebook.com/docs/audience-network/reference/android/com/facebook/ads/audiencenetworkads.html/
        AudienceNetworkAds.initialize(getApplicationContext());
        AudienceNetworkAds.isInAdsProcess(getApplicationContext());*/
    }

    public String getProcessName(Context context) {
        if (context == null) return null;
       /* if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
            ((ActivityManager)getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData(); // note: it has a return value!
        } else {
            String packageName = getApplicationContext().getPackageName();
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("pm clear "+packageName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

         ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //((ActivityManager)getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData(); // note: it has a return value!

        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == android.os.Process.myPid()) {
                return processInfo.processName;
            }
        }
        return null;
    }

    public static synchronized comScoreAnalytics getInstance() {
        return mInstance;
    }
}

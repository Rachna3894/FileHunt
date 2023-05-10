package com.mojodigitech.filehunt;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static com.mojodigitech.filehunt.Class.Constants.POSITION;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.mojodigitech.filehunt.Adapter.DrawerNavListAdapter;
import com.mojodigitech.filehunt.AddsUtility.AddConstants;
import com.mojodigitech.filehunt.AddsUtility.JsonParser;
import com.mojodigitech.filehunt.AddsUtility.OkhttpMethods;
import com.mojodigitech.filehunt.AddsUtility.SharedPreferenceUtil;
import com.mojodigitech.filehunt.AsyncTasks.WebCallGetCustomAdds;
import com.mojodigitech.filehunt.Class.Constants;
import com.mojodigitech.filehunt.Model.DrawerObjectItemList;
import com.mojodigitech.filehunt.Model.category_Model;
import com.mojodigitech.filehunt.Utils.EncryptDialogUtility;
import com.mojodigitech.filehunt.Utils.Utility;
import com.mojodigitech.filehunt.Utils.UtilityStorage;
import com.mojodigitech.filehunt.junkCleanModule.BoostFragment;
import com.mojodigitech.filehunt.junkCleanModule.JunkCleanActivity;
import com.mojodigitech.filehunt.junkCleanModule.ViewDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
public class MainActivity extends AppCompatActivity implements  EncryptDialogUtility.EncryptDialogListener, WebCallGetCustomAdds.cumstomAddListener/*, PurchasesUpdatedListener*/ {


    Toolbar toolbar;
    RecyclerView recyclerView;
    List<category_Model> catList = new ArrayList<category_Model>();
    Context mContext;
    private static final int PERMISSION_REQUEST_CODE = 123;
    ProgressBar progressBar, progressBar_Ext;
    TextView avlbMemory, totalMemmory, internalTxt, avlbTxt,appNameTxt;
    TextView avlbMemory_Ext, totalMemmory_Ext, internalTxt_Ext, avlbTxt_Ext , navAppVersion_Txt,strgTxt,toolbar_title;
    TextView cpu_txt,boo_txt;
    RelativeLayout storage_section;
    LinearLayout ext_layout,internalLLayout;

    private Menu context_menu;

    String[] permissionsRequired = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;


    //add vars
    BroadcastReceiver internetChangerReceiver;
    SharedPreferenceUtil mPrefs;
    //private AdView mAdView;


    View adContainer;
    RelativeLayout addhoster;

    //add push notification
    private static String fcm_Token ="" ;
    public   String deviceID ="";
    public   String nameOfDevice ="";
    public   String appVersionName ="";

    String clickPushNotification ="";
    boolean isNetworkAvailable  ;
    int max_execute ;
    boolean hideExtPrefValue;

    categoryAdapter cat_adapter;
      //  SkuDetails skuDetails;
   // private BillingClient mBillingClient;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private DrawerNavListAdapter drawerNavListAdapter;
    private ListView mDrawerList;


    LinearLayout boost_phone_layout,clearJunkBtn,freeRamBtn,duplicateImgBtn;

    ImageView customAddImage;
    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();

    AppUpdateManager appUpdateManager;
    int MY_REQUEST_CODE=9999;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(mContext==null) {
            mContext = MainActivity.this;
        }

        Log.d("EncryptedFiles", "==>> "+ Utility.getHiddenFileLimit());


        if(mPrefs ==null) {
            mPrefs = new SharedPreferenceUtil(mContext);
        }

        if(mPrefs !=null){
            clickPushNotification = mPrefs.getStringValue(AddConstants.CLICK_PUSH_NOTIFICATION, "");
        }

        isNetworkAvailable = AddConstants.checkIsOnline(mContext);


        permissionStatus = mContext.getSharedPreferences("permissionStatus", MODE_PRIVATE);

        if (!checkPermission()){
            requestPermission();
        }

        googleInAppUpadte();
        //You already have the permission, just go ahead.
        initActivityComponents();
        iniVars();
        getPushToken();

        //askForPermission();

        if(mContext!=null)
        {
            mPrefs = new SharedPreferenceUtil(mContext);
            addhoster=findViewById(R.id.addhoster);
            //mAdView = (AdView) findViewById(R.id.adView);
            adContainer = findViewById(R.id.adMobView);


        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Utility.setstatusBarColorBelowM(MainActivity.this);
        }

        //dispbannerAdd();

        // this broadcast  will  listen the  internet state change for sendig request  when internet becomes available
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        internetChangerReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                boolean isNetworkAvailable = AddConstants.checkIsOnline(mContext);

                //  Toast.makeText(context, "isNetworkAvailable-->" + isNetworkAvailable, Toast.LENGTH_SHORT).show();

                //Log.d("isNetworkAvailable", "" + isNetworkAvailable);
                if (isNetworkAvailable) {
                    //long ms1=System.currentTimeMillis();
                    // System.out.print("Milliseconds-->>"+ms1);
                    //Log.e("Milliseconds before-->>", ""+ms1);
                    new WebCall().execute();

                } else {
                    /*if (mAdView != null && mPrefs != null) {
                        AddMobUtils util = new AddMobUtils();
                        util.displayLocalBannerAdd(mAdView);
                        //util.showInterstitial(addprefs,HomeActivity.this, null);
                        //util.displayRewaredVideoAdd(addprefs, mContext, null);
                    }*/
                }
            }

        };
        registerReceiver(internetChangerReceiver, intentFilter);
        // this broadcast  will  listen the  internet state change for sendig request  when internet becomes available


        navAppVersion_Txt = (TextView) findViewById(R.id.navAppVersion_Txt);



/***********************Start**********************************************/



        deviceID = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        //Log.e("Android ID : ",""+deviceID);
        nameOfDevice = Build.MANUFACTURER+" "+Build.MODEL+" "+Build.VERSION.RELEASE;
        //Log.e("Device Name : ",""+nameOfDevice);
        PackageInfo pinfo = null;
        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            appVersionName = pinfo.versionName;
            ///Log.e("App Version Name : ",""+appVersionName);

            if(appVersionName!=null) {
                //String appVersion = "App Version : " + appVersionName;
                String appVersion = getResources().getString(R.string.app_version)+" "+ appVersionName;
                setNavAppVersion(appVersion);
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }catch (Exception ex){ ex.printStackTrace();}






        //this part is now inside getPushtoken () function

           /*

        if(addprefs!=null) {
            boolean st=addprefs.getBoolanValue(Constants.isFcmRegistered, false);
            System.out.print(""+st);
            if(!addprefs.getBoolanValue(Constants.isFcmRegistered, false)) {

                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( MainActivity.this,
                new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        fcm_Token = instanceIdResult.getToken();
                        Log.e("New Token : ", fcm_Token);

                        if (isNetworkAvailable) {
                            Log.e("Network is available ", "PushNotification Called");
                            new PushNotificationCall().execute();
                        } else {
                            Log.e("No Network", "PushNotification Call failed");
                        }
                    }
                });

            }
        }



        if(addprefs!=null) {

            boolean status=addprefs.getBoolanValue(AddConstants.AutoStartKey, false);
            if(!status) {
                Intent intent = new Intent();
                String manufacturer = android.os.Build.MANUFACTURER;
                showAutoStartPermDialog(manufacturer, intent);
                switch (manufacturer) {

                    case "xiaomi":
                        intent.setComponent(new ComponentName("com.miui.securitycenter",
                                "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                        break;
                    case "oppo":
                        intent.setComponent(new ComponentName("com.coloros.safecenter",
                                "com.coloros.safecenter.permission.startup.StartupAppListActivity"));

                        break;
                    case "vivo":
                        intent.setComponent(new ComponentName("com.vivo.permissionmanager",
                                "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
                        break;
                }

                List<ResolveInfo> arrayListInfo = getPackageManager().queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);

                if (arrayListInfo.size() > 0) {
                    // startActivity(intent);
                    showAutoStartPermDialog(manufacturer, intent);


                }
            }
        }*/



        //this part is now inside getPushtoken () function



        if(AddConstants.NEWSURL!=null && !AddConstants.NEWSURL.trim().isEmpty() && !AddConstants.NEWSURL.equalsIgnoreCase("") && clickPushNotification.equalsIgnoreCase("true") && !clickPushNotification.trim().isEmpty()) {
            //Log.e("Helper.NEWSURL ", ""+AddConstants.NEWSURL);
            mPrefs.setValue(AddConstants.CLICK_PUSH_NOTIFICATION, "false");

            //  Intent webIntent = new Intent(MainActivity.this, WebActivity.class);
            //startActivity(webIntent);

            openUrlInChromeOrdefaultBrowser(AddConstants.NEWSURL);

        }



    }


    private boolean checkPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int result = ContextCompat.checkSelfPermission(MainActivity.this, READ_EXTERNAL_STORAGE);
            int result1 = ContextCompat.checkSelfPermission(MainActivity.this, WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
        }
    }


    private void requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
                startActivityForResult(intent, 2296);

            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 2296);
            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2296) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {

                    // perform action when allow permission success
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


   @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean READ_EXTERNAL_STORAGE = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean WRITE_EXTERNAL_STORAGE = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (READ_EXTERNAL_STORAGE && WRITE_EXTERNAL_STORAGE) {
                        // perform action when allow permission success

                    } else {
                        Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
    public static String getToken(String token){
        fcm_Token= token;
        Log.d("token1234",fcm_Token);
        return token;
    }

    private void openUrlInChromeOrdefaultBrowser(String  url)
    {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setPackage("com.android.chrome");
        try {
            startActivity(i);
        } catch (ActivityNotFoundException e) {
            // Chrome is probably not installed
            // Try with the default browser
            i.setPackage(null);
            startActivity(i);
        }
    }



    /* google in app update*/

    private void googleInAppUpadte()
    {
        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(mContext);

// Returns an intent object that you use to check for an update.
        com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(new com.google.android.play.core.tasks.OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        // For a flexible update, use AppUpdateType.FLEXIBLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    // Request the update.

                    requestUpadte(appUpdateInfo);

                }


            }
        });


    }

    private void requestUpadte(AppUpdateInfo appUpdateInfo)
    {

        try {
            appUpdateManager.startUpdateFlowForResult(
                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,
                    // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                    AppUpdateType.IMMEDIATE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    MY_REQUEST_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    /* google in app  update */



    private  void getPushToken()
    {

        try {

            //here  on

            if (mPrefs != null) {
                boolean st = mPrefs.getBoolanValue(Constants.isFcmRegistered, false);
                System.out.print("" + st);
                if (!mPrefs.getBoolanValue(Constants.isFcmRegistered, false)) {

                        if (isNetworkAvailable) {
                            // Log.e("Network is available ", "PushNotification Called");
                            new PushNotificationCall().execute();
                        } else {
                            Log.e("No Network", "PushNotification Call failed");
                        }


                   /* FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MainActivity.this,
                            new OnSuccessListener<InstanceIdResult>() {
                                @Override
                                public void onSuccess(InstanceIdResult instanceIdResult) {
                                    fcm_Token = instanceIdResult.getToken();
                                    //Log.e("New Token : ", ""+fcm_Token);

                                    if (isNetworkAvailable) {
                                        // Log.e("Network is available ", "PushNotification Called");
                                        new PushNotificationCall().execute();
                                    } else {
                                        Log.e("No Network", "PushNotification Call failed");
                                    }
                                }
                            });*/

                }
            }


            if (mPrefs != null) {

                boolean isAutoStartPermGranted = mPrefs.getBoolanValue(AddConstants.AutoStartKey, false);
                if (!isAutoStartPermGranted) {
                    Intent intent = new Intent();
                    String manufacturer = android.os.Build.MANUFACTURER;
                    //showAutoStartPermDialog(manufacturer, intent);
                    switch (manufacturer) {

                        case "xiaomi":
                            intent.setComponent(new ComponentName("com.miui.securitycenter",
                                    "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                            break;
                        case "oppo":
                            intent.setComponent(new ComponentName("com.coloros.safecenter",
                                    "com.coloros.safecenter.permission.startup.StartupAppListActivity"));

                            break;
                        case "vivo":
                            intent.setComponent(new ComponentName("com.vivo.permissionmanager",
                                    "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
                            break;
                    }

                    List<ResolveInfo> arrayListInfo = getPackageManager().queryIntentActivities(intent,
                            PackageManager.MATCH_DEFAULT_ONLY);

                    if (arrayListInfo.size() > 0) {
                        // startActivity(intent);
                        showAutoStartPermDialog(manufacturer, intent);


                    }
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void showAutoStartPermDialog(String brandName, final Intent intent)
    {
        String appName=mContext.getResources().getString(R.string.app_name);
        final Dialog dialog =  new Dialog(mContext);
        dialog.setContentView(R.layout.autostart_dialog);
        TextView heading_Txt=dialog.findViewById(R.id.headingTxt);
        TextView desc_Txt=dialog.findViewById(R.id.desc_Txt);
        TextView ok_Txt=dialog.findViewById(R.id.ok);

        ok_Txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (intent != null) {
                        mPrefs.setValue(AddConstants.AutoStartKey, true);
                        dialog.dismiss();
                        startActivity(intent);
                    }
                }catch (ActivityNotFoundException e)
                {
                    e.printStackTrace();
                }

            }
        });

        heading_Txt.setText(appName+" "+mContext.getResources().getString(R.string.need_permission));
        desc_Txt.setText(brandName+" "+mContext.getResources().getString(R.string.custom_ui)+" "+appName+".\n"+mContext.getResources().getString(R.string.need_enable)+" "+appName+" "+mContext.getResources().getString(R.string.towork));

        dialog.show();

    }



    public void setNavAppVersion(String appVersion){
        navAppVersion_Txt.setText(appVersion);
    }


    @Override
    protected void onResume() {
        super.onResume();

        Utility.log_FirebaseActivity_Events(MainActivity.this,"HomeScreen");
        // dispbannerAdd();

        // hide /show the external storage  on the basis  of  userPreference
        if(mContext!=null && mPrefs !=null ) {
            dispDevicesStorage();
        }


        if(Constants.isTextSizeChanged)
        {
            //update the textsize  of RecycletItems;
            cat_adapter = new categoryAdapter(catList);
            recyclerView.setAdapter(cat_adapter);

            toolbar_title.setTextSize(Utility.getFontSizeValueHeading(mContext));

            strgTxt.setTextSize(Utility.getFontSizeValueSubHead(mContext));

            internalTxt.setTextSize(Utility.getFontSizeValueSubHead3(mContext));
            internalTxt_Ext.setTextSize(Utility.getFontSizeValueSubHead3(mContext));

            avlbMemory.setTextSize(Utility.getFontSizeValueSubHead2(mContext));
            avlbMemory_Ext.setTextSize(Utility.getFontSizeValueSubHead2(mContext));
            totalMemmory.setTextSize(Utility.getFontSizeValueSubHead2(mContext));
            totalMemmory_Ext.setTextSize(Utility.getFontSizeValueSubHead2(mContext));
            avlbTxt.setTextSize(Utility.getFontSizeValueSubHead2(mContext));
            avlbTxt_Ext.setTextSize(Utility.getFontSizeValueSubHead2(mContext));

            appNameTxt.setTextSize(Utility.getFontSizeValueHeading(mContext));

            cpu_txt.setTextSize(Utility.getFontSizeValueHeading(mContext));
            boo_txt.setTextSize(Utility.getFontSizeValueHeading(mContext));


            if(drawerNavListAdapter!=null)
                drawerNavListAdapter.notifyDataSetChanged();

            Constants.isTextSizeChanged=false;
        }



    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        *//*to  handle google  in app update*//*
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                System.out.println("update failed"+resultCode);
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
            else
            {
                System.out.println("update success"+requestCode);
            }
        } else if (requestCode == 2296) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        *//*to  handle google  in app update*//*



    }*/

    @Override
    protected void onRestart() {
        super.onRestart();


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    /*private void dispbannerAdd()
    {
        // display add from preferences

        AddMobUtils adutil = new AddMobUtils();

        if(AddConstants.checkIsOnline(mContext) && adContainer !=null && mPrefs !=null)
        {
            String AddPrioverId= mPrefs.getStringValue(AddConstants.ADD_PROVIDER_ID, AddConstants.NOT_FOUND);
            if(AddPrioverId.equalsIgnoreCase(AddConstants.Adsense_Admob_GooglePrivideId)) {
                adutil.displayServerBannerAdd(mPrefs, adContainer, mContext);

            }
            else if(AddPrioverId.equalsIgnoreCase(AddConstants.FaceBookAddProividerId))
            {
                adutil.dispFacebookBannerAdd(mContext, mPrefs, MainActivity.this);
            }

        }
        else {
            adutil.displayLocalBannerAdd(mAdView);
        }



        //  banner add
        // displat add from preferences
    }*/



// biiling code
   /* public void dispAddFreePaymentDiaog()
    {
        try {
            skuDetails= new SkuDetails(Constants.ITEM_SKU_SUBSCIBE);
            skuDetails.getSku();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                 .setSkuDetails( skuDetails)
                 .build();
        BillingResult responseCode = mBillingClient.launchBillingFlow( (Activity) mContext, flowParams);

        Log.d("isAddFreeCode",""+responseCode);
    }

    *//*billing code part *//*
    public void makeConnectioBillingClient()
    {
        *//*in app billing code*//*
        // Establish connection to billing client
        mBillingClient = BillingClient.newBuilder(MainActivity.this).setListener(this).enablePendingPurchases().build();
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {
                //TODO implement your own retry policy
                Toast.makeText(MainActivity.this,  "billing connection failure", Toast.LENGTH_SHORT);


                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }

            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The billing client is ready. You can query purchases here.
                    queryPurchases();
                }
            }
        });


        *//*in app billing code*//*
        // Establish connection to billing client
    }

    *//*google in app billig code functions*//*


    public void queryPurchases()
    {
        //Method not being used for now, but can be used if purchases ever need to be queried in the future
        Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.INAPP);
        if (purchasesResult != null) {
            List<Purchase> purchasesList = purchasesResult.getPurchasesList();
            if (purchasesList == null) {
                return;
            }
            if (!purchasesList.isEmpty()) {
                for (Purchase purchase : purchasesList) {
                    if (purchase.getSkus().equals(Constants.ITEM_SKU_ADREMOVAL)) {
                        mPrefs.setBoolanValue(Constants.pref_remove_ads,true);
                    }
                }
            }
        }

    }

    @Override
    public void onPurchasesUpdated(BillingResult responseCode, @Nullable List<Purchase> purchases) {


        //Handle the responseCode for the purchase
        //If response code is OK,  handle the purchase
        //If user already owns the item, then indicate in the shared prefs that item is owned
        //If cancelled/other code, log the error

        if (responseCode.getResponseCode() == BillingClient.BillingResponseCode.OK
                && purchases != null) {
            for (Purchase purchase : purchases) {
                handlePurchase(purchase);
            }
        } else if (responseCode.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            Log.d("billing status", "User Canceled" + responseCode);
        } else if (responseCode.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            mPrefs.setBoolanValue(Constants.pref_remove_ads,true);
            //setAdFree(true);
            //layout_remove_adds.setVisibility(View.GONE);
        } else {
            Log.d("billing status", "Other code" + responseCode);
            // Handle any other error codes.
        }
    }

    public  Boolean queryPrefPurchases() {
        Boolean adFree = mPrefs.getBoolanValue(Constants.pref_remove_ads, false);

        if (adFree) {

        }

        return adFree;

    }

    private void handlePurchase(Purchase purchase) {
        if (purchase.getSkus().equals(Constants.ITEM_SKU_ADREMOVAL)) {
            mPrefs.setBoolanValue(Constants.pref_remove_ads,true);
            // setAdFree(true);
        }
    }*/

  /*  @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                initActivityComponents();
                iniVars();
                getPushToken();


            } else if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, permissionsRequired[0]) || ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, permissionsRequired[1])) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(mContext.getResources().getString(R.string.need_permission));
                builder.setMessage(mContext.getString(R.string.app_name) + mContext.getResources().getString(R.string.storage_needed));
                builder.setPositiveButton(mContext.getResources().getString(R.string.grant), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions((Activity) mContext,permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton(mContext.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //Toast.makeText(mContext, "Unable to get Permission", Toast.LENGTH_LONG).show();
                Utility.dispToast(mContext, getResources().getString(R.string.unable_get_permission));
            }
        }


    }*/


    private void iniVars() {

        category_Model cat_Img=new category_Model(getResources().getString(R.string.cat_Images));
        category_Model cat_Apk=new category_Model(getResources().getString(R.string.cat_Apk));
        category_Model cat_Animation=new category_Model(getResources().getString(R.string.cat_Animation));
        category_Model cat_Audio=new category_Model(getResources().getString(R.string.cat_Audio));
        category_Model cat_Video=new category_Model(getResources().getString(R.string.cat_Videos));
        category_Model cat_Download=new category_Model(getResources().getString(R.string.cat_Download));
        category_Model cat_Document=new category_Model(getResources().getString(R.string.cat_Documents));
        category_Model cat_Recent=new category_Model(getResources().getString(R.string.cat_Recent));
        category_Model cat_zip=new category_Model(getResources().getString(R.string.cat_zip_title));
        category_Model cat_Locker=new category_Model(getResources().getString(R.string.cat_hidden));
        category_Model cat_game=new category_Model("Free games");

        catList.add(cat_Img);
        catList.add(cat_Video);
        catList.add(cat_Audio);

        catList.add(cat_game);
        catList.add(cat_Document);
        catList.add(cat_Download);
        catList.add(cat_Animation);
        catList.add(cat_Recent);
        catList.add(cat_Apk);
        catList.add(cat_zip);
        catList.add(cat_Locker);




        cat_adapter = new categoryAdapter(catList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.HORIZONTAL));
        // recyclerView.setItemAnimator(new DefaultItemAnimator());
        if(cat_adapter!=null)
            recyclerView.setAdapter(cat_adapter);



        if(mContext!=null ) {

            fillProgressBar();
            try {
                String sdCardPath = UtilityStorage.getExternalStoragePath(mContext, true);
                // if sdcard is ejected the returned path will not exist;
                if (sdCardPath != null && Utility.isPathExist(sdCardPath, mContext) ) {
                    ext_layout.setVisibility(View.VISIBLE);
                    fillProgressBar_Ext();
                }
                else {

                    ext_layout.setVisibility(View.GONE);
                }
            } catch (Exception e) {

            }



        }




    }

    private void dispDevicesStorage() {

        if(storage_section !=null) {

            if (mPrefs != null && Utility.hasUserHideStorages(mPrefs)) {
                storage_section.setVisibility(View.GONE);
            } else {
                storage_section.setVisibility(View.VISIBLE);
            }
        }



    }

    private void initActivityComponents()
    {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerObjectItemList[] drawerItem = new DrawerObjectItemList[4];

        drawerItem[0] = new DrawerObjectItemList(R.drawable.ic_menu_settings, getResources().getString(R.string.nav_settings));
//        drawerItem[1] = new DrawerObjectItemList(R.drawable.ic_menu_about, "About");
        drawerItem[1] = new DrawerObjectItemList(R.drawable.ic_menu_share, getResources().getString(R.string.nav_share));
        drawerItem[2] = new DrawerObjectItemList(R.drawable.ic_suggested_app, getResources().getString(R.string.txtsuggestedApps));
        drawerItem[3] = new DrawerObjectItemList(R.drawable.ic_privacy_policy, getResources().getString(R.string.nav_privacy_policy));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            //Called when a drawer has settled in a completely closed state.
            public void onDrawerClosed(View view) {

                super.onDrawerClosed(view);
            }

            //Called when a drawer has settled in a completely open state. /
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mDrawerList = (ListView) findViewById(R.id.drawerMenuList);
        drawerNavListAdapter = new DrawerNavListAdapter(MainActivity.this, R.layout.drawer_item_listview, drawerItem);
        mDrawerList.setAdapter(drawerNavListAdapter);
        appNameTxt=findViewById(R.id.appNameTxt);
        appNameTxt.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(mContext));

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        redirectToActivity(SettingsActivity.class);
                        drawerClosed();
                        break;
                    case 1:
                        shareApp();
                        drawerClosed();
                        break;
                    case 2:
                        redirectToActivity(SuggestedAppsActivity.class);
                        drawerClosed();
                        break;
                    case 3:
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.privacyUrl));
                        startActivity(browserIntent);
                        drawerClosed();
                        break;
                }
            }
        });

        recyclerView = findViewById(R.id.recycler_view);

        storage_section=findViewById(R.id.storage_section);
        storage_section.setVisibility(View.VISIBLE);
        ext_layout =  findViewById(R.id.ext_layout);
        internalLLayout=findViewById(R.id.internalLLayout);
        progressBar = findViewById(R.id.progressBar);
        avlbMemory =  findViewById(R.id.avlbMemory);
        totalMemmory = findViewById(R.id.totalMemmory);
        internalTxt =  findViewById(R.id.internalTxt);
        avlbTxt =  findViewById(R.id.avlbTxt);

        progressBar_Ext =  findViewById(R.id.progressBar_ext);
        avlbMemory_Ext =  findViewById(R.id.avlbMemory_ext);
        totalMemmory_Ext =  findViewById(R.id.totalMemmory_ext);
        internalTxt_Ext = findViewById(R.id.externalTxt);
        avlbTxt_Ext =  findViewById(R.id.avlbTxt_ext);

        toolbar_title=findViewById(R.id.toolbar_title);
        strgTxt=findViewById(R.id.strgTxt);



        //  new module

        boost_phone_layout=findViewById(R.id.boost_phone_layout);
        boost_phone_layout.setVisibility(View.VISIBLE);
        clearJunkBtn=findViewById(R.id.clearJunkBtn);
        freeRamBtn=findViewById(R.id.freeRamBtn);
        //duplicateImgBtn=findViewById(R.id.duplicateImgBtn);

        cpu_txt=findViewById(R.id.cpu_txt);
        boo_txt=findViewById(R.id.boo_txt);

        cpu_txt.setTextSize(Utility.getFontSizeValueHeading(mContext));
        boo_txt.setTextSize(Utility.getFontSizeValueHeading(mContext));

        //TextView dup_txt=findViewById(R.id.dup_txt);

        cpu_txt.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(mContext));
        boo_txt.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(mContext));
        //dup_txt.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(mContext));
        ViewDialog dialog=new ViewDialog(this);

        clearJunkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                redirectToActivity(JunkCleanActivity.class);
            }
        });

        freeRamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToActivity(BoostFragment.class);
                //redirectToActivity(Actitivcity_Boost_Phone.class);
            }
        });

        /* duplicateImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
*/

        // new module

        //setTypeFce
        //setTypeFce
        if(mContext!=null) {


            toolbar_title.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(mContext));
            avlbTxt_Ext.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(mContext));
            internalTxt_Ext.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(mContext));
            avlbMemory_Ext.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(mContext));
            avlbTxt.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(mContext));
            internalTxt.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(mContext));
            totalMemmory.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(mContext));

            totalMemmory_Ext.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(mContext));
            avlbMemory.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(mContext));
            strgTxt.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(mContext));



        }

        internalLLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentImageGallary = new Intent(mContext, Activity_Stotrage.class);
                intentImageGallary.putExtra(Constants.storageType, Constants.interNal);
                startActivity(intentImageGallary);
            }
        });

        ext_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentImageGallary = new Intent(mContext, Activity_Stotrage.class);
                intentImageGallary.putExtra(Constants.storageType, Constants.sdCard);
                startActivity(intentImageGallary);
            }
        });

        customAddImage=findViewById(R.id.customAddImage);
        startTimer();
    }

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 2000ms the TimerTask will run every 60000ms
        timer.schedule(timerTask, 5000, 60000); //
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {

                        if(isNetworkAvailable) {
                            new WebCallGetCustomAdds(mContext, MainActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }
                    }
                });
            }
        };
    }




    private void fillProgressBar() {


        progressBar.setProgress(0);
        progressBar.setMax(100);
        long TotalInternalMemory = Utility.getTotalInternalMemorySize();
        long AvailableInternalMemory = Utility.getAvailableInternalMemorySize();


        float per = (float) AvailableInternalMemory / (float) (TotalInternalMemory / 100);
        //System.out.print("Memory Stats--> Total " + TotalInternalMemory + " Avaailable" + AvailableInternalMemory + "" + Utility.setdecimalPoints(String.valueOf(per), 2));
        // avlbMemory.setText(Utility.formatSize(Utility.getAvailableInternalMemorySize()));
        avlbMemory.setText(Utility.humanReadableByteCount(Utility.getAvailableInternalMemorySize(), true) + "(" + Utility.setdecimalPoints(String.valueOf(per), 2) + "%)");
        // totalMemmory.setText("Total "+Utility.formatSize(Utility.getTotalInternalMemorySize()));
        totalMemmory.setText(getResources().getString(R.string.total) +" "+ Utility.humanReadableByteCount(Utility.getTotalInternalMemorySize(), true));
        int progress = 100 - (int) per;
        progressBar.setProgress(progress);


    }

    private void fillProgressBar_Ext() {


        progressBar_Ext.setProgress(0);
        progressBar_Ext.setMax(100);
        long TotalMemory_Ext = Utility.getTotalExternalMemorySize(UtilityStorage.getExternalStoragePath(mContext, true));
        long AvailableMemory_Ext = Utility.getAvailableExternalMemorySize(UtilityStorage.getExternalStoragePath(mContext, true));


        long per = AvailableMemory_Ext / (TotalMemory_Ext / 100);
        // System.out.print("Memory Stats--> Total " + TotalMemory_Ext + " Avaailable" + AvailableMemory_Ext + "" + per);

        // avlbMemory_Ext.setText(Utility.humanReadableByteCount(AvailableMemory_Ext, true) + "(" + per + "%)");   // old code
        avlbMemory_Ext.setText(Utility.humanReadableByteCount(AvailableMemory_Ext, true) + "(" + Utility.setdecimalPoints(String.valueOf(per), 2) + "%)");

        totalMemmory_Ext.setText(getResources().getString(R.string.total) +" "+ Utility.humanReadableByteCount(TotalMemory_Ext, true));
        int progress = 100 - (int) per;
        progressBar_Ext.setProgress(progress);


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        //menuInflater.inflate(R.menu.menu, menu);
//        if(storage_section!=null)
//        {
//            if (storage_section.getVisibility() == View.GONE) {
//                MenuItem item1 = menu.findItem(R.id.showstorage);
//                MenuItem item2 = menu.findItem(R.id.hidestorage);
//                item1.setVisible(true);
//                item2.setVisible(false);
//            }
//            if (storage_section.getVisibility() == View.VISIBLE) {
//                MenuItem item1 = menu.findItem(R.id.showstorage);
//                MenuItem item1 = menu.findItem(R.id.showstorage);
//                MenuItem item2 = menu.findItem(R.id.hidestorage);
//                item1.setVisible(false);
//                item2.setVisible(true);
//
//                // will  work on it later for scrolling to  make stofrage visible when visible  is clicked
//                if(recyclerView !=null && catList !=null)
//                    recyclerView.scrollToPosition(catList.size() - 1);
//
//
//            }
//        }
        return true;
    }



    private void showHidestorage(int flag)
    {
        switch (flag)
        {

            case  1:

                storage_section.setVisibility(View.VISIBLE);
                invalidateOptionsMenu();// this calls oncreateOptiosMenu there we we show/hide storage
                break;
            case 2:
                storage_section.setVisibility(View.GONE);
                invalidateOptionsMenu();// this calls oncreateOptiosMenu there we we show/hide storage


                break;

        }


    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

//            case R.id.showstorage:
//                showHidestorage(1);
//                break;
//            case R.id.hidestorage:
//                showHidestorage(2);
//                break;
        }
        return true;
    }




    private void redirectToActivity(Class targetClass)
    {
        Intent i =  new Intent(MainActivity.this,targetClass);
        startActivity(i);
    }



    @Override
    public void onForgotClick(Class targetClass) {
        mPrefs.setStringValue(Constants.SECURITY_QUES_TYPE, Constants.FORGOT_PASS_QUES);
        redirectToActivity(targetClass);
    }

    @Override
    public void onCancelClick() {
    }


    @Override
    public int onEncryptClick(String password) {
        String s  =  Utility.readPasswordFile();
        //Utility.dispToast(ctx,""+s.toString());
        if(s.equals(password))
        {
            redirectToActivity(LockerActivityMain.class);
            return 1;
        }
        else {
            Utility.dispToast(mContext,getResources().getString(R.string.passwordnotmatch));
            return  0;
        }
    }


    @Override
    public void onRefreshAdd() {
        dispCustomAdd();
    }




    private  void dispCustomAdd()
    {
        try {

            if (!MainActivity.this.isFinishing())
            {
                String country_code = Utility.getCountryCode(mContext);
                if (country_code == null || country_code.equalsIgnoreCase("")) {
                    country_code = Utility.getLocale(mContext);
                }
                boolean countryCodeMatch = country_code.equalsIgnoreCase(mPrefs.getStringValue(AddConstants.COUNTRY_CODE, "")) ? true : false;

                if (mPrefs != null && mPrefs.getStringValue(AddConstants.CUSTOM_ADD_RUNNING, "0").equalsIgnoreCase("1") && countryCodeMatch) {
                    String banner = mPrefs.getStringValue(AddConstants.BANNER_PATH, "0");

                    if (!banner.equalsIgnoreCase("")) {
                        Glide.with(mContext).load(banner)
                                .diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.IMMEDIATE)
                                .skipMemoryCache(false).placeholder(R.drawable.image_holder).error(R.drawable.image_holder)
                                .into(customAddImage);
                        customAddImage.setVisibility(View.VISIBLE);
                        final String landingUrl = mPrefs.getStringValue(AddConstants.LANDING_URL, "0");
                        customAddImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (landingUrl != null) {

                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(landingUrl));
                                    startActivity(i);

                                }


                            }
                        });
                    }
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            //System.out.print(""+e.getMessage());
        }

    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    public class categoryAdapter extends RecyclerView.Adapter<categoryAdapter.categoryViewHolder> {

        List<category_Model> catList;
        @Override
        public categoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_item_layout, parent, false);

            return new categoryViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final categoryViewHolder holder, final int position) {
            holder.catName.setText(catList.get(position).getCatName());


            switch (holder.getAdapterPosition()) {
                case 0:
                    holder.catIcon.setImageResource(R.drawable.cat_ic_image);
                    //holder.catIcon.setImageResource(R.drawable.ic_image);
                    break;
                case 1:
                    holder.catIcon.setImageResource(R.drawable.cat_ic_vdo);
                    break;
                case 2:
                    holder.catIcon.setImageResource(R.drawable.cat_ic_music);
                    break;
                case 3:
                    holder.catIcon.setImageResource(R.drawable.cat_game);
                    break;
                case 4:
                    holder.catIcon.setImageResource(R.drawable.cat_ic_docs);
                    break;
                case 5:
                    holder.catIcon.setImageResource(R.drawable.cat_ic_download);
                    break;
                case 6:
                    holder.catIcon.setImageResource(R.drawable.cat_ic_animation);
                    break;
                case 7:
                    holder.catIcon.setImageResource(R.drawable.cat_ic_recent);
                    break;
                case 8:
                    holder.catIcon.setImageResource(R.drawable.cat_ic_apk);
                    break;
                case 9:
                    holder.catIcon.setImageResource(R.drawable.cat_ic_zip);
                    break;
                case 10:
                    holder.catIcon.setImageResource(R.drawable.cat_ic_locker);
                    break;
                default:
                    holder.catIcon.setImageResource(R.drawable.cat_ic_locker);
                    break;
            }

            holder.catName.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(mContext));
            //holder.itemCount.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(getActivity()));



            holder.container_Layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    switch (holder.getAdapterPosition())
                    {
                        case 3:
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("https://www.gamezop.com/?id=88dn5QypL"));
                            startActivity(intent);
                            break;
                        case 4:
                            redirectToActivity(DocsActivityRe.class);
                            break;
                        case 5:
                            redirectToActivity(DownloadActivityRe.class);
                            break;
                        case 6:
                            redirectToActivity(AnimationActivityRe.class);
                            break;
                        case 7:
                            redirectToActivity(RecentActivityRe.class);
                            break;
                        case 8:
                            redirectToActivity(ApkActivityRe.class);
                            break;
                        case 9:
                            redirectToActivity(ZipActivityRe.class);
                            break;

                        case 10:

                            String  schoolFirstSetQues = Utility.readQuestionsFile(Constants.schoolDir,  Constants.schoolQuesFileDes);
                            String  shoesFirstSetQues = Utility.readQuestionsFile(Constants.shoesDir,  Constants.shoesQuesFileDes);
                            String  sportFirstSetQues = Utility.readQuestionsFile(Constants.sportDir,  Constants.sportQuesFileDes);

                            if(Utility.isManualPasswordSet() && !schoolFirstSetQues.isEmpty() && !shoesFirstSetQues.isEmpty() && !sportFirstSetQues.isEmpty() )
                            {
                                new EncryptDialogUtility(MainActivity.this).fileEncryptPasswordDialog(mContext);
                            }
                            else if(!Utility.isManualPasswordSet() && Utility.readPasswordFile().isEmpty() )
                            {
                                mPrefs.setStringValue(Constants.SECURITY_QUES_TYPE, Constants.SET_PASS_QUES);
                                redirectToActivity(LockerPasswordActivity.class);
                            }
                            else if(!Utility.readPasswordFile().isEmpty() && schoolFirstSetQues.isEmpty() && shoesFirstSetQues.isEmpty() && sportFirstSetQues.isEmpty() )
                            {
                                mPrefs.setStringValue(Constants.SECURITY_QUES_TYPE, Constants.SET_PASS_QUES);
                                redirectToActivity(SecurityQuesActivity.class);
                            }
                            else if(!Utility.readPasswordFile().isEmpty() || schoolFirstSetQues.isEmpty() || shoesFirstSetQues.isEmpty() || sportFirstSetQues.isEmpty() )
                            {
                                mPrefs.setStringValue(Constants.SECURITY_QUES_TYPE, Constants.SET_PASS_QUES);
                                redirectToActivity(SecurityQuesActivity.class);
                            }
                            else
                            {
                                mPrefs.setStringValue(Constants.SECURITY_QUES_TYPE, Constants.SET_PASS_QUES);
                                redirectToActivity(LockerPasswordActivity.class);
                            }


//                            if(Utility.isManualPasswordSet())
//                            {
//                              new EncryptDialogUtility(MainActivity.this).fileEncryptPasswordDialog(mContext);
//                            }
//                            else
//                            {
//                                addprefs.setStringValue(Constants.SECURITY_QUES_TYPE, Constants.SET_PASS_QUES);
//                                redirectToActivity(LockerPasswordActivity.class);
//                            }

                            break;

                        default:
                            Intent i = new Intent(mContext, Category_Explore_Activity.class);
                            i.putExtra(POSITION, holder.getAdapterPosition());
                            startActivity(i);
                            break;
                    }




                }
            });


        }

        @Override
        public int getItemCount() {
            return catList.size();
        }

        public class categoryViewHolder extends RecyclerView.ViewHolder {
            public TextView catName;
            RelativeLayout container_Layout;
            ImageView catIcon;



            public categoryViewHolder(View view) {
                super(view);

                catName =  view.findViewById(R.id.catName);
                catIcon =  view.findViewById(R.id.cat_Icon);
                container_Layout=view.findViewById(R.id.container_Layout);

                catName.setTextSize(Utility.getFontSizeValueHeading(mContext));


            }
        }

        public categoryAdapter(List<category_Model> catList) {

            this.catList=catList;
        }

    }


    public class WebCall extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected String doInBackground(String... strings) {
            String versioName="0";
            int versionCode=0;
            try {
                versioName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                versionCode = getPackageManager().getPackageInfo(getPackageName(),0 ).versionCode;
                Log.d("currentVersion", "" + versioName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            catch (Exception e)
            {
                // handel any other exception
            }

            try {
                JSONObject requestObj= AddConstants.prepareAddJsonRequest(mContext, AddConstants.VENDOR_ID , versioName ,versionCode );

                return OkhttpMethods.CallApi(mContext,AddConstants.API_URL,requestObj.toString());
            } catch (IOException e) {
                e.printStackTrace();
                return ""+e.getMessage();
            }
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.d("JsonResponse", s);
            //long ms2=System.currentTimeMillis();
            //System.out.print("Milliseconds after-->>"+ms2);
            //Log.e("Milliseconds after-->>", ""+ms2);
            if (mPrefs != null)
            {
                int responseCode= mPrefs.getIntValue(AddConstants.API_RESPONSE_CODE, 0);

                if (s != null  && responseCode==200 ) {
                    Log.d("Addjson", s.toString());
                    try {
                        JSONObject mainJson = new JSONObject(s);
                        if (mainJson.has("status")) {
                            String status = JsonParser.getkeyValue_Str(mainJson, "status");

                            String newVersion=JsonParser.getkeyValue_Str(mainJson,"appVersion");
                            mPrefs.setValue(AddConstants.APP_VERSION, newVersion);
                            //addprefs.setValue(AddConstants.APP_VERSION, "1.29");

                            if (status.equalsIgnoreCase("true")) {

                                String adShow = JsonParser.getkeyValue_Str(mainJson, "AdShow");

                                if (adShow.equalsIgnoreCase("true")) {
                                    if (mainJson.has("data")) {
                                        JSONObject dataJson = mainJson.getJSONObject("data");
                                       // AddMobUtils util = new AddMobUtils();
                                        String show_Add = JsonParser.getkeyValue_Str(mainJson, "AdShow");

                                        String adProviderId =JsonParser.getkeyValue_Str(dataJson, "adProviderId");
                                        String adProviderName = JsonParser.getkeyValue_Str(dataJson, "adProviderName");

                                        String appId_PublisherId = JsonParser.getkeyValue_Str(dataJson, "appId_PublisherId");
                                        String bannerAdId = JsonParser.getkeyValue_Str(dataJson, "bannerAdId");
                                        String interstitialAdId = JsonParser.getkeyValue_Str(dataJson, "interstitialAdId");
                                        String videoAdId = JsonParser.getkeyValue_Str(dataJson, "videoAdId");
                                        /*String appId_PublisherId = "ca-app-pub-3940256099942544~3347511713";//testID
                                        String bannerAdId = "ca-app-pub-3940256099942544/6300978111"; //testId
                                        String interstitialAdId = "ca-app-pub-3940256099942544/1033173712";//testId
                                        String videoAdId = "ca-app-pub-3940256099942544/5224354917";//testId*/
                                        //Log.d("AddiDs", adProviderName + " ==" + appId_PublisherId + "==" + bannerAdId + "==" + interstitialAdId + "==" + videoAdId);
                                        //check for true value above in code so  can put true directly;
                                        try {
                                            mPrefs.setValue(AddConstants.SHOW_ADD, Boolean.parseBoolean(show_Add));
                                        }catch (Exception e)
                                        {
                                            // IN CASE OF EXCEPTION CONSIDER  FALSE AS THE VALUE WILL NOT BE TRUE,FALSE.
                                            mPrefs.setValue(AddConstants.SHOW_ADD, false);
                                        }
                                        //addprefs.setValue(AddConstants.APP_VERSION, newVersion);
                                        //addprefs.setValue(AddConstants.APP_VERSION, "1.22");
                                        mPrefs.setValue(AddConstants.ADD_PROVIDER_ID, adProviderId);
                                        mPrefs.setValue(AddConstants.APP_ID, appId_PublisherId);
                                        mPrefs.setValue(AddConstants.BANNER_ADD_ID, bannerAdId);
                                        mPrefs.setValue(AddConstants.INTERESTIAL_ADD_ID, interstitialAdId);
                                        mPrefs.setValue(AddConstants.VIDEO_ADD_ID, videoAdId);

                                        if (adContainer != null  && adProviderId.equalsIgnoreCase(AddConstants.Adsense_Admob_GooglePrivideId))
                                        {
                                            // requst googleAdd
                                           // util.displayServerBannerAdd(mPrefs, adContainer, mContext);
                                            // util.showInterstitial(addprefs,HomeActivity.this, interstitialAdId);
                                            //util.displayRewaredVideoAdd(addprefs,mContext, videoAdId);
                                        }
                                        else if (adProviderId.equalsIgnoreCase(AddConstants.InMobiProvideId))
                                        {
                                            // inmobi adds not being implemented in this version
                                            // inmobi adds not being implemented in this version
                                        }
                                        else  if(adProviderId.equalsIgnoreCase(AddConstants.FaceBookAddProividerId))
                                        {
                                            //util.dispFacebookBannerAdd(mContext, addprefs,MainActivity.this);
                                        }

                                    } else {
                                        String message = JsonParser.getkeyValue_Str(mainJson, "message");
                                        Log.d("message", "" + message);
                                    }
                                } else {
                                    String message = JsonParser.getkeyValue_Str(mainJson, "message");

                                    Log.d("message", "" + message);

                                }


                            }

                            //call dispUpdateDialog
                            // dispUpdateDialog();  // now google  in  app  update will be used ;



                        }

                    } catch (JSONException e) {
                        Log.d("jsonParse", "error while parsing json -->" + e.getMessage());
                        e.printStackTrace();
                    }


                } else {
                    // display loccal AddiDs Adds;
//                    if (mAdView != null) {
//                        AddMobUtils util = new AddMobUtils();
//                        util.displayLocalBannerAdd(mAdView);
//                        //util.showInterstitial(addprefs,HomeActivity.this, null);
//                        // util.displayRewaredVideoAdd(addprefs,mContext, null);
//                    }
                }


            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try
        {
            stoptimertask();
        }
        catch (Exception e)
        {

        }


        // clear any  copied data on app  close just  like filego  app;
        if(Constants.filesToCopy.size()>=1)
        {
            Constants.filesToCopy.clear();
        }
        if(internetChangerReceiver !=null)
            unregisterReceiver(internetChangerReceiver);

        clearApplicationData(mContext);
    }

    public void clearApplicationData(Context context) {
        File cache = context.getCacheDir();

        File appDir = new File(cache.getParent());

        long bytes=dirSize(appDir);

        System.out.println(""+bytes);
        String str=Utility.humanReadableByteCount(bytes,true);
        System.out.println(""+str);
        long size=1024*1024 *50;  //1024*1024 *50  50 mb;
        System.out.print(""+size);
        if(bytes>size)

            if (appDir.exists()) {
                String[] children = appDir.list();
                for (String s : children) {
                    if (!s.equals("lib") || !s.equalsIgnoreCase("shared_prefs")) {
                        deleteDir(new File(appDir, s));
                        Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                    }
                }
            }
    }
    public static boolean deleteDir(File dir) {
        long bytes=dir.length();
        System.out.println(""+bytes);

        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        }
        else if(dir!= null && dir.isFile())
        {
            return dir.delete();
        } else {
            return false;
        }
    }

    private static long dirSize(File dir) {

        if (dir.exists()) {
            long result = 0;
            File[] fileList = dir.listFiles();
            for(int i = 0; i < fileList.length; i++) {
                // Recursive call if it's a directory
                if(fileList[i].isDirectory()) {
                    result += dirSize(fileList [i]);
                } else {
                    // Sum the file size in bytes
                    result += fileList[i].length();
                }
            }
            return result; // return the file size
        }
        return 0;
    }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // super.onBackPressed();
        }


        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        // Toast.makeText(this, "Please press again to exit.", Toast.LENGTH_SHORT).show();
        Utility.dispToast(mContext, getResources().getString(R.string.pressagain));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }





    private void shareApp() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "FileHunt");
        share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.mojodigi.filehunt&hl=en");
        startActivity(Intent.createChooser(share, "FileHunt"));

    }











    // this web call send token to  server;
    public class PushNotificationCall extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {

            try {
//                Log.e("deviceId ", ""+deviceID);
//                Log.e("deviceName ", ""+nameOfDevice);
//                Log.e("fcmToken ", ""+fcm_Token);
//                Log.e("appVer ", ""+appVersionName);
                JSONObject requestObj = AddConstants.prepareFcmJsonRequest(mContext, deviceID, nameOfDevice, fcm_Token , appVersionName);
                return OkhttpMethods.CallApi(mContext, AddConstants.API_PUSH_NOTIFICATION, requestObj.toString());

            } catch (IOException e) {
                e.printStackTrace();
                return ""+e.getMessage();
            }
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e("Push Json Response ", ""+s);

            if (mPrefs != null)
            {
                int responseCode= mPrefs.getIntValue(AddConstants.API_RESPONSE_CODE, 0);

                if (s != null  && responseCode==200 ) {
                    try {
                        JSONObject mainJson = new JSONObject(s);
                        if (mainJson.has("status")) {
                            String status = JsonParser.getkeyValue_Str(mainJson, "status");
                            Log.e("status", "" + status);


                            if (status.equalsIgnoreCase("false")) {

                                if (mainJson.has("data")) {
                                    JSONObject dataJson = mainJson.getJSONObject("data");
                                } else {
                                    String message = JsonParser.getkeyValue_Str(mainJson, "message");
                                    Log.e("message", "" + message);
                                }
                            }
                            if (status.equalsIgnoreCase("false")) {
                                Log.e("status", "" + status);

                                if(max_execute<=5){
                                    new PushNotificationCall().execute();
                                    max_execute++;
                                }
                            }
                            else {
                                if(mPrefs !=null)
                                    mPrefs.setValue(Constants.isFcmRegistered, true);
                            }
                        }
                    } catch (JSONException e) {
                        Log.d("jsonParse", "error while parsing json -->" + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    Log.e("", "else");
                }
            }
        }
    }


    /*now google vin app  update  will  be used */
    private void dispUpdateDialog() {
        try {
            String currentVersion = "0";
            String newVersion="0";
            if(mPrefs !=null)
                newVersion= mPrefs.getStringValue(AddConstants.APP_VERSION, AddConstants.NOT_FOUND);

            try {
                currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                Log.d("currentVersion", "" + currentVersion);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            if (Float.parseFloat(newVersion) > Float.parseFloat(currentVersion) && !newVersion.equalsIgnoreCase("0"))

            {
                if (mContext != null) {
                    final Dialog dialog = new Dialog(mContext);
                    dialog.setContentView(R.layout.dialog_version_update);
                    long time = mPrefs.getLongValue("displayedTime", 0);
                    long diff=86400000; // one day
                    //long diff=60000; // one minute;

                    if (time < System.currentTimeMillis() - diff) {
                        dialog.show();
                        mPrefs.setValue("displayedTime", System.currentTimeMillis());
                    }


                    TextView later = dialog.findViewById(R.id.idDialogLater);
                    TextView updateNow = dialog.findViewById(R.id.idDialogUpdateNow);
                    TextView idVersionDetailsText = dialog.findViewById(R.id.idVersionDetailsText);
                    TextView idAppVersionText = dialog.findViewById(R.id.idAppVersionText);
                    TextView idVersionTitleText = dialog.findViewById(R.id.idVersionTitleText);


                    idVersionTitleText.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(mContext));
                    idVersionDetailsText.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(mContext));
                    idAppVersionText.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(mContext));
                    later.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(mContext));
                    updateNow.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(mContext));

                    idAppVersionText.setText(newVersion);


                    later.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialog.dismiss();
                        }
                    });


                    updateNow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            final String appPackageName = getPackageName(); // package name of the app
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }


                            dialog.dismiss();
                        }
                    });


                }


            }
        }
        catch (Exception e)
        {

        }

    }

    private void drawerClosed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }
}

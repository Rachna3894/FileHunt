package com.mojodigitech.filehunt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mojodigitech.filehunt.AddsUtility.AddConstants;
import com.mojodigitech.filehunt.AddsUtility.SharedPreferenceUtil;
import com.mojodigitech.filehunt.Class.Constants;
import com.mojodigitech.filehunt.Utils.EncryptDialogUtility;
import com.mojodigitech.filehunt.Utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity implements  EncryptDialogUtility.EncryptDialogListener/*PurchasesUpdatedListener*/ {


  //  private BillingClient mBillingClient;
    private Context mContext;
    private TextView txtDispSmallFile, txtHideexternal, txtShowHiddenFiles, txtTextSize , txtUpdateSecurityQues,txtRemoveAdds;
    private Switch switchHiddenfile, switchHideStorage, switchSmallFile;

    //RelativeLayout removeAddsLayout;
    Spinner txtSize_Spinner;
    List<Integer> spinnerArray = new ArrayList<>();
    SharedPreferenceUtil mPrefs;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        initializeComponent();
        setTextViewFontSize();
    }


    private void initializeComponent() {

        mContext=SettingsActivity.this;
        if(mContext!=null)
            mPrefs =new SharedPreferenceUtil(mContext);
        Utility.setActivityTitle2(mContext,getResources().getString(R.string.title_settings));
        txtDispSmallFile= findViewById(R.id.txtDispSmallFile);
        txtHideexternal= findViewById(R.id.txtHideexternal);
        txtShowHiddenFiles=findViewById(R.id.txtShowHiddenFiles);
        txtTextSize=findViewById(R.id.txtTextSize);
        //txtRemoveAdds=findViewById(R.id.txtRemoveAdds);
        //removeAddsLayout=findViewById(R.id.removeAddsLayout);
        txtUpdateSecurityQues=findViewById(R.id.txtUpdateSecurityQues);


        switchHiddenfile= findViewById(R.id.switchHiddenfile);
        switchHideStorage=findViewById(R.id.switchHideStorage);
        switchSmallFile=findViewById(R.id.switchSmallFile);
        txtSize_Spinner=findViewById(R.id.txtSize_Spinner);

        spinnerArray.add(16);
        spinnerArray.add(17);
        spinnerArray.add(18);
        spinnerArray.add(19);
        spinnerArray.add(20);
        spinnerArray.add(21);
        spinnerArray.add(22);
        spinnerArray.add(23);
        spinnerArray.add(24);


        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(
                mContext,
                android.R.layout.simple_spinner_dropdown_item,
                spinnerArray
        );

        txtSize_Spinner.setAdapter(adapter);

        txtDispSmallFile.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(mContext));
        txtHideexternal.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(mContext));
        txtShowHiddenFiles.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(mContext));
        txtTextSize.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(mContext));
        txtUpdateSecurityQues.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(mContext));
       // txtRemoveAdds.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(mContext));









        txtSize_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                mPrefs.setValue(AddConstants.KEY_TEXT_SIZE_INDEX, i);
                Constants.isTextSizeChanged=true;

                txtDispSmallFile.setTextSize(Integer.parseInt(txtSize_Spinner.getSelectedItem().toString()));
                txtHideexternal.setTextSize(Integer.parseInt(txtSize_Spinner.getSelectedItem().toString()));
                txtShowHiddenFiles.setTextSize(Integer.parseInt(txtSize_Spinner.getSelectedItem().toString()));
                txtTextSize.setTextSize(Integer.parseInt(txtSize_Spinner.getSelectedItem().toString()));
               // txtRemoveAdds.setTextSize(Integer.parseInt(txtSize_Spinner.getSelectedItem().toString()));
                txtUpdateSecurityQues.setTextSize(Integer.parseInt(txtSize_Spinner.getSelectedItem().toString()));

//                Utility.setActivityTitle2(mContext,getResources().getString(R.string.title_settings));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        boolean isAddFree=mPrefs.getBoolanValue(Constants.pref_remove_ads, false);
// it will be enable when application will be live o google play store
       /* if(isAddFree)
        {
            removeAddsLayout.setVisibility(View.GONE);
        }

        removeAddsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispAddFreePaymentDiaog();



            }
        });*/

        switchHideStorage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

                if(checked) {
                    mPrefs.setValue(AddConstants.KEY_HIDE_EXTERNAL_STORAGE, true);
                }
                else {
                    mPrefs.setValue(AddConstants.KEY_HIDE_EXTERNAL_STORAGE, false);
                }

            }
        });

        switchHiddenfile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

                if(checked) {
                    mPrefs.setValue(AddConstants.KEY_SHOW_HIDDEN_FILE, true);
                }
                else {
                    mPrefs.setValue(AddConstants.KEY_SHOW_HIDDEN_FILE, false); }
            }
        });

        switchSmallFile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked) {
                    mPrefs.setValue(AddConstants.KEY_DISPLAY_SMALL_FILE, true);
                }
                else {
                    mPrefs.setValue(AddConstants.KEY_DISPLAY_SMALL_FILE, false);
                }
            }
        });



        txtUpdateSecurityQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  schoolFirstSetQues = Utility.readQuestionsFile(Constants.schoolDir,  Constants.schoolQuesFileDes);
                String  shoesFirstSetQues = Utility.readQuestionsFile(Constants.shoesDir,  Constants.shoesQuesFileDes);
                String  sportFirstSetQues = Utility.readQuestionsFile(Constants.sportDir,  Constants.sportQuesFileDes);

                if(Utility.isManualPasswordSet() && !schoolFirstSetQues.isEmpty() && !shoesFirstSetQues.isEmpty() && !sportFirstSetQues.isEmpty() )
                {
                    mPrefs.getStringValue(Constants.SECURITY_QUES_TYPE, Constants.UPDATE_PASS_QUES);
                    new EncryptDialogUtility(SettingsActivity.this).fileEncryptPasswordDialog(mContext);
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


//                if(Utility.isManualPasswordSet())
//                {
//                    sharedPrefs.getStringValue(Constants.SECURITY_QUES_TYPE, Constants.UPDATE_PASS_QUES);
//                    new EncryptDialogUtility(SettingsActivity.this).fileEncryptPasswordDialog(mContext);
//                }
//                else
//                {
//                    sharedPrefs.setStringValue(Constants.SECURITY_QUES_TYPE, Constants.SET_PASS_QUES);
//                     redirectToActivity(LockerPasswordActivity.class);
//                }
            }
        });



        //makeConnectioBillingClient();
        //queryPurchases();
        setSelectedValueInField();






    }


    //billing code 15-02-23
   /* private void handlePurchase(Purchase purchase) {
        if (purchase.getSkus().equals(Constants.ITEM_SKU_ADREMOVAL)) {
            mPrefs.setBoolanValue(Constants.pref_remove_ads,true);
            // setAdFree(true);
        }
    }*/

   /* @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {


        //Handle the responseCode for the purchase
        //If response code is OK,  handle the purchase
        //If user already owns the item, then indicate in the shared prefs that item is owned
        //If cancelled/other code, log the error

        if (responseCode == BillingClient.BillingResponse.OK
                && purchases != null) {
            for (Purchase purchase : purchases) {
                handlePurchase(purchase);
            }
        } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            Log.d("billing status", "User Canceled" + responseCode);
        } else if (responseCode == BillingClient.BillingResponse.ITEM_ALREADY_OWNED) {
            mPrefs.setBoolanValue(Constants.pref_remove_ads,true);
            //setAdFree(true);
            //layout_remove_adds.setVisibility(View.GONE);
        } else {
            Log.d("billing status", "Other code" + responseCode);
            // Handle any other error codes.
        }
    }*/



    //Billing code
   /* @Override
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


    public void dispAddFreePaymentDiaog()
    {

        HashMap<String, SkuDetails> skuDetailsHashMap = new HashMap<>();
        List<String> skuList = new ArrayList<> ();
        skuList.add(Constants.ITEM_SKU_SUBSCIBE); // Testing id : android.test.purchased
        //skuList.add("developer.mojo_digi_filehunt_subscribe");

        SkuDetailsParams skuParams = SkuDetailsParams.newBuilder()
                .setType(BillingClient.SkuType.INAPP)
                .setSkusList(skuList)
                .build();
        mBillingClient.querySkuDetailsAsync(skuParams,
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList) {
                        // Process the result.
                        for (SkuDetails skuDetails : skuDetailsList) {
                            skuDetailsHashMap.put(skuDetails.getSku(), skuDetails);
                        }
                    }
                });

        BillingFlowParams mBillingFlowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(skuDetailsHashMap.get(Constants.ITEM_SKU_ADREMOVAL)).build();

        mBillingClient.launchBillingFlow(this, mBillingFlowParams);
    }

    *//*billing code part *//*

    public void makeConnectioBillingClient()
    {
        *//*in app billing code*//*
        // Establish connection to billing client

        mBillingClient = BillingClient.newBuilder(getApplicationContext())
                .enablePendingPurchases()
                .setListener(this)
                .build();
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                int billingResponseCode = billingResult.getResponseCode();
                Log.d("TAG", "onBillingSetupFinished");
                if (billingResponseCode == BillingClient.BillingResponseCode.OK)
                {

                  //  getPurchasedItems();
                    //getSKUDetails();
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
               // startConnection();
                Log.d("TAG", "onBillingServiceDisconnected");
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

    }*/



    private void setSelectedValueInField() {

        if(mContext !=null && mPrefs !=null)
        {
            boolean hideExtPrefValue= mPrefs.getBoolanValue(AddConstants.KEY_HIDE_EXTERNAL_STORAGE, false);
            boolean showHiddenFile= mPrefs.getBoolanValue(AddConstants.KEY_SHOW_HIDDEN_FILE, false);
            boolean showSmallFile= mPrefs.getBoolanValue(AddConstants.KEY_DISPLAY_SMALL_FILE, false);
            int  txtSizeIndex= mPrefs.getIntValue(AddConstants.KEY_TEXT_SIZE_INDEX, 0);


            switchHideStorage.setChecked(hideExtPrefValue);
            switchHiddenfile.setChecked(showHiddenFile);
            switchSmallFile.setChecked(showSmallFile);

            //txtSize_Spinner.setSelection(txtSize);

            txtSize_Spinner.setSelection(txtSizeIndex);
            Log.d("fontSize  -index", ""+txtSizeIndex);



            mPrefs.setValue(AddConstants.KEY_TEXT_SIZE, spinnerArray.get(txtSizeIndex));

            // hold the the size of text  to  be applied on  txt of the app;
            int txtSize= mPrefs.getIntValue(AddConstants.KEY_TEXT_SIZE, 10);
            Log.d("fontSize- size", ""+txtSize);

        }
    }

    private  void setTextViewFontSize()
    {
        txtDispSmallFile.setTextSize(Utility.getFontSizeValueHeading(mContext));
        txtHideexternal.setTextSize(Utility.getFontSizeValueHeading(mContext));
        txtShowHiddenFiles.setTextSize(Utility.getFontSizeValueHeading(mContext));
        txtTextSize.setTextSize(Utility.getFontSizeValueHeading(mContext));
        //txtRemoveAdds.setTextSize(Utility.getFontSizeValueHeading(mContext));
        txtUpdateSecurityQues.setTextSize(Utility.getFontSizeValueHeading(mContext));

    }

    @Override
    public void onCancelClick() {
        //Utility.dispToast(mContext,"");
    }

    @Override
    public int onEncryptClick(String password) {
        String s  =  Utility.readPasswordFile();
        if(s.equals(password))
        {
            mPrefs.setStringValue(Constants.SECURITY_QUES_TYPE, Constants.UPDATE_PASS_QUES);
            redirectToActivity(SecurityQuesActivity.class);
            return 1;
        }
        else {
            Utility.dispToast(mContext,getResources().getString(R.string.passwordnotmatch));
            return  0;
        }
    }

    @Override
    public void onForgotClick(Class targetClass) {
        mPrefs.setStringValue(Constants.SECURITY_QUES_TYPE, Constants.FORGOT_PASS_QUES);
        redirectToActivity(targetClass);
    }


    private void redirectToActivity(Class targetClass)
    {
        Intent i =  new Intent(SettingsActivity.this, targetClass);
        startActivity(i);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();

        int abc=(Integer) txtSize_Spinner.getSelectedItem();
        System.out.print(""+abc);
        mPrefs.setValue(AddConstants.KEY_TEXT_SIZE, (Integer) txtSize_Spinner.getSelectedItem());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}

package com.mojodigitech.filehunt.AddsUtility;

/*public class AddMobUtils extends Activity {
    private String ADDLOGTAG = "BANNER_ADD_LOGTAG";
    SharedPreferenceUtil addPref;
    RewardedVideoAd mRewardedVideoAd;

    private String ADDLOGTAG_INTERESRT = "INTERESTIAL_ADD_LOGTAG";
    private String ADDLOGTAG_VIDEO = "VIDEO_ADD_LOGTAG";

    public AddMobUtils() {

    }

    public void displayLocalBannerAdd(final AdView mAdView) {
        // will get Addis from xml addview

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        AdSettings.addTestDevice("33BE2250B43518CCDA7DE426D04EE231");

                // Check the LogCat to get your test device ID

                // .addTestDevice("33BE2250B43518CCDA7DE426D04EE231")





        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Toast.makeText(getApplicationContext(), "Ad loaded!", Toast.LENGTH_SHORT).show();
                Log.d(ADDLOGTAG, "Add is Loaded");
                mAdView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdClosed() {
                // Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
                Log.d(ADDLOGTAG, "Ad is closed!");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                //Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
                Log.d(ADDLOGTAG, "" + "Ad failed to load! error code: " + errorCode);
            }

            @Override
            public void onAdLeftApplication() {
                // Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
                Log.d(ADDLOGTAG, "Ad left application!");
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });

        mAdView.loadAd(adRequest);

        //return  mAdView;
    }


    public void displayServerBannerAdd(SharedPreferenceUtil addPref, View adContainer, Context mContext) {

        // will  get  Adds from server
        String bannerAddid = addPref.getStringValue(AddConstants.BANNER_ADD_ID, "notfound");
        boolean showAdd = addPref.getBoolanValue(AddConstants.SHOW_ADD, false);

        if (!bannerAddid.equalsIgnoreCase("notfound") && showAdd) {
            final AdView mAdView = new AdView(mContext);
            mAdView.setAdSize(AdSize.SMART_BANNER);
            mAdView.setAdUnitId(bannerAddid);
            ((RelativeLayout) adContainer).addView(mAdView);


            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    // Check the LogCat to get your test device ID
                    .addTestDevice("33BE2250B43518CCDA7DE426D04EE231")
                    .build();

            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    // Toast.makeText(getApplicationContext(), "Ad loaded!", Toast.LENGTH_SHORT).show();
                    Log.d(ADDLOGTAG, "Add is Loaded");
                    mAdView.setVisibility(View.VISIBLE);


                }

                @Override
                public void onAdClosed() {
                    // Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
                    Log.d(ADDLOGTAG, "Ad is closed!");
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    //Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
                    Log.d(ADDLOGTAG, "" + "Ad failed to load! error code: " + errorCode);
                }

                @Override
                public void onAdLeftApplication() {
                    // Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
                    Log.d(ADDLOGTAG, "Ad left application!");
                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();
                }
            });

            mAdView.loadAd(adRequest);
        }


    }


    private InterstitialAd interstitialAd;

    // in case  local adds send addUnitId null
    public void showInterstitial(SharedPreferenceUtil addPref, final Context ctx, String addUnitId) {
        boolean showAdd = addPref.getBoolanValue(AddConstants.SHOW_ADD, false);
        if (showAdd) {
            interstitialAd = new InterstitialAd(ctx);

            // set the ad unit ID
            if (addUnitId != null)
                interstitialAd.setAdUnitId(addUnitId);
            else
                interstitialAd.setAdUnitId(ctx.getResources().getString(R.string.ad_unit_id));

            AdRequest adRequest = new AdRequest.Builder().addTestDevice(Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID)).build();
            interstitialAd.loadAd(adRequest);

            // Load ads into Interstitial Ads
            interstitialAd.loadAd(adRequest);


            interstitialAd.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    showInterstitial();
                }
            });
        }

    }

    private void showInterstitial() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
            Log.d(ADDLOGTAG_INTERESRT, "interestial Add Loaded");
        } else {
            Log.d(ADDLOGTAG_INTERESRT, "interestial add did not load");

        }
    }

    public RewardedVideoAd displayRewaredVideoAdd(SharedPreferenceUtil addPref, final Context ctx, String addUnitId) {

        boolean showAdd = addPref.getBoolanValue(AddConstants.SHOW_ADD, false);
        if (showAdd) {
            if (addUnitId != null)
                MobileAds.initialize(this, ctx.getString(R.string.admob_app_id));
            else
                MobileAds.initialize(this, addUnitId);

            mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(ctx);
            if (addUnitId != null) {
                mRewardedVideoAd.loadAd(ctx.getString(R.string.ad_unit_id_reward), new AdRequest.Builder().addTestDevice(Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID))
                        //.addTestDevice("1A4C168E6A1C14BCF1F65DA2AC9E8C43")
                        .build());
            } else {

                mRewardedVideoAd.loadAd(ctx.getString(R.string.ad_unit_id_reward), new AdRequest.Builder().addTestDevice(Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID))
                        //.addTestDevice("1A4C168E6A1C14BCF1F65DA2AC9E8C43")
                        .build());
            }


            mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {

                @Override
                public void onRewarded(RewardItem rewardItem) {
                    //Toast.makeText(RewardedVideoAdActivity.this, "onRewarded! currency: " + rewardItem.getType() + "  amount: " +
                    // rewardItem.getAmount(), Toast.LENGTH_SHORT).show();

                    Log.d(ADDLOGTAG_VIDEO, "onRewarded Executed");
                }

                @Override
                public void onRewardedVideoAdLeftApplication() {

                    Log.d(ADDLOGTAG_VIDEO, "onRewardedVideoAdLeftApplication");
                }

                @Override
                public void onRewardedVideoAdClosed() {


                    Log.d(ADDLOGTAG_VIDEO, "onRewardedVideoAdClosed");
                }

                @Override
                public void onRewardedVideoAdFailedToLoad(int errorCode) {

                    Log.d(ADDLOGTAG_VIDEO, "onRewardedVideoAdFailedToLoad " + errorCode);
                    //mRewardedVideoAd.loadAd(ctx.getString(R.string.ad_unit_id_reward), new AdRequest.Builder().addTestDevice(Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID)).addTestDevice("1A4C168E6A1C14BCF1F65DA2AC9E8C43").build());
                }

                @Override
                public void onRewardedVideoCompleted() {
                    Log.d(ADDLOGTAG_VIDEO, "onRewardedCompleted");
                }

                @Override
                public void onRewardedVideoAdLoaded() {

                    Log.d(ADDLOGTAG_VIDEO, "onRewardedVideoAdLoaded");

                    //  Toast.makeText(ctx, "video add  loaded", Toast.LENGTH_SHORT).show();
                    if (mRewardedVideoAd.isLoaded()) {
                        mRewardedVideoAd.show();
                    }

                }

                @Override
                public void onRewardedVideoAdOpened() {
                    Log.d(ADDLOGTAG_VIDEO, "onRewardedVideoAdOpened");
                }

                @Override
                public void onRewardedVideoStarted() {
                    Log.d(ADDLOGTAG_VIDEO, "onRewardedVideoStarted");
                }
            });
        }
        // loadRewardedVideoAd(ctx,mRewardedVideoAd);

        return mRewardedVideoAd;

    }

    public void dispFacebookBannerAdd(Context mContext, SharedPreferenceUtil addPref,Activity activity) {
        String bannerAddid = addPref.getStringValue(AddConstants.BANNER_ADD_ID, AddConstants.NOT_FOUND);
        boolean showAdd = addPref.getBoolanValue(AddConstants.SHOW_ADD, false);

         boolean isAddFree=addPref.getBoolanValue(Constants.pref_remove_ads, false);
        try {

            if (!bannerAddid.equalsIgnoreCase(AddConstants.NOT_FOUND) && showAdd && !isAddFree) {
                com.facebook.ads.AdView fbAdView = new com.facebook.ads.AdView(mContext, bannerAddid, com.facebook.ads.AdSize.BANNER_HEIGHT_50);

                final LinearLayout adContainer = activity.findViewById(R.id.banner_container);

                adContainer.addView(fbAdView);

                fbAdView.setAdListener(new com.facebook.ads.AdListener() {
                    @Override
                    public void onError(Ad ad, AdError adError) {
                        // Ad error callback

                        Log.d("Fberror", "" + adError.getErrorMessage());
                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
                        // Ad loaded callback
                        adContainer.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAdClicked(Ad ad) {
                        // Ad clicked callback
                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {
                        // Ad impression logged callback
                    }
                });

                // Request an ad
                fbAdView.loadAd();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void dispFacebookInterestialAdds(Context mContext,SharedPreferenceUtil addPref)
    {
        String interestialAddid = addPref.getStringValue(AddConstants.INTERESTIAL_ADD_ID, AddConstants.NOT_FOUND);
        boolean showAdd = addPref.getBoolanValue(AddConstants.SHOW_ADD, false);
        boolean isAddFree=addPref.getBoolanValue(Constants.pref_remove_ads, false);
        try {
        if(!interestialAddid.equalsIgnoreCase(AddConstants.NOT_FOUND) && showAdd && !isAddFree) {
            final String TAG = AddMobUtils.class.getSimpleName();
            final com.facebook.ads.InterstitialAd interstitialAd = new com.facebook.ads.InterstitialAd(mContext, interestialAddid);

            // Set listeners for the Interstitial Ad
            interstitialAd.setAdListener(new com.facebook.ads.InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {
                    // Interstitial ad displayed callback
                    Log.e(TAG, "Interstitial ad displayed.");
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    // Interstitial dismissed callback
                    Log.e(TAG, "Interstitial ad dismissed.");
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    // Ad error callback
                    Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    // Interstitial ad is loaded and ready to be displayed
                    Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                    // Show the ad
                    interstitialAd.show();

                    //showAdWithDelay();
                }

                @Override
                public void onAdClicked(Ad ad) {
                    // Ad clicked callback
                    Log.d(TAG, "Interstitial ad clicked!");
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    // Ad impression logged callback
                    Log.d(TAG, "Interstitial ad impression logged!");
                }
            });

            // For auto play video ads, it's recommended to load the ad
            // at least 30 seconds before it is shown
            interstitialAd.loadAd();
        }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }
}*/

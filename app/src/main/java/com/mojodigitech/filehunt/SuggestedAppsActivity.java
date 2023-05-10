package com.mojodigitech.filehunt;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mojodigitech.filehunt.Utils.Utility;

public class SuggestedAppsActivity extends AppCompatActivity {

    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
           mContext=this;

        setContentView(R.layout.activity_suggest_apps);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Utility.setstatusBarColorBelowM(SuggestedAppsActivity.this);
        }
        Utility.setActivityTitle2(mContext,getResources().getString(R.string.txtsuggestedApps));


        RelativeLayout screenlockLayout=findViewById(R.id.app1Layout);
        RelativeLayout videoPlayer=findViewById(R.id.app2Layout);
        RelativeLayout khulasaLite=findViewById(R.id.app3Layout);
        RelativeLayout selfiePro=findViewById(R.id.app4Layout);
        RelativeLayout ninjafox=findViewById(R.id.app5Layout);
        RelativeLayout scanner=findViewById(R.id.app6Layout);
        RelativeLayout khulasa=findViewById(R.id.app7Layout);

        TextView txtApp1=findViewById(R.id.txtApp1);
        TextView txtApp2=findViewById(R.id.txtApp2);
        TextView txtApp3=findViewById(R.id.txtApp3);
        TextView txtApp4=findViewById(R.id.txtApp4);
        TextView txtApp5=findViewById(R.id.txtApp5);
        TextView txtApp6=findViewById(R.id.txtApp6);
        TextView txtApp7=findViewById(R.id.txtApp7);


        txtApp1.setTypeface( Utility.typeFace_adobe_caslonpro_Regular(mContext));
        txtApp2.setTypeface( Utility.typeFace_adobe_caslonpro_Regular(mContext));
        txtApp3.setTypeface( Utility.typeFace_adobe_caslonpro_Regular(mContext));
        txtApp4.setTypeface( Utility.typeFace_adobe_caslonpro_Regular(mContext));
        txtApp5.setTypeface( Utility.typeFace_adobe_caslonpro_Regular(mContext));
        txtApp6.setTypeface( Utility.typeFace_adobe_caslonpro_Regular(mContext));
        txtApp7.setTypeface( Utility.typeFace_adobe_caslonpro_Regular(mContext));






        screenlockLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String appPackageName = "com.mojodigi.screenlock"; // package name of the app
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });


        videoPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String appPackageName = "com.mojodigi.videoplayer"; // package name of the app
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });




        khulasaLite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String appPackageName = "com.mojodigi.khulasaNewsLite"; // package name of the app
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        selfiePro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String appPackageName = "com.mojodigi.selfiepro"; // package name of the app
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });



        ninjafox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String appPackageName = "com.mojodigi.ninjafox"; // package name of the app
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String appPackageName = "com.mojodigi.smartcamscanner"; // package name of the app
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
 khulasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String appPackageName = "com.mojodigi.khulasanews"; // package name of the app
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });






    }
}

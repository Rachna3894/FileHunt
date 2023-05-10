package com.mojodigitech.filehunt;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mojodigitech.filehunt.AddsUtility.SharedPreferenceUtil;
import com.mojodigitech.filehunt.Class.Constants;
import com.mojodigitech.filehunt.Utils.Utility;

import java.io.File;

public class LockerPasswordActivity extends AppCompatActivity {

    private EditText passwordTxt, cpasswordTxt  ;
    private Button submit  ;
    private Context mContext;

    private SharedPreferenceUtil addprefs;
    private String securityQuesType ="" ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_locker_password);

        mContext = LockerPasswordActivity.this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Utility.setstatusBarColorBelowM(LockerPasswordActivity.this);
        }

        if(addprefs==null) {
            addprefs = new SharedPreferenceUtil(mContext);
        }
        securityQuesType = addprefs.getStringValue(Constants.SECURITY_QUES_TYPE, "");

        submit=findViewById(R.id.done_button);
        passwordTxt=findViewById(R.id.password);
        cpasswordTxt=findViewById(R.id.cpassword);


        submit.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(mContext));
        passwordTxt.setTextSize(Utility.getFontSizeValueHeading(mContext));
        cpasswordTxt.setTextSize(Utility.getFontSizeValueHeading(mContext));
        submit.setTextSize(Utility.getFontSizeValueHeading(mContext));


        Utility.setActivityTitle2(LockerPasswordActivity.this,getResources().getString(R.string.setpassword));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordTxt.getText().toString().length()>=1 && cpasswordTxt.getText().toString().length()>=1) {
                    if (passwordTxt.getText().toString().equalsIgnoreCase(cpasswordTxt.getText().toString())) {

                        if (securityQuesType.equalsIgnoreCase(Constants.SET_PASS_QUES)) {
                            addprefs.setStringValue(Constants.SECURITY_QUES_TYPE, Constants.SET_PASS_QUES);

                           String s  =  Utility.readPasswordFile();
                           if(!s.isEmpty()){
                                   redirectToActivity(SecurityQuesActivity.class);
                                    finish();
                           }
                           else {
                               File dir = new File(Environment.getExternalStorageDirectory()+ "/" + Constants.passDir);
                               deleteNon_EmptyDir(dir);
                               int status = Utility.createPasswordFile(LockerPasswordActivity.this, passwordTxt.getText().toString());
                               if (status == 1)
                                   redirectToActivity(SecurityQuesActivity.class);
                               finish();
                           }

//                            addprefs.setStringValue(Constants.TEMPORARY_PASS, passwordTxt.getText().toString());
//                            redirectToActivity(SecurityQuesActivity.class);
//                            finish();


                        }
                        else if (securityQuesType.equalsIgnoreCase(Constants.FORGOT_PASS_QUES)) {

                            addprefs.setStringValue(Constants.SECURITY_QUES_TYPE, Constants.FORGOT_PASS_QUES);

                            File dir = new File(Environment.getExternalStorageDirectory()+ "/" + Constants.passDir);
                            deleteNon_EmptyDir(dir);
                            int status = Utility.createPasswordFile(LockerPasswordActivity.this, passwordTxt.getText().toString());
                            if (status == 1)
                                redirectToActivity(LockerActivityMain.class);
                                finish();
                        }
                        else if (securityQuesType.equalsIgnoreCase(Constants.UPDATE_PASS_QUES)) {
                            addprefs.setStringValue(Constants.SECURITY_QUES_TYPE, Constants.UPDATE_PASS_QUES);
                            //addprefs.setStringValue(Constants.TEMPORARY_PASS, passwordTxt.getText().toString());
                            redirectToActivity(SecurityQuesActivity.class);

                            finish();
                        }


                    } else {
                        Utility.dispToast(LockerPasswordActivity.this, getResources().getString(R.string.passwordnotmatch));
                    }
                }
                else
                {
                    Utility.dispToast(LockerPasswordActivity.this,getResources().getString(R.string.type_password));
                }
            }
        });



    }

    private void redirectToActivity(Class targetClass)
    {
        Intent i =  new Intent(LockerPasswordActivity.this, targetClass);
        startActivity(i);
    }

    public  boolean deleteNon_EmptyDir(File dir) {
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteNon_EmptyDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }



}

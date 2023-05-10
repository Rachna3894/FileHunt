package com.mojodigitech.filehunt;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mojodigitech.filehunt.AddsUtility.SharedPreferenceUtil;
import com.mojodigitech.filehunt.Class.Constants;
import com.mojodigitech.filehunt.Utils.Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SecurityQuesActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private EditText question1Edit, question2Edit, question3Edit ;

    private ArrayList<String> getSavedArrayList  = null;
    private ArrayList<String> firstSetQuesArrayList = null;
    private ArrayList<String> quesArrayList = null;
    private Set<String> setPassSecurityQuesSet = null;
    private ArrayList<String> forgotQuesArrayList = null;
    private ArrayList<String> forgotCurrectQuesList = null;
    private ArrayList<String> updateQuesArrayList = null;
    private Set<String> updateSecurityQuesSet = null;
    private ArrayList<String> forgotMatchedQuesArrayList = null;

    private Button save_ques_button ;

    private TextView question1Text , question2Text , question3Text , questionNoteText;

    private SharedPreferenceUtil addprefs;

    private LinearLayout setPassQuesLayout ;

    private String securityQuesType ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_ques);

        mContext = SecurityQuesActivity.this;

        if(addprefs==null) {
            addprefs = new SharedPreferenceUtil(mContext);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Utility.setstatusBarColorBelowM(SecurityQuesActivity.this);
        }





        securityQuesType = addprefs.getStringValue(Constants.SECURITY_QUES_TYPE, "");
        if(securityQuesType.equalsIgnoreCase(Constants.SET_PASS_QUES)){
            Utility.setActivityTitle2(SecurityQuesActivity.this, getResources().getString(R.string.set_security_ques_title));
        }else if(securityQuesType.equalsIgnoreCase(Constants.UPDATE_PASS_QUES)){
            Utility.setActivityTitle2(SecurityQuesActivity.this, getResources().getString(R.string.update_security_ques_title));
        }else if(securityQuesType.equalsIgnoreCase(Constants.FORGOT_PASS_QUES)){
            Utility.setActivityTitle2(SecurityQuesActivity.this, getResources().getString(R.string.forgot_security_ques_title));
        }else {
            Utility.setActivityTitle2(SecurityQuesActivity.this,getResources().getString(R.string.security_ques_title));
        }

        //Utility.dispToast(mContext, securityQuesType);


        setPassQuesLayout=findViewById(R.id.setPassQuesLayout);
        question1Text=findViewById(R.id.question1Text);
        question2Text=findViewById(R.id.question2Text);
        question3Text=findViewById(R.id.question3Text);
        questionNoteText=findViewById(R.id.questionNoteText);

        question1Text.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(mContext));
        question2Text.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(mContext));
        question3Text.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(mContext));
        questionNoteText.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(mContext));

        question1Edit=findViewById(R.id.question1Edit);
        question2Edit=findViewById(R.id.question2Edit);
        question3Edit=findViewById(R.id.question3Edit);
        save_ques_button=findViewById(R.id.save_ques_button);
        save_ques_button.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(mContext));
        save_ques_button.setOnClickListener(this);

        if(securityQuesType.equalsIgnoreCase(Constants.SET_PASS_QUES)){
            question1Edit.setHint(getResources().getString(R.string.set_security_ques_hint));
            question2Edit.setHint(getResources().getString(R.string.set_security_ques_hint));
            question3Edit.setHint(getResources().getString(R.string.set_security_ques_hint));
        }else if(securityQuesType.equalsIgnoreCase(Constants.UPDATE_PASS_QUES)){
            question1Edit.setHint(getResources().getString(R.string.update_security_ques_hint));
            question2Edit.setHint(getResources().getString(R.string.update_security_ques_hint));
            question3Edit.setHint(getResources().getString(R.string.update_security_ques_hint));
        }else if(securityQuesType.equalsIgnoreCase(Constants.FORGOT_PASS_QUES)){
            question1Edit.setHint(getResources().getString(R.string.forgot_security_ques_hint));
            question2Edit.setHint(getResources().getString(R.string.forgot_security_ques_hint));
            question3Edit.setHint(getResources().getString(R.string.forgot_security_ques_hint));
        }else {
            question1Edit.setHint(getResources().getString(R.string.security_ques_hint));
            question2Edit.setHint(getResources().getString(R.string.security_ques_hint));
            question3Edit.setHint(getResources().getString(R.string.security_ques_hint));
        }

        if(securityQuesType.equalsIgnoreCase(Constants.UPDATE_PASS_QUES)) {

            String  schoolSavedQues = Utility.readQuestionsFile(Constants.schoolDir,  Constants.schoolQuesFileDes);
            String  shoesSavedQues = Utility.readQuestionsFile(Constants.shoesDir,  Constants.shoesQuesFileDes);
            String  sportSavedQues = Utility.readQuestionsFile(Constants.sportDir,  Constants.sportQuesFileDes);
            if(!schoolSavedQues.isEmpty())
                question1Edit.setText(schoolSavedQues);
            if(!shoesSavedQues.isEmpty())
                question2Edit.setText(shoesSavedQues);
            if(!sportSavedQues.isEmpty())
                question3Edit.setText(sportSavedQues);

        }else {
            question1Edit.setText("");
            question2Edit.setText("");
            question3Edit.setText("");
        }




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.save_ques_button:

                String question1Text = question1Edit.getText().toString().trim();
                String question2Text = question2Edit.getText().toString().trim();
                String question3Text = question3Edit.getText().toString().trim();

                if(securityQuesType.equalsIgnoreCase(Constants.SET_PASS_QUES)) {
                    addprefs.setStringValue(Constants.SECURITY_QUES_TYPE, Constants.SET_PASS_QUES);

                    firstSetQuesArrayList = new ArrayList<>();
                    quesArrayList = new ArrayList<>();
                    setPassSecurityQuesSet = new HashSet<>();

                    if (question1Text.isEmpty() || question1Text.equalsIgnoreCase("")) {
                        Utility.dispToast(mContext, "Please Enter Your First School.");
                        return;
                    }
                    if (question2Text.isEmpty() || question2Text.equalsIgnoreCase("")) {
                        Utility.dispToast(mContext, "Please Enter Your Shoes Size.");
                        return;
                    }
                    if (question3Text.isEmpty() || question3Text.equalsIgnoreCase("")) {
                        Utility.dispToast(mContext, "Please Enter Your Favourite Sport.");
                        return;
                    }


                    File quesDir = new File(Environment.getExternalStorageDirectory()+ "/" + Constants.schoolDir);
                    deleteNon_EmptyDir(quesDir);
                    Utility.writeQuestionsFile(mContext, question1Text, Constants.schoolDir,  Constants.schoolQuesFile, Constants.schoolQuesFileDes);
                    //Utility.dispToast(mContext, Utility.readQuestionsFile( Constants.schoolDir,  Constants.schoolQuesFileDes));

                    File shoesDir = new File(Environment.getExternalStorageDirectory()+ "/" + Constants.shoesDir);
                    deleteNon_EmptyDir(shoesDir);
                    Utility.writeQuestionsFile(mContext, question2Text ,Constants.shoesDir,  Constants.shoesQuesFile, Constants.shoesQuesFileDes);
                    //Utility.dispToast(mContext, Utility.readQuestionsFile(Constants.shoesDir, Constants.shoesQuesFileDes));

                    File sportDir = new File(Environment.getExternalStorageDirectory()+ "/" + Constants.sportDir);
                    deleteNon_EmptyDir(sportDir);
                    Utility.writeQuestionsFile(mContext, question3Text , Constants.sportDir, Constants.sportQuesFile, Constants.sportQuesFileDes);

                    redirectToActivity(LockerActivityMain.class);

                    SecurityQuesActivity.this.finish();
                }

                else if(securityQuesType.equalsIgnoreCase(Constants.FORGOT_PASS_QUES)) {

                    addprefs.setStringValue(Constants.SECURITY_QUES_TYPE, Constants.FORGOT_PASS_QUES);

                    //getSavedArrayList =  getSavedArrayList();
                    getSavedArrayList =  new ArrayList<>();
                    forgotQuesArrayList = new ArrayList<>();
                    forgotCurrectQuesList = new ArrayList<>();
                    forgotMatchedQuesArrayList = new ArrayList<>();

                    if (question1Text.length() >= 1)
                        forgotQuesArrayList.add(question1Text);
                    if (question2Text.length() >= 1)
                        forgotQuesArrayList.add(question2Text);
                    if (question3Text.length() >= 1)
                        forgotQuesArrayList.add(question3Text);

                    if (forgotQuesArrayList.size()<2) {
                        Utility.dispToast(mContext, "Please Enter At least 2 Answers.");
                        return;
                    }

                    String  schoolSavedQues = Utility.readQuestionsFile(Constants.schoolDir,  Constants.schoolQuesFileDes);
                    String  shoesSavedQues = Utility.readQuestionsFile(Constants.shoesDir,  Constants.shoesQuesFileDes);
                    String  sportSavedQues = Utility.readQuestionsFile(Constants.sportDir,  Constants.sportQuesFileDes);



                    if (question1Text.length() >= 1 && schoolSavedQues.equalsIgnoreCase(question1Text)) {
                        forgotCurrectQuesList.add(question1Text);
                    }
                    if (question2Text.length() >= 1 && shoesSavedQues.equalsIgnoreCase(question2Text)) {
                        forgotCurrectQuesList.add(question2Text);
                    }
                    if (question3Text.length() >= 1 && sportSavedQues.equalsIgnoreCase(question3Text)) {
                        forgotCurrectQuesList.add(question3Text);
                    }

                    if(forgotCurrectQuesList.size()<2){
                        Utility.dispToast(mContext, "Please Enter At least 2 Correct Answers.");
                        return;
                    }


                    if(!schoolSavedQues.isEmpty() && schoolSavedQues.length() > 0)
                        getSavedArrayList.add(schoolSavedQues);
                    if(!shoesSavedQues.isEmpty()&& shoesSavedQues.length() > 0)
                        getSavedArrayList.add(shoesSavedQues);
                    if(!sportSavedQues.isEmpty()&& sportSavedQues.length() > 0)
                        getSavedArrayList.add(sportSavedQues);


                    if (getSavedArrayList.contains(question1Text)){
                        forgotMatchedQuesArrayList.add(question1Text);
                    }
                    if (getSavedArrayList.contains(question2Text)){
                        forgotMatchedQuesArrayList.add(question2Text);
                    }
                    if (getSavedArrayList.contains(question3Text)){
                        forgotMatchedQuesArrayList.add(question3Text);
                    }

                    //Utility.dispToast(mContext, getSavedArrayList+" "+forgotMatchedQuesArrayList);

                    if(forgotMatchedQuesArrayList.size()>=2){
                        redirectToActivity(LockerPasswordActivity.class);
                        SecurityQuesActivity.this.finish();
                    }
                    else {
                        Utility.dispToast(mContext, "Please Enter At least 2 Correct Answers for Reset Password.");
                    }
                }


                else if(securityQuesType.equalsIgnoreCase(Constants.UPDATE_PASS_QUES)) {

                    addprefs.setStringValue(Constants.SECURITY_QUES_TYPE, Constants.UPDATE_PASS_QUES);

                    updateQuesArrayList = new ArrayList<>();
                    updateSecurityQuesSet = new HashSet<>();

                    if (question1Text.isEmpty() || question1Text.equalsIgnoreCase("")) {
                        Utility.dispToast(mContext, "Please Enter Your First School.");
                        return;
                    }
                    if (question2Text.isEmpty() || question2Text.equalsIgnoreCase("")) {
                        Utility.dispToast(mContext, "Please Enter Your Shoes Size.");
                        return;
                    }
                    if (question3Text.isEmpty() || question3Text.equalsIgnoreCase("")) {
                        Utility.dispToast(mContext, "Please Enter Your Favourite Sport.");
                        return;
                    }


                    File quesDir = new File(Environment.getExternalStorageDirectory()+ "/" + Constants.schoolDir);
                    deleteNon_EmptyDir(quesDir);
                    Utility.writeQuestionsFile(mContext, question1Text, Constants.schoolDir,  Constants.schoolQuesFile, Constants.schoolQuesFileDes);

                    File shoesDir = new File(Environment.getExternalStorageDirectory()+ "/" + Constants.shoesDir);
                    deleteNon_EmptyDir(shoesDir);
                    Utility.writeQuestionsFile(mContext, question2Text ,Constants.shoesDir,  Constants.shoesQuesFile, Constants.shoesQuesFileDes);

                    File sportDir = new File(Environment.getExternalStorageDirectory()+ "/" + Constants.sportDir);
                    deleteNon_EmptyDir(sportDir);
                    Utility.writeQuestionsFile(mContext, question3Text , Constants.sportDir, Constants.sportQuesFile, Constants.sportQuesFileDes);

                    //Utility.dispToast(mContext, "Security Questions Updated Successfully.");
                    SecurityQuesActivity.this.finish();

                }

                break;
        }
    }



    private void redirectToActivity(Class targetClass)
    {
        Intent i =  new Intent(SecurityQuesActivity.this,targetClass);
        startActivity(i);
    }

    private void saveArrayList(ArrayList<String> arrayList) {
        try {
            FileOutputStream fileOutputStream = openFileOutput(Constants.questionFile , Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
            out.writeObject(arrayList);
            out.close();
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private ArrayList<String> getSavedArrayList() {
        ArrayList<String> savedArrayList = null;

        try {
            FileInputStream inputStream = openFileInput(Constants.questionFile);
            ObjectInputStream in = new ObjectInputStream(inputStream);
            savedArrayList = (ArrayList<String>) in.readObject();
            in.close();
            inputStream.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return savedArrayList;
    }


    private ArrayList<String> getSetData(Set set)
    {
        ArrayList<String> listData =new ArrayList<>();
        if(!set.isEmpty())
        {
            for (Iterator<String> it = set.iterator(); it.hasNext(); ) {
                String setValue = it.next();
                listData.add(setValue);
            }

        }
        return listData;
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

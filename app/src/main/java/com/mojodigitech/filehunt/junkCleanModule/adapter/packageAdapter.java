package com.mojodigitech.filehunt.junkCleanModule.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mojodigitech.filehunt.R;
import com.mojodigitech.filehunt.Utils.Utility;
import com.mojodigitech.filehunt.junkCleanModule.model.packageModel;

import java.util.ArrayList;

public class packageAdapter extends RecyclerView.Adapter<packageAdapter.MyViewHolder> {

    private ArrayList<packageModel> appsList;
    Context mContext;
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView appLogo;
        TextView appName;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.appName = (TextView) itemView.findViewById(R.id.appName);
            this.appLogo = (ImageView) itemView.findViewById(R.id.app_Logo);

        }
    }

    public packageAdapter(Context mContext,ArrayList<packageModel> appsList) {
        this.appsList =appsList ;
        this.mContext=mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_boost_phone, parent, false);

        //view.setOnClickListener(MainActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView appNameText = holder.appName;
        ImageView appLogo = holder.appLogo;

        appNameText.setTypeface(Utility.typeFace_adobe_caslonpro_Regular(mContext));
        appNameText.setText(appsList.get(listPosition).getAppPackageName());
        appLogo.setImageDrawable(appsList.get(listPosition).getAppIcon());


    }

    @Override
    public int getItemCount() {
        return appsList.size();
    }
}
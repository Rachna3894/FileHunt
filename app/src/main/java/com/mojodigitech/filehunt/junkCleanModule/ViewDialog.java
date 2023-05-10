package com.mojodigitech.filehunt.junkCleanModule;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;

import com.mojodigitech.filehunt.R;

public class ViewDialog {

    Activity activity;
    Dialog dialog;

    public ViewDialog(Activity activity) {
        this.activity = activity;

        dialog  = new Dialog(activity,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_loading_layout);

       /* ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(gifImageView);
        Glide.with(activity)
                .load(R.drawable.loading)
                .placeholder(R.drawable.loading)
                .centerCrop()
                .crossFade()
                .into(imageViewTarget);*/

    }

    public void showDialog() {
       /* dialog  = new Dialog(activity,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_loading_layout);

        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(gifImageView);
        Glide.with(activity)
                .load(R.drawable.loading)
                .placeholder(R.drawable.loading)
                .centerCrop()
                .crossFade()
                .into(imageViewTarget);*/

        dialog.show();
    }


    public void hideDialog(){
        dialog.dismiss();

    }

    public boolean isShowing(){
        if (dialog.isShowing()){
            return true;
        }else {
            return false;
        }
    }


}

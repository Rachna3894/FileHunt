package com.mojodigitech.filehunt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mojodigitech.filehunt.AddsUtility.AddConstants;
import com.mojodigitech.filehunt.AddsUtility.SharedPreferenceUtil;

public class WebActivity extends AppCompatActivity {

    public static String TAG = "WebActivity";
    private Context mContext = null;

    private WebView webView;

    private SharedPreferenceUtil addprefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web);

        if(mContext==null) {
            mContext = WebActivity.this;
        }
        if(addprefs==null) {
            addprefs = new SharedPreferenceUtil(mContext);
        }

        initView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {


        webView = (WebView) findViewById(R.id.webView);

        if (AddConstants.NEWSURL != null) {
            webView.setVerticalScrollBarEnabled(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new myWebClient());
            webView.loadUrl(AddConstants.NEWSURL);

            AddConstants.NEWSURL = "";

        } else {
            Toast.makeText(mContext, getResources().getString(R.string.cannotload), Toast.LENGTH_SHORT).show();
        }

    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {


            return super.onRenderProcessGone(view, detail);

        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            //   CustomProgressDialog.dismiss();

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // CustomProgressDialog.dismiss();
            super.onPageFinished(view, url);
            Log.e("onpagefinish", ""+url);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            //CustomProgressDialog.show(WebviewActivity.this,"Loading");
            view.loadUrl(url);
            return true;

        }

    }


    @Override
    public void onBackPressed() {
        if(webView.canGoBack())
        {
            webView.goBack();
        }

        else
        {
            super.onBackPressed();
        }
    }
}

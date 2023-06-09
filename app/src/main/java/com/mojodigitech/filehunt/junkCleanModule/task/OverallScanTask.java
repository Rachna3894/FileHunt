package com.mojodigitech.filehunt.junkCleanModule.task;

import android.os.AsyncTask;
import android.os.Environment;

import com.mojodigitech.filehunt.junkCleanModule.callback.IScanCallback;
import com.mojodigitech.filehunt.junkCleanModule.model.JunkInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;


public class OverallScanTask extends AsyncTask<Void, Void, Void> {

    private IScanCallback mCallback;
    private final int SCAN_LEVEL = 4;
    private JunkInfo mApkInfo;
    private JunkInfo mLogInfo;
    private JunkInfo mTmpInfo;

    public OverallScanTask(IScanCallback callback) {
        mCallback = callback;
        mApkInfo = new JunkInfo();
        mLogInfo = new JunkInfo();
        mTmpInfo = new JunkInfo();
    }

    private void travelPath(File root, int level) {
        if (root == null || !root.exists() || level > SCAN_LEVEL) {
            return;
        }
        try{
            File[] lists = root.listFiles();
            for (File file : lists) {
                if (file.exists() && file.isFile()) {
                    String name = file.getName();
                    JunkInfo info = null;
                    if (name.endsWith(".apk")) {
                        info = new JunkInfo();
                        info.mSize = file.length();

                        info.name = name;
                        info.mPath = file.getAbsolutePath();
                        info.mIsChild = false;
                        info.mIsVisible = true;
                        mApkInfo.mChildren.add(info);
                        mApkInfo.mSize += info.mSize;
                    } else if (name.endsWith(".log")) {
                        info = new JunkInfo();
                        info.mSize = file.length();
                        info.name = name;
                        info.mPath = file.getAbsolutePath();
                        info.mIsChild = false;
                        info.mIsVisible = true;
                        mLogInfo.mChildren.add(info);
                        mLogInfo.mSize += info.mSize;
                    } else if (name.endsWith(".tmp") || name.endsWith(".temp")) {
                        info = new JunkInfo();
                        info.mSize = file.length();
                        info.name = name;
                        info.mPath = file.getAbsolutePath();
                        info.mIsChild = false;
                        info.mIsVisible = true;
                        mTmpInfo.mChildren.add(info);
                        mTmpInfo.mSize += info.mSize;
                    }

                    if (info != null) {
                        mCallback.onProgress(info);
                    }
                } else {
                    if (level < SCAN_LEVEL) {
                        try{
                            travelPath(file, level + 1);
                        }catch (NullPointerException npex){
                            npex.printStackTrace();
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }catch (NullPointerException npex){
            npex.printStackTrace();
        }catch (Exception ex){
            ex.printStackTrace();
        }


    }

    @Override
    protected Void doInBackground(Void... params) {

        mCallback.onBegin();

        File externalDir = Environment.getExternalStorageDirectory();
        if (externalDir != null) {
            try {
                travelPath(externalDir, 0);
            }catch (NullPointerException npex){
                npex.printStackTrace();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

        ArrayList<JunkInfo> list = new ArrayList<>();

        if (mApkInfo.mSize > 0L) {
            Collections.sort(mApkInfo.mChildren);
            Collections.reverse(mApkInfo.mChildren);
            list.add(mApkInfo);
        }

        if (mLogInfo.mSize > 0L) {
            Collections.sort(mLogInfo.mChildren);
            Collections.reverse(mLogInfo.mChildren);
            list.add(mLogInfo);
        }

        if (mTmpInfo.mSize > 0L) {
            Collections.sort(mTmpInfo.mChildren);
            Collections.reverse(mTmpInfo.mChildren);
            list.add(mTmpInfo);
        }

        mCallback.onFinish(list);

        return null;
    }
}

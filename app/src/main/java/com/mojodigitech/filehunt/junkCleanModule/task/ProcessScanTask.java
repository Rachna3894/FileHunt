package com.mojodigitech.filehunt.junkCleanModule.task;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import com.jaredrummler.android.processes.ProcessManager;
import com.jaredrummler.android.processes.models.AndroidAppProcess;
import com.jaredrummler.android.processes.models.Statm;
import com.mojodigitech.filehunt.Application.comScoreAnalytics;
import com.mojodigitech.filehunt.junkCleanModule.callback.IScanCallback;
import com.mojodigitech.filehunt.junkCleanModule.model.JunkInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProcessScanTask extends AsyncTask<Void, Void, Void> {

    private IScanCallback mCallback;

    public ProcessScanTask(IScanCallback mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    protected Void doInBackground(Void... params) {
        mCallback.onBegin();

        List<AndroidAppProcess> processes = ProcessManager.getRunningAppProcesses();

        ArrayList<JunkInfo> junks = new ArrayList<>();

        for (AndroidAppProcess process : processes) {
            JunkInfo info = new JunkInfo();
            info.mIsChild = false;
            info.mIsVisible = true;
            info.mPackageName = process.getPackageName();
            // get app icon here


            try {
                Statm statm = process.statm();
                info.mSize = statm.getResidentSetSize();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            try {
                PackageManager pm = comScoreAnalytics.getInstance().getPackageManager();
                PackageInfo packageInfo = process.getPackageInfo(comScoreAnalytics.getInstance(), 0);
                info.name = packageInfo.applicationInfo.loadLabel(pm).toString();
                 //info.mAppIcon = pm.getApplicationIcon(packageInfo.packageName);
                /* //info.mAppIcon = pm.getApplicationIcon(packageInfo.packageName);
                *
                *  this will  be used later in this project by uncommenting mAppIcon variable from  JunkInfo class
                * */
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                continue;
            }

            mCallback.onProgress(info);

            junks.add(info);
        }

        Collections.sort(junks);
        Collections.reverse(junks);
        mCallback.onFinish(junks);

        return null;
    }
}

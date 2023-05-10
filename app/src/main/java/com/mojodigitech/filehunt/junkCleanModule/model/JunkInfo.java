package com.mojodigitech.filehunt.junkCleanModule.model;


import com.mojodigitech.filehunt.Application.comScoreAnalytics;
import com.mojodigitech.filehunt.R;

import java.util.ArrayList;


public class JunkInfo implements Comparable<JunkInfo> {
    public String name;
    public long mSize;
    public String mPackageName;
    public String mPath;
    public ArrayList<JunkInfo> mChildren = new ArrayList<>();
    public boolean mIsVisible = false;
    public boolean mIsChild = true;

    //public Drawable mAppIcon;
    @Override
    public int compareTo(JunkInfo another) {
        String top = comScoreAnalytics.getInstance().getString(R.string.system_cache);

        if (this.name != null && this.name.equals(top)) {
            return 1;
        }

        if (another.name != null && another.name.equals(top)) {
            return -1;
        }

        if (this.mSize > another.mSize) {
            return 1;
        } else if (this.mSize < another.mSize) {
            return -1;
        } else {
            return 0;
        }
    }
}

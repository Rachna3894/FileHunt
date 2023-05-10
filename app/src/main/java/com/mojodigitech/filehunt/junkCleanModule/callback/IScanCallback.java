package com.mojodigitech.filehunt.junkCleanModule.callback;


import com.mojodigitech.filehunt.junkCleanModule.model.JunkInfo;

import java.util.ArrayList;


public interface IScanCallback {
    void onBegin();

    void onProgress(JunkInfo info);

    void onFinish(ArrayList<JunkInfo> children);
}

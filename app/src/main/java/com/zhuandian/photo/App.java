package com.zhuandian.photo;

import android.app.Application;

import cn.bmob.v3.Bmob;

/**
 * desc :
 * author：xiedong
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "3f3ca30e5235f37e0d420276aef4a330");
    }
}

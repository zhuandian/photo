package com.zhuandian.photo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.zhuandian.photo.utils.MyLocationListener;

public class MainActivity extends AppCompatActivity {
    private MyLocationListener myListener = new MyLocationListener();
    public LocationClient mLocationClient = null;
    private static final int BAIDU_READ_PHONE_STATE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLocation();
    }


    private void initLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, BAIDU_READ_PHONE_STATE);
            } else {
                initBaiduMap();
            }
        }else {
            initBaiduMap();
        }
        myListener.setLocationSuccess(new MyLocationListener.onLocationSuccess() {
            @Override
            public void onSuccess(String location) {
                Toast.makeText(MainActivity.this, String.format("根据我们的系统定位，您现在位于\n%s\n，系统根据您的地理位置，为您做出了相应的内容推荐，请您尽情享用...", location), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initBaiduMap() {
        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }
}

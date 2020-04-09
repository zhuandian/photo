package com.zhuandian.photo;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.MenuItem;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.zhuandian.base.BaseActivity;
import com.zhuandian.base.BaseFragment;
import com.zhuandian.photo.adapter.HomePageAdapter;
import com.zhuandian.photo.business.fragment.PhotoFragment;
import com.zhuandian.photo.business.fragment.HomeFragment;
import com.zhuandian.photo.business.fragment.MineFragment;
import com.zhuandian.photo.utils.MyLocationListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.vp_home)
    ViewPager vpHome;
    @BindView(R.id.tab_bottom)
    BottomNavigationView tabBottom;
    public static final int PAGE_HOME = 0;
    public static final int PAGE_PHOTO = 1;
    public static final int PAGE_MY = 2;

    private MyLocationListener myListener = new MyLocationListener();
    public LocationClient mLocationClient = null;
    private static final int BAIDU_READ_PHONE_STATE = 100;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void setUpView() {
        initLocation();

        List<BaseFragment> fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new PhotoFragment());
        fragmentList.add(new MineFragment());
        vpHome.setAdapter(new HomePageAdapter(getSupportFragmentManager(), fragmentList));
        vpHome.setOffscreenPageLimit(4);

        vpHome.setCurrentItem(PAGE_HOME);
        initBottomTab();
    }


    public void setCurrentPage(int position) {
        vpHome.setCurrentItem(position);
    }

    private void initBottomTab() {
        vpHome.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabBottom.getMenu().getItem(position).setChecked(true);
            }
        });

        tabBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.tab_home:
                        vpHome.setCurrentItem(PAGE_HOME);
                        break;
                    case R.id.tab_photo:
                        vpHome.setCurrentItem(PAGE_PHOTO);
                        break;
                    case R.id.tab_my:
                        vpHome.setCurrentItem(PAGE_MY);
                        break;
                }

                return true;
            }
        });
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
        } else {
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

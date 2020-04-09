package com.zhuandian.photo.business.fragment;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zhuandian.base.BaseFragment;
import com.zhuandian.photo.R;
import com.zhuandian.photo.adapter.PhotoAdapter;
import com.zhuandian.photo.business.activity.PhotoFilterActivity;
import com.zhuandian.photo.entity.PhotoEntity;
import com.zhuandian.photo.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * desc :
 * author：xiedong
 * date：2020/03/21
 */
public class HomeFragment extends BaseFragment {

    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    private List<PhotoEntity> mDatas = new ArrayList<>();
    private PhotoAdapter photoAdapter;
    List<String> images = new ArrayList<String>();

    //设置图片标题:自动对应
    List<String> titles = new ArrayList<String>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        photoAdapter = new PhotoAdapter(mDatas, actitity);
        rvList.setAdapter(photoAdapter);
        rvList.setLayoutManager(new LinearLayoutManager(actitity));
        initDataList();
    }


    private void initBanner() {
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(actitity).load((String) path).into(imageView);
            }
        });
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);//设置圆形指示器与标题
        banner.setIndicatorGravity(BannerConfig.RIGHT);//设置指示器位置
        banner.setDelayTime(3000);//设置轮播时间
        banner.setImages(images);//设置图片源
        banner.setBannerTitles(titles);//设置标题源
        banner.start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                //Banner 点击事件自己处理
            }
        });
    }

    private void initDataList() {
        BmobQuery<PhotoEntity> query = new BmobQuery<>();
//        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.addWhereEqualTo("photoUserId", BmobUser.getCurrentUser(UserEntity.class).getObjectId());
        query.setLimit(9);
        query.findObjects(new FindListener<PhotoEntity>() {
            @Override
            public void done(List<PhotoEntity> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            mDatas.add(list.get(i));
                            if (i > 5 && i < 9) {
                                images.add(list.get(i).getPhotoUrl());
                                titles.add("");
                            }
                        }
                        photoAdapter.notifyDataSetChanged();
                        initBanner();
                    }
                }
            }
        });
    }


    @OnClick(R.id.tv_search)
    public void onViewClicked() {
        startActivity(new Intent(actitity, PhotoFilterActivity.class));
    }
}

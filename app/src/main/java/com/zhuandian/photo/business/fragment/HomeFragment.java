package com.zhuandian.photo.business.fragment;

import android.content.Context;
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
import com.zhuandian.photo.entity.PhotoEntity;
import com.zhuandian.photo.entity.UserEntity;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
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
    List<String> images = new ArrayList<String>() {
        {
            add("http://img.zcool.cn/community/0114a856640b6d32f87545731c076a.jpg");
            add("http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg");
            add("http://img.zcool.cn/community/018fdb56e1428632f875520f7b67cb.jpg");
            add("http://img.zcool.cn/community/01c8dc56e1428e6ac72531cbaa5f2c.jpg");
        }
    };

    //设置图片标题:自动对应
    List<String> titles = new ArrayList<String>() {
        {
            add("十大星级品牌联盟");
            add("全场2折起");
            add("嗨购5折不要停");
            add("双12趁现在");
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        photoAdapter = new PhotoAdapter(mDatas, actitity);
        rvList.setAdapter(photoAdapter);
        rvList.setLayoutManager(new LinearLayoutManager(actitity));
        initBanner();
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
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.addWhereEqualTo("userId", BmobUser.getCurrentUser(UserEntity.class).getObjectId());
        query.setLimit(3);
        query.findObjects(new FindListener<PhotoEntity>() {
            @Override
            public void done(List<PhotoEntity> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            mDatas.add(list.get(i));
                        }
                        photoAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }


}

package com.zhuandian.photo.business.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zhuandian.base.BaseActivity;
import com.zhuandian.photo.R;
import com.zhuandian.photo.adapter.PhotoAdapter;
import com.zhuandian.photo.entity.LabelEntity;
import com.zhuandian.photo.entity.LocalEntity;
import com.zhuandian.photo.entity.PhotoEntity;
import com.zhuandian.photo.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class PhotoFilterActivity extends BaseActivity {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rg_type)
    RadioGroup rgType;
    @BindView(R.id.rg_local)
    RadioGroup rgLocal;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    private List<PhotoEntity> mDatas = new ArrayList<>();
    private PhotoAdapter photoAdapter;
    private String photoLocal;
    private String photoLabel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_photo_filter;
    }

    @Override
    protected void setUpView() {

        tvTitle.setText("照片筛选");
        photoAdapter = new PhotoAdapter(mDatas, this);
        rvList.setAdapter(photoAdapter);
        rvList.setLayoutManager(new GridLayoutManager(this,3));
        initPhotoLabel();
        initPhotoLocal();
    }

    private void initPhotoLocal() {
        BmobQuery<LocalEntity> query = new BmobQuery<>();
        query.findObjects(new FindListener<LocalEntity>() {
            @Override
            public void done(List<LocalEntity> list, BmobException e) {
                if (e == null) {
                    rgLocal.removeAllViews();

                    for (int i = 0; i < list.size(); i++) {
                        RadioButton radioButton = new RadioButton(PhotoFilterActivity.this);
                        radioButton.setText(list.get(i).getLocalName());
                        int finalI = i;
                        radioButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                photoLocal = list.get(finalI).getLocalName();
                            }
                        });
                        rgLocal.addView(radioButton);
                    }

                }
            }
        });
    }

    private void initPhotoLabel() {
        BmobQuery<LabelEntity> query = new BmobQuery<>();
        query.findObjects(new FindListener<LabelEntity>() {
            @Override
            public void done(List<LabelEntity> list, BmobException e) {
                if (e == null) {
                    rgType.removeAllViews();

                    for (int i = 0; i < list.size(); i++) {
                        RadioButton radioButton = new RadioButton(PhotoFilterActivity.this);
                        radioButton.setText(list.get(i).getLabelName());
                        int finalI = i;
                        radioButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                photoLabel = list.get(finalI).getLabelName();
                            }
                        });
                        rgType.addView(radioButton);
                    }

                }
            }
        });
    }




    @OnClick({R.id.iv_back, R.id.tv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_search:
                initPhotoList();
                break;
        }
    }

    private void initPhotoList() {
        mDatas.clear();
        BmobQuery<PhotoEntity> query = new BmobQuery<>();
        query.order("-updatedAt");
        query.addWhereEqualTo("photoUserId", BmobUser.getCurrentUser(UserEntity.class).getObjectId());
        query.addWhereEqualTo("photoLocal", photoLocal);
        query.addWhereEqualTo("photoLabel", photoLabel);
        query.setLimit(10);

        query.findObjects(new FindListener<PhotoEntity>() {
            @Override
            public void done(List<PhotoEntity> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        mDatas.add(list.get(i));
                    }
                    photoAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}

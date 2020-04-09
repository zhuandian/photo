package com.zhuandian.photo.business.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.photoview.PhotoView;
import com.zhuandian.base.BaseActivity;
import com.zhuandian.photo.R;
import com.zhuandian.photo.entity.PhotoEntity;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoDetailActivity extends BaseActivity {


    @BindView(R.id.preview_image)
    PhotoView previewImage;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_photo_detail;
    }

    @Override
    protected void setUpView() {
        PhotoEntity entity = (PhotoEntity) getIntent().getSerializableExtra("entity");
        Glide.with(this).load(entity.getPhotoUrl()).into(previewImage);
    }
}

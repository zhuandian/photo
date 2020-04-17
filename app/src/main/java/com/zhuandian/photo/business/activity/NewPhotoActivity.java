package com.zhuandian.photo.business.activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.zhuandian.base.BaseActivity;
import com.zhuandian.photo.R;
import com.zhuandian.photo.adapter.PhotoAdapter;
import com.zhuandian.photo.entity.LabelEntity;
import com.zhuandian.photo.entity.LocalEntity;
import com.zhuandian.photo.entity.PhotoEntity;
import com.zhuandian.photo.entity.UserEntity;
import com.zhuandian.photo.utils.ImageToBase64;
import com.zhuandian.photo.utils.LocationUtils;
import com.zhuandian.photo.utils.PictureSelectorUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class NewPhotoActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_select_photo)
    TextView tvSelectPhoto;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.et_photo_label)
    EditText etPhotoLabel;

    private List<PhotoEntity> mDatas = new ArrayList<>();
    private PhotoAdapter photoAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_photo;
    }

    @Override
    protected void setUpView() {
        tvRight.setText("上传");
        tvRight.setVisibility(View.VISIBLE);
        tvTitle.setText("上传图片");

        photoAdapter = new PhotoAdapter(mDatas, this);
        rvList.setAdapter(photoAdapter);
        rvList.setLayoutManager(new GridLayoutManager(this,3));
    }



    @OnClick({R.id.iv_back, R.id.tv_right, R.id.tv_select_photo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                break;
            case R.id.tv_right:
                uploadPhoto();
                break;
            case R.id.tv_select_photo:
                PictureSelectorUtils.selectImg(PictureSelector.create(this), 9);
                break;
        }
    }


    private void insertPhotoLabel2Sever() {
        BmobQuery<LabelEntity> query = new BmobQuery<>();
        query.findObjects(new FindListener<LabelEntity>() {
            @Override
            public void done(List<LabelEntity> list, BmobException e) {
                if (list.size() > 0) {
                    boolean isHaveLocal = false;
                    for (LabelEntity labelEntity : list) {
                        if (labelEntity.getLabelName().equals(etPhotoLabel.getText().toString())) {
                            isHaveLocal = true;
                            break;
                        }
                    }

                    if (!isHaveLocal){
                        LabelEntity labelEntity = new LabelEntity();
                        labelEntity.setLabelName(etPhotoLabel.getText().toString());
                        labelEntity.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {

                            }
                        });
                    }


                } else {
                    LabelEntity labelEntity = new LabelEntity();
                    labelEntity.setLabelName(etPhotoLabel.getText().toString());
                    labelEntity.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {

                        }
                    });
                }
            }
        });
    }
    private void uploadPhoto() {
        String label = etPhotoLabel.getText().toString();
        if (TextUtils.isEmpty(label)){
            Toast.makeText(this, "请先输入照片标签", Toast.LENGTH_SHORT).show();
            return;
        }

        insertPhotoLabel2Sever();

        for (int i = 0; i < mDatas.size(); i++) {
            mDatas.get(i).setPhotoLabel(label);
            mDatas.get(i).setPhotoLocal(LocationUtils.LOCATION_STR);
            mDatas.get(i).setPhotoUserId(BmobUser.getCurrentUser(UserEntity.class).getObjectId());
            int finalI = i;
            mDatas.get(i).save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    Toast.makeText(NewPhotoActivity.this, "上传中...", Toast.LENGTH_SHORT).show();
                    if (finalI ==mDatas.size()-1){
                        finish();
                    }
                }
            });
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                mDatas.clear();
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                for (LocalMedia localMedia:selectList){
                    BmobFile bmobFile = new BmobFile(new File(localMedia.getPath()));
                    bmobFile.upload(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null){
                                PhotoEntity photoEntity = new PhotoEntity();
                                photoEntity.setPhotoUrl(bmobFile.getUrl());
                                mDatas.add(photoEntity);
                                photoAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }

            }
        }
    }
}

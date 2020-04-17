package com.zhuandian.photo.business.activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
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
    @BindView(R.id.rg_type)
    RadioGroup rgType;

    private List<PhotoEntity> mDatas = new ArrayList<>();
    private PhotoAdapter photoAdapter;
    private List<String> photoLabelList = new ArrayList<>();

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
        rvList.setLayoutManager(new GridLayoutManager(this, 3));

        initPhotoLabel();
    }


    private void initPhotoLabel() {
        BmobQuery<LabelEntity> query = new BmobQuery<>();
        query.findObjects(new FindListener<LabelEntity>() {
            @Override
            public void done(List<LabelEntity> list, BmobException e) {
                if (e == null) {
                    rgType.removeAllViews();
                    photoLabelList.clear();

                    for (int i = 0; i < list.size(); i++) {
                        CheckBox checkBox = new CheckBox(NewPhotoActivity.this);
                        checkBox.setText(list.get(i).getLabelName());
                        int finalI = i;

                        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    photoLabelList.add(list.get(finalI).getLabelName());
                                } else {
                                    photoLabelList.remove(list.get(finalI).getLabelName());
                                }
                            }
                        });


                        rgType.addView(checkBox);
                    }

                }
            }
        });
    }


    @OnClick({R.id.iv_back, R.id.tv_right, R.id.tv_select_photo,R.id.tv_new_label})
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

            case R.id.tv_new_label:
                insertPhotoLabel2Sever();
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

                    if (!isHaveLocal) {
                        LabelEntity labelEntity = new LabelEntity();
                        String labelName = etPhotoLabel.getText().toString();
                        labelEntity.setLabelName(labelName);
                        labelEntity.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                CheckBox checkBox = new CheckBox(NewPhotoActivity.this);
                                checkBox.setText(etPhotoLabel.getText().toString());
                                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (isChecked) {
                                            photoLabelList.add(labelName);
                                        } else {
                                            photoLabelList.remove(labelName);
                                        }
                                    }
                                });
                                rgType.addView(checkBox);
                                etPhotoLabel.setText("");
                            }
                        });
                    }else {
                        Toast.makeText(NewPhotoActivity.this, "当前标签已存在", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    LabelEntity labelEntity = new LabelEntity();
                    String labelName = etPhotoLabel.getText().toString();
                    labelEntity.setLabelName(labelName);
                    labelEntity.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            CheckBox checkBox = new CheckBox(NewPhotoActivity.this);
                            checkBox.setText(etPhotoLabel.getText().toString());
                            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if (isChecked) {
                                        photoLabelList.add(labelName);
                                    } else {
                                        photoLabelList.remove(labelName);
                                    }
                                }
                            });
                            rgType.addView(checkBox);
                            etPhotoLabel.setText("");
                        }
                    });
                }
            }
        });
    }

    private void uploadPhoto() {
        if (photoLabelList.size()<=0) {
            Toast.makeText(this, "请先输入照片标签", Toast.LENGTH_SHORT).show();
            return;
        }



        for (int i = 0; i < mDatas.size(); i++) {
            mDatas.get(i).setPhotoLabel(photoLabelList);
            mDatas.get(i).setPhotoLocal(LocationUtils.LOCATION_STR);
            mDatas.get(i).setPhotoUserId(BmobUser.getCurrentUser(UserEntity.class).getObjectId());
            int finalI = i;
            mDatas.get(i).save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    Toast.makeText(NewPhotoActivity.this, "上传中...", Toast.LENGTH_SHORT).show();
                    if (finalI == mDatas.size() - 1) {
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
                for (LocalMedia localMedia : selectList) {
                    BmobFile bmobFile = new BmobFile(new File(localMedia.getPath()));
                    bmobFile.upload(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
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

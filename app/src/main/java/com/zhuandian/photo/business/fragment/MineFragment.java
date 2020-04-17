package com.zhuandian.photo.business.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.zhuandian.base.BaseFragment;
import com.zhuandian.photo.MainActivity;
import com.zhuandian.photo.R;

import com.zhuandian.photo.business.PersonalDataActivity;
import com.zhuandian.photo.business.activity.FootActivity;
import com.zhuandian.photo.business.activity.PhotoFilterActivity;
import com.zhuandian.photo.business.login.LoginActivity;
import com.zhuandian.photo.entity.PhotoEntity;
import com.zhuandian.photo.entity.UserEntity;
import com.zhuandian.photo.utils.PictureSelectorUtils;
import com.zhuandian.view.CircleImageView;

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
public class MineFragment extends BaseFragment {
    @BindView(R.id.iv_header)
    CircleImageView ivHeader;
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;
    @BindView(R.id.tv_photo_count)
    TextView tvPhotoCount;
    private SharedPreferences sharedPreferences;
    private UserEntity userEntity;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {
        userEntity = BmobUser.getCurrentUser(UserEntity.class);
        sharedPreferences = actitity.getSharedPreferences("config", Context.MODE_PRIVATE);
        String headerPath = sharedPreferences.getString(userEntity.getObjectId(), "");
        if (!headerPath.isEmpty()) {
            decodePath2Bitmap(headerPath);
        }

        if (userEntity != null) {
            tvNickName.setText(userEntity.getNikeName() == null ? userEntity.getUsername() : userEntity.getNikeName());
        }

        initPhotoCount();
    }

    private void initPhotoCount() {
        BmobQuery<PhotoEntity> query = new BmobQuery<>();
        query.addWhereEqualTo("photoUserId", userEntity.getObjectId());
        query.findObjects(new FindListener<PhotoEntity>() {
            @Override
            public void done(List<PhotoEntity> list, BmobException e) {
                if (e == null && list.size() > 0) {
                    tvPhotoCount.setText("共上传：" + list.size() + " 张图片");
                }
            }
        });
    }

    @OnClick({R.id.iv_header, R.id.tv_nick_name, R.id.tv_more_setting, R.id.tv_logout, R.id.tv_my_history, R.id.tv_my_photo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_header:
                PictureSelectorUtils.selectImg(PictureSelector.create(this), 1);
                break;
            case R.id.tv_my_history:
                startActivity(new Intent(actitity, FootActivity.class));
                break;
            case R.id.tv_my_photo:
                ((MainActivity) actitity).setCurrentPage(MainActivity.PAGE_PHOTO);
                break;
            case R.id.tv_more_setting:
                startActivity(new Intent(actitity, PersonalDataActivity.class));
                break;
            case R.id.tv_logout:
                startActivity(new Intent(actitity, LoginActivity.class));
                BmobUser.logOut();
                actitity.finish();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                if (selectList.size() > 0) {
                    String imagePath = selectList.get(0).getCompressPath();
                    sharedPreferences = actitity.getSharedPreferences("config", Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString(userEntity.getObjectId(), imagePath).commit();
                    decodePath2Bitmap(imagePath);
                }
            }
        }
    }


    /**
     * 把指定路径的image资源转成Bitmap
     *
     * @param path
     */
    private void decodePath2Bitmap(String path) {
        Bitmap bm = BitmapFactory.decodeFile(path);
        if (bm != null) {
            ivHeader.setImageBitmap(bm);
        }
    }

}

package com.zhuandian.photo.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.PicturePreviewActivity;
import com.zhuandian.base.BaseAdapter;
import com.zhuandian.base.BaseViewHolder;
import com.zhuandian.photo.R;
import com.zhuandian.photo.business.activity.PhotoDetailActivity;
import com.zhuandian.photo.entity.PhotoEntity;
import com.zhuandian.photo.utils.ImageToBase64;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class PhotoAdapter extends BaseAdapter<PhotoEntity, BaseViewHolder> {


    @BindView(R.id.iv_photo)
    ImageView ivPhoto;

    public PhotoAdapter(List<PhotoEntity> mDatas, Context context) {
        super(mDatas, context);
    }


    @Override
    protected void converData(BaseViewHolder myViewHolder, PhotoEntity photoEntity, int position) {
        ButterKnife.bind(this, myViewHolder.itemView);
        Glide.with(mContext).load(photoEntity.getPhotoUrl()).into(ivPhoto);
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PhotoDetailActivity.class);
                intent.putExtra("entity", photoEntity);
                mContext.startActivity(intent);
            }
        });

        myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(mContext)
                        .setTitle("确认删除？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                photoEntity.delete(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            dialog.dismiss();
                                            Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                        .show();
                return true;
            }
        });

    }


    @Override
    public int getItemLayoutId() {
        return R.layout.item_photo;
    }
}

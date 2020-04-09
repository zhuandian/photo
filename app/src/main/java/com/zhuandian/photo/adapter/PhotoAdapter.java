package com.zhuandian.photo.adapter;

import android.content.Context;

import com.zhuandian.base.BaseAdapter;
import com.zhuandian.base.BaseViewHolder;
import com.zhuandian.photo.R;
import com.zhuandian.photo.entity.PhotoEntity;
import java.util.List;

import butterknife.ButterKnife;

public class PhotoAdapter extends BaseAdapter<PhotoEntity, BaseViewHolder> {


    public PhotoAdapter(List<PhotoEntity> mDatas, Context context) {
        super(mDatas, context);
    }


    @Override
    protected void converData(BaseViewHolder myViewHolder, PhotoEntity photoEntity, int position) {
        ButterKnife.bind(this, myViewHolder.itemView);


    }



    @Override
    public int getItemLayoutId() {
        return R.layout.item_photo;
    }
}

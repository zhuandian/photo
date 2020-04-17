package com.zhuandian.photo.business.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;

import com.zhuandian.base.BaseFragment;
import com.zhuandian.photo.R;
import com.zhuandian.photo.adapter.PhotoAdapter;
import com.zhuandian.photo.business.activity.NewPhotoActivity;
import com.zhuandian.photo.entity.PhotoEntity;
import com.zhuandian.photo.entity.UserEntity;
import com.zhuandian.photo.utils.BaseRecyclerView;

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
public class PhotoFragment extends BaseFragment {
    @BindView(R.id.brv_list)
    BaseRecyclerView brvPhotoList;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;

    private List<PhotoEntity> mDatas = new ArrayList<>();
    private PhotoAdapter photoAdapter;
    private int currentCount = -10;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_photo;
    }

    @Override
    protected void initView() {
        ivBack.setVisibility(View.GONE);
        tvTitle.setText("图库列表");
        photoAdapter = new PhotoAdapter(mDatas, actitity);
        brvPhotoList.setRecyclerViewAdapter(photoAdapter);
        brvPhotoList.setRecyclerViewLayoutManager(new GridLayoutManager(actitity,3));
        loadDatas();
        initRefreshListener();
    }


    private void initRefreshListener() {
        brvPhotoList.setRefreshListener(new BaseRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentCount = -10; //重新置位
                mDatas.clear();
                photoAdapter.notifyDataSetChanged();
                loadDatas();

            }
        });
        brvPhotoList.setLoadMoreListener(new BaseRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadDatas();
            }
        });


    }


    private void loadDatas() {
//        currentCount = currentCount + 10;
        BmobQuery<PhotoEntity> query = new BmobQuery<>();

        query.order("-updatedAt");
        query.addWhereEqualTo("photoUserId", BmobUser.getCurrentUser(UserEntity.class).getObjectId());
//        query.setLimit(10);
//        query.setSkip(currentCount);

        query.findObjects(new FindListener<PhotoEntity>() {
            @Override
            public void done(List<PhotoEntity> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        mDatas.add(list.get(i));
                    }
                    photoAdapter.notifyDataSetChanged();
                    brvPhotoList.setRefreshLayoutState(false);
                } else {
                    brvPhotoList.setRefreshLayoutState(false);
                }
            }
        });
    }


    @OnClick(R.id.iv_new_photo)
    public void onViewClicked() {
        startActivity(new Intent(actitity, NewPhotoActivity.class));
    }
}

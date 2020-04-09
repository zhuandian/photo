package com.zhuandian.photo.business.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuandian.base.BaseFragment;
import com.zhuandian.photo.R;
import com.zhuandian.photo.adapter.PhotoAdapter;

import com.zhuandian.photo.entity.PhotoEntity;
import com.zhuandian.photo.entity.UserEntity;
import com.zhuandian.photo.utils.BaseRecyclerView;

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
public class PhotoFragment extends BaseFragment {
    @BindView(R.id.brv_list)
    BaseRecyclerView brvGoodsList;
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
        brvGoodsList.setRecyclerViewAdapter(photoAdapter);
        loadDatas();
        initRefreshListener();
    }


    private void initRefreshListener() {
        brvGoodsList.setRefreshListener(new BaseRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentCount = -10; //重新置位
                mDatas.clear();
                photoAdapter.notifyDataSetChanged();
                loadDatas();

            }
        });
        brvGoodsList.setLoadMoreListener(new BaseRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadDatas();
            }
        });


    }


    private void loadDatas() {
        currentCount = currentCount + 10;
        BmobQuery<PhotoEntity> query = new BmobQuery<>();
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.order("-updatedAt");
        query.addWhereEqualTo("userId", BmobUser.getCurrentUser(UserEntity.class).getObjectId());
//        query.addWhereEqualTo("state",1);
        query.setLimit(10);
        query.setSkip(currentCount);
        //这里查询规则有点绕，备注一下，如果userEntity不为null代表需要检索指定用户发布的二手商品信息，如果为null则检索全部
        //如果userEntity为当前登录用户，则需要有对商品下架的权限，否则只有查看权限

        query.findObjects(new FindListener<PhotoEntity>() {
            @Override
            public void done(List<PhotoEntity> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        mDatas.add(list.get(i));
                    }
                    photoAdapter.notifyDataSetChanged();
                    brvGoodsList.setRefreshLayoutState(false);
                } else {
                    brvGoodsList.setRefreshLayoutState(false);
                }
            }
        });
    }

}

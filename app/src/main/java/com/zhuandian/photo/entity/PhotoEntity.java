package com.zhuandian.photo.entity;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * desc :
 * author：xiedong
 */
public class PhotoEntity extends BmobObject {
   private String photoUrl;
   private String photoLocal;
   private String photoUserId;
   private List<String> photoLabel;


    public List<String> getPhotoLabel() {
        return photoLabel;
    }

    public void setPhotoLabel(List<String> photoLabel) {
        this.photoLabel = photoLabel;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoLocal() {
        return photoLocal;
    }

    public void setPhotoLocal(String photoLocal) {
        this.photoLocal = photoLocal;
    }

    public String getPhotoUserId() {
        return photoUserId;
    }

    public void setPhotoUserId(String photoUserId) {
        this.photoUserId = photoUserId;
    }
}

package com.zhuandian.photo.entity;

import cn.bmob.v3.BmobObject;

/**
 * @desc 地点标签
 * @date 2020-04-09.
 */
public class LocalEntity extends BmobObject {
    private String localName;

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }
}

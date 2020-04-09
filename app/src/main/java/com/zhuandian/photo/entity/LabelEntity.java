package com.zhuandian.photo.entity;

import cn.bmob.v3.BmobObject;

/**
 * @desc 照片标签
 * @date 2020-04-09.
 */
public class LabelEntity extends BmobObject {
    private String labelName;

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }
}

package com.geek.model.admin.enums;

import lombok.Getter;

@Getter
public enum CommonTableEnum {

    AD_CHANNEL("*", true, true, true, true),
    AD_SENSITIVE("*", true, true, true, true),
    // APP用户端。
    AP_ARTICLE("*", true, false, false, false),
    AP_ARTICLE_CONFIG("*", true, false, true, false),
    AP_USER("*", true, false, true, false);

    private String filed;
    private boolean list;// 开启列表权限？
    private boolean add;// 开启增加权限？
    private boolean update;// 开启修改权限？
    private boolean delete;// 开启删除权限？

    CommonTableEnum(String filed, boolean list, boolean add, boolean update, boolean delete) {
        this.filed = filed;
        this.list = list;
        this.add = add;
        this.update = update;
        this.delete = delete;
    }

}

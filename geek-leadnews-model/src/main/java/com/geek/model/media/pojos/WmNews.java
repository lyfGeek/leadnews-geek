package com.geek.model.media.pojos;

import com.geek.model.annotation.IdEncrypt;
import lombok.Data;

import java.util.Date;

@Data
public class WmNews {

    @IdEncrypt
    protected Long userId;
    private Integer id;
    private String title;
    private Short type;
    @IdEncrypt
    private Integer channelId;
    private String labels;
    private Date createdTime;
    private Date submitedTime;
    private Short status;
    private Date publishTime;
    private String reason;
    @IdEncrypt
    private Integer articleId;
    private String content;
    private String images; // 图片用逗号分隔。
    private Short enable;// 是否上下架。

}

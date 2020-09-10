package com.geek.model.media.dtos;

import com.geek.model.annotation.IdEncrypt;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class WmNewsDto {

    private Integer id;
    private String title;
    @IdEncrypt
    private Integer channelId;
    private String labels;
    private Date publishTime;
    private String content;
    private Short type;
    private Date submittedTime;
    private Short status;
    private String reason;
    private List<String> images;

}

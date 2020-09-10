package com.geek.model.media.dtos;

import com.geek.model.annotation.IdEncrypt;
import com.geek.model.common.dtos.PageRequestDto;
import lombok.Data;

import java.util.Date;

@Data
public class WmNewsPageReqDto extends PageRequestDto {

    private Short status;
    private Date beginPubDate;
    private Date endPubDate;
    @IdEncrypt
    private Integer channelId;
    private String keyWord;

}

package com.geek.model.user.dtos;

import com.geek.model.common.dtos.PageRequestDto;
import lombok.Data;

@Data
public class UserFansPageReqDto extends PageRequestDto {

    private String fansName;
    private Short level;

}

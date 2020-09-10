package com.geek.model.behavior.pojos;

import com.geek.model.annotation.IdEncrypt;
import lombok.Data;

import java.util.Date;

@Data
public class ApForwardBehavior {

    private Long id;
    @IdEncrypt
    private Integer entryId;
    private Integer articleId;
    private Integer dynamicId;
    private Date createdTime;

}

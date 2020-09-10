package com.geek.model.article.dtos;

import com.geek.model.annotation.IdEncrypt;
import com.geek.model.user.pojos.ApUserSearch;
import lombok.Data;

import java.util.List;

@Data
public class UserSearchDto {

    /**
     * 设备 ID。
     */
    @IdEncrypt
    private Integer equipmentId;
    private String searchWords;
    /**
     * 查询 tag: all, article, login, author。
     */
    private String tag;
    private List<ApUserSearch> hisList;
    private String hotDate;
    private int pageNum;
    private int pageSize;

    public int getFromIndex() {
        if (this.pageNum < 1) return 0;
        if (this.pageSize < 1) this.pageSize = 10;
        return this.pageSize * (pageNum - 1);
    }

}

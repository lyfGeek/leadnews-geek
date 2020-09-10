package com.geek.admin.service;

import com.geek.model.admin.dtos.CommonDto;
import com.geek.model.common.dtos.ResponseResult;

public interface ICommonService {

    /**
     * 列表查询 --> 无条件查询，无条件统计；有条件的查询 有条件的统计。
     *
     * @param dto
     * @return
     */
    ResponseResult list(CommonDto dto);

    /**
     * 通过 dto 中的 model 来判断，选择使用新增或修改。
     *
     * @param dto
     * @return
     */
    ResponseResult update(CommonDto dto);

    /**
     * 通用的删除。
     *
     * @param dto
     * @return
     */
    ResponseResult delete(CommonDto dto);

}

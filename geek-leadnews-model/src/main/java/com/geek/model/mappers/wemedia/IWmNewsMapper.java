package com.geek.model.mappers.wemedia;

import com.geek.model.media.dtos.WmNewsPageReqDto;
import com.geek.model.media.pojos.WmNews;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IWmNewsMapper {

    /**
     * 根据主键修改。
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(WmNews record);

    /**
     * 添加草稿新闻。
     *
     * @param dto
     * @return
     */
    int insertNewsForEdit(WmNews dto);

    /**
     * 查询根据 dto 条件。
     *
     * @param dto
     * @param uid
     * @return
     */
    List<WmNews> selectBySelective(@Param("dto") WmNewsPageReqDto dto, @Param("uid") Long uid);

    /**
     * 查询总数统计。
     *
     * @param dto
     * @param uid
     * @return
     */
    int countSelectBySelective(@Param("dto") WmNewsPageReqDto dto, @Param("uid") Long uid);

    WmNews selectNewsDetailByPrimaryKey(Integer id);

    int deleteByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WmNews record);

}

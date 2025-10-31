package com.hu.wink.mapper;

import com.hu.wink.model.dto.QuestionQueryRequest;
import com.hu.wink.model.entity.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
* @author hu
* @description 针对表【question(题目)】的数据库操作Mapper
* @createDate 2025-10-24 21:22:40
* @Entity com.hu.wink.model.entity.Question
*/
public interface QuestionMapper extends BaseMapper<Question> {

    /**
     * 根据题库ID分页查询题目（联查中间表）
     * @param page 分页参数
     * @param questionQueryRequest 查询条件
     * @return 分页结果
     */
    Page<Question> listQuestionByPageWithBank(@Param("page") Page<Question> page, @Param("questionQueryRequest") QuestionQueryRequest questionQueryRequest);

}





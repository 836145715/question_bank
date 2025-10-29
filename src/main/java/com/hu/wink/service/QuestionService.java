package com.hu.wink.service;

import com.hu.wink.model.entity.Question;
import com.hu.wink.model.vo.QuestionVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author hu
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2025-10-24 21:22:40
*/
public interface QuestionService extends IService<Question> {

    /**
     * 获取脱敏的题目信息
     *
     * @param question 题目实体
     * @return 题目视图
     */
    QuestionVO getQuestionVO(Question question);

    /**
     * 获取脱敏的题目信息列表
     *
     * @param questionList 题目实体列表
     * @return 题目视图列表
     */
    List<QuestionVO> getQuestionVO(List<Question> questionList);

}

package com.hu.wink.service;

import com.hu.wink.model.entity.QuestionBankQuestion;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author hu
* @description 针对表【question_bank_question(题库题目)】的数据库操作Service
* @createDate 2025-10-24 21:23:20
*/
public interface QuestionBankQuestionService extends IService<QuestionBankQuestion> {
    /**
     * 获取题库下的题目 id 列表
     * @param questionBankId 题库 id
     * @return 题目 id 列表
     */
    List<Long> getQuestionIdList(Long questionBankId);
}

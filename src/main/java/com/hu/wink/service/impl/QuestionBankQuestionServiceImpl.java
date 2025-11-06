package com.hu.wink.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hu.wink.model.entity.QuestionBankQuestion;
import com.hu.wink.service.QuestionBankQuestionService;
import com.hu.wink.mapper.QuestionBankQuestionMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author hu
* @description 针对表【question_bank_question(题库题目)】的数据库操作Service实现
* @createDate 2025-10-24 21:23:20
*/
@Service
public class QuestionBankQuestionServiceImpl extends ServiceImpl<QuestionBankQuestionMapper, QuestionBankQuestion>
    implements QuestionBankQuestionService{

    @Override
    public List<Long> getQuestionIdList(Long questionBankId) {
        LambdaQueryChainWrapper<QuestionBankQuestion> query = lambdaQuery();
        List<QuestionBankQuestion> list = query.eq(QuestionBankQuestion::getQuestionBankId, questionBankId)
                .select(QuestionBankQuestion::getQuestionId).list();
        return list.stream().map(QuestionBankQuestion::getQuestionId).collect(Collectors.toList());
    }
}





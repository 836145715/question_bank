package com.hu.wink.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hu.wink.model.entity.QuestionBank;
import com.hu.wink.service.QuestionBankService;
import com.hu.wink.mapper.QuestionBankMapper;
import org.springframework.stereotype.Service;

/**
* @author hu
* @description 针对表【question_bank(题库)】的数据库操作Service实现
* @createDate 2025-10-24 21:21:41
*/
@Service
public class QuestionBankServiceImpl extends ServiceImpl<QuestionBankMapper, QuestionBank>
    implements QuestionBankService{

}





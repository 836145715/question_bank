package com.hu.wink.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hu.wink.model.entity.Question;
import com.hu.wink.service.QuestionService;
import com.hu.wink.mapper.QuestionMapper;
import org.springframework.stereotype.Service;

/**
* @author hu
* @description 针对表【question(题目)】的数据库操作Service实现
* @createDate 2025-10-24 21:22:40
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

}





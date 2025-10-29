package com.hu.wink.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hu.wink.model.entity.Question;
import com.hu.wink.model.entity.User;
import com.hu.wink.model.vo.QuestionVO;
import com.hu.wink.model.vo.UserVO;
import com.hu.wink.service.QuestionService;
import com.hu.wink.service.UserService;
import com.hu.wink.mapper.QuestionMapper;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author hu
* @description 针对表【question(题目)】的数据库操作Service实现
* @createDate 2025-10-24 21:22:40
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

    @Resource
    private UserService userService;

    @Override
    public QuestionVO getQuestionVO(Question question) {
        if (question == null) {
            return null;
        }

        QuestionVO questionVO = new QuestionVO();
        questionVO.setId(question.getId());
        questionVO.setTitle(question.getTitle());
        questionVO.setContent(question.getContent());
        questionVO.setTags(question.getTags());
        questionVO.setAnswer(question.getAnswer());
        questionVO.setCreateTime(question.getCreateTime());
        questionVO.setUpdateTime(question.getUpdateTime());
        questionVO.setEditTime(question.getEditTime());
        questionVO.setReviewStatus(question.getReviewStatus());
        questionVO.setReviewMessage(question.getReviewMessage());
        questionVO.setReviewTime(question.getReviewTime());
        questionVO.setViewNum(question.getViewNum());
        questionVO.setThumbNum(question.getThumbNum());
        questionVO.setFavourNum(question.getFavourNum());
        questionVO.setPriority(question.getPriority());
        questionVO.setSource(question.getSource());
        questionVO.setNeedVip(question.getNeedVip());

        // 设置创建者信息
        Long userId = question.getUserId();
        if (userId != null && userId > 0) {
            User user = userService.getById(userId);
            UserVO userVO = userService.getUserVO(user);
            questionVO.setUser(userVO);
        }

        // 设置审核人信息
        Long reviewerId = question.getReviewerId();
        if (reviewerId != null && reviewerId > 0) {
            User reviewer = userService.getById(reviewerId);
            UserVO reviewerVO = userService.getUserVO(reviewer);
            questionVO.setReviewer(reviewerVO);
        }

        return questionVO;
    }

    @Override
    public List<QuestionVO> getQuestionVO(List<Question> questionList) {
        if (questionList == null || questionList.isEmpty()) {
            return null;
        }
        return questionList.stream().map(this::getQuestionVO).collect(Collectors.toList());
    }

}





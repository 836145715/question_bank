package com.hu.wink.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hu.wink.common.ErrorCode;
import com.hu.wink.exception.BusinessException;
import com.hu.wink.exception.ThrowUtils;
import com.hu.wink.model.dto.QuestionAddRequest;
import com.hu.wink.model.dto.QuestionEditRequest;
import com.hu.wink.model.dto.QuestionQueryRequest;
import com.hu.wink.model.dto.QuestionUpdateRequest;
import com.hu.wink.model.entity.Question;
import com.hu.wink.model.entity.User;
import com.hu.wink.model.vo.QuestionVO;
import com.hu.wink.model.vo.UserVO;
import com.hu.wink.service.QuestionService;
import com.hu.wink.service.UserService;
import com.hu.wink.mapper.QuestionMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
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
    public long addQuestion(QuestionAddRequest questionAddRequest, HttpServletRequest request) {
        if (questionAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);

        Question question = new Question();
        question.setTitle(questionAddRequest.getTitle());
        question.setContent(questionAddRequest.getContent());
        question.setTags(questionAddRequest.getTags());
        question.setAnswer(questionAddRequest.getAnswer());
        question.setUserId(loginUser.getId());
        question.setPriority(questionAddRequest.getPriority());
        question.setSource(questionAddRequest.getSource());
        question.setNeedVip(questionAddRequest.getNeedVip());
        question.setViewNum(0);
        question.setThumbNum(0);
        question.setFavourNum(0);
        question.setReviewStatus(0); // 默认待审核

        boolean result = this.save(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return question.getId();
    }

    @Override
    public boolean updateQuestion(QuestionUpdateRequest questionUpdateRequest, HttpServletRequest request) {
        if (questionUpdateRequest == null || questionUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);

        Question oldQuestion = this.getById(questionUpdateRequest.getId());
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);

        // 只有管理员或创建者可以修改
        if (!userService.isAdmin(loginUser) && !loginUser.getId().equals(oldQuestion.getUserId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        Question updateQuestion = new Question();
        updateQuestion.setId(questionUpdateRequest.getId());
        if (StringUtils.isNotBlank(questionUpdateRequest.getTitle())) {
            updateQuestion.setTitle(questionUpdateRequest.getTitle());
        }
        if (StringUtils.isNotBlank(questionUpdateRequest.getContent())) {
            updateQuestion.setContent(questionUpdateRequest.getContent());
        }
        if (questionUpdateRequest.getTags() != null && !questionUpdateRequest.getTags().isEmpty()) {
            updateQuestion.setTags(questionUpdateRequest.getTags());
        }
        if (StringUtils.isNotBlank(questionUpdateRequest.getAnswer())) {
            updateQuestion.setAnswer(questionUpdateRequest.getAnswer());
        }
        if (questionUpdateRequest.getPriority() != null) {
            updateQuestion.setPriority(questionUpdateRequest.getPriority());
        }
        if (StringUtils.isNotBlank(questionUpdateRequest.getSource())) {
            updateQuestion.setSource(questionUpdateRequest.getSource());
        }
        if (questionUpdateRequest.getNeedVip() != null) {
            updateQuestion.setNeedVip(questionUpdateRequest.getNeedVip());
        }
        updateQuestion.setEditTime(new Date());

        boolean result = this.updateById(updateQuestion);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return result;
    }

    @Override
    public boolean reviewQuestion(QuestionEditRequest questionEditRequest, HttpServletRequest request) {
        if (questionEditRequest == null || questionEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);
        ThrowUtils.throwIf(!userService.isAdmin(loginUser), ErrorCode.NO_AUTH_ERROR);

        Question oldQuestion = this.getById(questionEditRequest.getId());
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);

        Question updateQuestion = new Question();
        updateQuestion.setId(questionEditRequest.getId());
        updateQuestion.setReviewStatus(questionEditRequest.getReviewStatus());
        updateQuestion.setReviewMessage(questionEditRequest.getReviewMessage());
        updateQuestion.setReviewerId(loginUser.getId());
        updateQuestion.setReviewTime(new Date());

        boolean result = this.updateById(updateQuestion);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return result;
    }

    @Override
    public boolean deleteQuestion(long id, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);

        Question oldQuestion = this.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);

        // 只有管理员或创建者可以删除
        if (!userService.isAdmin(loginUser) && !loginUser.getId().equals(oldQuestion.getUserId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        boolean result = this.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return result;
    }

    @Override
    public Question getQuestionById(long id) {
        return this.getById(id);
    }

    @Override
    public QuestionVO getQuestionVOById(long id) {
        Question question = this.getById(id);
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR);
        return getQuestionVO(question);
    }

    @Override
    public Page<Question> listQuestionByPage(QuestionQueryRequest questionQueryRequest) {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        QueryWrapper<Question> queryWrapper = getQueryWrapper(questionQueryRequest);
        return this.page(new Page<>(current, size), queryWrapper);
    }

    @Override
    public Page<QuestionVO> listQuestionVOByPage(QuestionQueryRequest questionQueryRequest) {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 只查询通过审核的题目
        QuestionQueryRequest userQueryRequest = new QuestionQueryRequest();
        userQueryRequest.setCurrent((int) current);
        userQueryRequest.setPageSize((int) size);
//        userQueryRequest.setReviewStatus(1); // 只查询已通过的
        userQueryRequest.setTitle(questionQueryRequest.getTitle());
        userQueryRequest.setTags(questionQueryRequest.getTags());
        userQueryRequest.setSource(questionQueryRequest.getSource());
        userQueryRequest.setNeedVip(questionQueryRequest.getNeedVip());
        userQueryRequest.setMinViewNum(questionQueryRequest.getMinViewNum());
        userQueryRequest.setMaxViewNum(questionQueryRequest.getMaxViewNum());
        userQueryRequest.setMinThumbNum(questionQueryRequest.getMinThumbNum());
        userQueryRequest.setMaxThumbNum(questionQueryRequest.getMaxThumbNum());
        userQueryRequest.setMinFavourNum(questionQueryRequest.getMinFavourNum());
        userQueryRequest.setMaxFavourNum(questionQueryRequest.getMaxFavourNum());
        userQueryRequest.setSortField("viewNum");
        userQueryRequest.setSortOrder("desc");

        QueryWrapper<Question> queryWrapper = getQueryWrapper(userQueryRequest);
        Page<Question> questionPage = this.page(new Page<>(current, size), queryWrapper);
        // 转换为VO
        Page<QuestionVO> questionVOPage = new Page<>();
        questionVOPage.setCurrent(questionPage.getCurrent());
        questionVOPage.setSize(questionPage.getSize());
        questionVOPage.setTotal(questionPage.getTotal());
        List<QuestionVO> questionVOList = getQuestionVO(questionPage.getRecords());
        questionVOPage.setRecords(questionVOList);
        return questionVOPage;
    }

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

    @Override
    public QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        if (questionQueryRequest == null) {
            return queryWrapper;
        }
        Long id = questionQueryRequest.getId();
        String title = questionQueryRequest.getTitle();
        String content = questionQueryRequest.getContent();
        String tags = questionQueryRequest.getTags();
        Long userId = questionQueryRequest.getUserId();
        Integer reviewStatus = questionQueryRequest.getReviewStatus();
        Long reviewerId = questionQueryRequest.getReviewerId();
        Integer priority = questionQueryRequest.getPriority();
        String source = questionQueryRequest.getSource();
        Integer needVip = questionQueryRequest.getNeedVip();
        Integer minViewNum = questionQueryRequest.getMinViewNum();
        Integer maxViewNum = questionQueryRequest.getMaxViewNum();
        Integer minThumbNum = questionQueryRequest.getMinThumbNum();
        Integer maxThumbNum = questionQueryRequest.getMaxThumbNum();
        Integer minFavourNum = questionQueryRequest.getMinFavourNum();
        Integer maxFavourNum = questionQueryRequest.getMaxFavourNum();
        String sortField = questionQueryRequest.getSortField();
        String sortOrder = questionQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.eq(id != null && id > 0, "id", id);
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.like(StringUtils.isNotBlank(tags), "tags", tags);
        queryWrapper.eq(userId != null && userId > 0, "userId", userId);
        queryWrapper.eq(reviewStatus != null, "reviewStatus", reviewStatus);
        queryWrapper.eq(reviewerId != null && reviewerId > 0, "reviewerId", reviewerId);
        queryWrapper.eq(priority != null, "priority", priority);
        queryWrapper.eq(StringUtils.isNotBlank(source), "source", source);
        queryWrapper.eq(needVip != null, "needVip", needVip);
        queryWrapper.ge(minViewNum != null && minViewNum >= 0, "viewNum", minViewNum);
        queryWrapper.le(maxViewNum != null && maxViewNum >= 0, "viewNum", maxViewNum);
        queryWrapper.ge(minThumbNum != null && minThumbNum >= 0, "thumbNum", minThumbNum);
        queryWrapper.le(maxThumbNum != null && maxThumbNum >= 0, "thumbNum", maxThumbNum);
        queryWrapper.ge(minFavourNum != null && minFavourNum >= 0, "favourNum", minFavourNum);
        queryWrapper.le(maxFavourNum != null && maxFavourNum >= 0, "favourNum", maxFavourNum);
        // 排序
        if (StringUtils.isNotBlank(sortField)) {
            boolean isAsc = "ascend".equalsIgnoreCase(sortOrder);
            queryWrapper.orderBy(true, isAsc, sortField);
        } else {
            queryWrapper.orderByDesc("createTime");
        }
        return queryWrapper;
    }

}





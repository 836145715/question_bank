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
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        question.setNeedVip(questionAddRequest.getNeedVip() == null ? 0 : questionAddRequest.getNeedVip());
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
        if (StringUtils.isNotBlank(questionUpdateRequest.getTags())) {
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

        return this.updateById(updateQuestion);
    }

    @Override
    public boolean reviewQuestion(QuestionEditRequest questionEditRequest, HttpServletRequest request) {
        if (questionEditRequest == null || questionEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);
        ThrowUtils.throwIf(!userService.isAdmin(loginUser), ErrorCode.NO_AUTH_ERROR);

        Question question = this.getById(questionEditRequest.getId());
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR);

        question.setReviewStatus(questionEditRequest.getReviewStatus());
        question.setReviewMessage(questionEditRequest.getReviewMessage());
        question.setReviewerId(loginUser.getId());
        question.setReviewTime(new Date());

        return this.updateById(question);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteQuestion(long id, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);

        Question question = this.getById(id);
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR);

        // 只有管理员或创建者可以删除
        if (!userService.isAdmin(loginUser) && !loginUser.getId().equals(question.getUserId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        return this.removeById(id);
    }

    @Override
    public Question getQuestionById(long id) {
        return this.getById(id);
    }

    @Override
    public QuestionVO getQuestionVOById(long id) {
        Question question = this.getById(id);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // 只有通过审核的题目才能被普通用户查看
        if (question.getReviewStatus() != 1) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "题目未通过审核");
        }

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

        // 只查询通过审核的题目 - 创建新对象避免修改原对象
        QuestionQueryRequest userQueryRequest = new QuestionQueryRequest();
        userQueryRequest.setCurrent((int) current);
        userQueryRequest.setPageSize((int) size);
        userQueryRequest.setReviewStatus(1); // 只显示通过审核的

        // 复制其他查询条件
        userQueryRequest.setTitle(questionQueryRequest.getTitle());
        userQueryRequest.setContent(questionQueryRequest.getContent());
        userQueryRequest.setTags(questionQueryRequest.getTags());
        userQueryRequest.setUserId(questionQueryRequest.getUserId());
        userQueryRequest.setReviewerId(questionQueryRequest.getReviewerId());
        userQueryRequest.setPriority(questionQueryRequest.getPriority());
        userQueryRequest.setSource(questionQueryRequest.getSource());
        userQueryRequest.setNeedVip(questionQueryRequest.getNeedVip());
        userQueryRequest.setMinViewNum(questionQueryRequest.getMinViewNum());
        userQueryRequest.setMaxViewNum(questionQueryRequest.getMaxViewNum());
        userQueryRequest.setMinThumbNum(questionQueryRequest.getMinThumbNum());
        userQueryRequest.setMaxThumbNum(questionQueryRequest.getMaxThumbNum());
        userQueryRequest.setMinFavourNum(questionQueryRequest.getMinFavourNum());
        userQueryRequest.setMaxFavourNum(questionQueryRequest.getMaxFavourNum());
        userQueryRequest.setSortField(questionQueryRequest.getSortField());
        userQueryRequest.setSortOrder(questionQueryRequest.getSortOrder());

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
    public boolean thumbQuestion(long id, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);

        Question question = this.getById(id);
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR);

        // 只有通过审核的题目才能点赞
        if (question.getReviewStatus() != 1) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "题目未通过审核");
        }

        // TODO: 实际项目中应该检查用户是否已经点赞，这里简化处理
        question.setThumbNum(question.getThumbNum() + 1);
        return this.updateById(question);
    }

    @Override
    public boolean favourQuestion(long id, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);

        Question question = this.getById(id);
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR);

        // 只有通过审核的题目才能收藏
        if (question.getReviewStatus() != 1) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "题目未通过审核");
        }

        // TODO: 实际项目中应该检查用户是否已经收藏，这里简化处理
        question.setFavourNum(question.getFavourNum() + 1);
        return this.updateById(question);
    }


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

        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.like(StringUtils.isNotBlank(tags), "tags", tags);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(reviewStatus), "reviewStatus", reviewStatus);
        queryWrapper.eq(ObjectUtils.isNotEmpty(reviewerId), "reviewerId", reviewerId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(priority), "priority", priority);
        queryWrapper.eq(ObjectUtils.isNotEmpty(source), "source", source);
        queryWrapper.eq(ObjectUtils.isNotEmpty(needVip), "needVip", needVip);
        queryWrapper.ge(ObjectUtils.isNotEmpty(minViewNum), "viewNum", minViewNum);
        queryWrapper.le(ObjectUtils.isNotEmpty(maxViewNum), "viewNum", maxViewNum);
        queryWrapper.ge(ObjectUtils.isNotEmpty(minThumbNum), "thumbNum", minThumbNum);
        queryWrapper.le(ObjectUtils.isNotEmpty(maxThumbNum), "thumbNum", maxThumbNum);
        queryWrapper.ge(ObjectUtils.isNotEmpty(minFavourNum), "favourNum", minFavourNum);
        queryWrapper.le(ObjectUtils.isNotEmpty(maxFavourNum), "favourNum", maxFavourNum);
        queryWrapper.orderBy(ObjectUtils.isNotEmpty(sortField),
            "ascend".equals(sortOrder), sortField);

        return queryWrapper;
    }
}





package com.hu.wink.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hu.wink.common.ErrorCode;
import com.hu.wink.exception.BusinessException;
import com.hu.wink.exception.ThrowUtils;
import com.hu.wink.model.dto.QuestionBankAddRequest;
import com.hu.wink.model.dto.QuestionBankEditRequest;
import com.hu.wink.model.dto.QuestionBankQueryRequest;
import com.hu.wink.model.dto.QuestionBankUpdateRequest;
import com.hu.wink.model.entity.QuestionBank;
import com.hu.wink.model.entity.QuestionBankQuestion;
import com.hu.wink.model.entity.User;
import com.hu.wink.model.vo.QuestionBankVO;
import com.hu.wink.model.vo.UserVO;
import com.hu.wink.service.QuestionBankQuestionService;
import com.hu.wink.service.QuestionBankService;
import com.hu.wink.service.QuestionService;
import com.hu.wink.service.UserService;
import com.hu.wink.mapper.QuestionBankMapper;
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
* @description 针对表【question_bank(题库)】的数据库操作Service实现
* @createDate 2025-10-24 21:21:41
*/
@Service
public class QuestionBankServiceImpl extends ServiceImpl<QuestionBankMapper, QuestionBank>
    implements QuestionBankService{

    @Resource
    private UserService userService;

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionBankQuestionService questionBankQuestionService;

    @Override
    public long addQuestionBank(QuestionBankAddRequest questionBankAddRequest, HttpServletRequest request) {
        if (questionBankAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);

        QuestionBank questionBank = new QuestionBank();
        questionBank.setTitle(questionBankAddRequest.getTitle());
        questionBank.setDescription(questionBankAddRequest.getDescription());
        questionBank.setPicture(questionBankAddRequest.getPicture());
        questionBank.setUserId(loginUser.getId());
        questionBank.setPriority(questionBankAddRequest.getPriority());
        questionBank.setViewNum(0);
        questionBank.setReviewStatus(0); // 默认待审核

        boolean result = this.save(questionBank);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return questionBank.getId();
    }

    @Override
    public boolean updateQuestionBank(QuestionBankUpdateRequest questionBankUpdateRequest, HttpServletRequest request) {
        if (questionBankUpdateRequest == null || questionBankUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);

        QuestionBank oldQuestionBank = this.getById(questionBankUpdateRequest.getId());
        ThrowUtils.throwIf(oldQuestionBank == null, ErrorCode.NOT_FOUND_ERROR);

        // 只有管理员或创建者可以修改
        if (!userService.isAdmin(loginUser) && !loginUser.getId().equals(oldQuestionBank.getUserId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        QuestionBank updateQuestionBank = new QuestionBank();
        updateQuestionBank.setId(questionBankUpdateRequest.getId());
        if (StringUtils.isNotBlank(questionBankUpdateRequest.getTitle())) {
            updateQuestionBank.setTitle(questionBankUpdateRequest.getTitle());
        }
        if (StringUtils.isNotBlank(questionBankUpdateRequest.getDescription())) {
            updateQuestionBank.setDescription(questionBankUpdateRequest.getDescription());
        }
        if (StringUtils.isNotBlank(questionBankUpdateRequest.getPicture())) {
            updateQuestionBank.setPicture(questionBankUpdateRequest.getPicture());
        }
        if (questionBankUpdateRequest.getPriority() != null) {
            updateQuestionBank.setPriority(questionBankUpdateRequest.getPriority());
        }
        updateQuestionBank.setEditTime(new Date());

        return this.updateById(updateQuestionBank);
    }

    @Override
    public boolean reviewQuestionBank(QuestionBankEditRequest questionBankEditRequest, HttpServletRequest request) {
        if (questionBankEditRequest == null || questionBankEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);
        ThrowUtils.throwIf(!userService.isAdmin(loginUser), ErrorCode.NO_AUTH_ERROR);

        QuestionBank questionBank = this.getById(questionBankEditRequest.getId());
        ThrowUtils.throwIf(questionBank == null, ErrorCode.NOT_FOUND_ERROR);

        questionBank.setReviewStatus(questionBankEditRequest.getReviewStatus());
        questionBank.setReviewMessage(questionBankEditRequest.getReviewMessage());
        questionBank.setReviewerId(loginUser.getId());
        questionBank.setReviewTime(new Date());

        return this.updateById(questionBank);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteQuestionBank(long id, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);

        QuestionBank questionBank = this.getById(id);
        ThrowUtils.throwIf(questionBank == null, ErrorCode.NOT_FOUND_ERROR);

        // 只有管理员或创建者可以删除
        if (!userService.isAdmin(loginUser) && !loginUser.getId().equals(questionBank.getUserId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 删除题库
        boolean result = this.removeById(id);

        // 删除题库下的题目关联
        if (result) {
            QueryWrapper<QuestionBankQuestion> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("questionBankId", id);
            questionBankQuestionService.remove(deleteWrapper);
        }

        return result;
    }

    @Override
    public QuestionBank getQuestionBankById(long id) {
        return this.getById(id);
    }

    @Override
    public QuestionBankVO getQuestionBankVOById(long id) {
        QuestionBank questionBank = this.getById(id);
        if (questionBank == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // 只有通过审核的题库才能被普通用户查看
        if (questionBank.getReviewStatus() != 1) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "题库未通过审核");
        }

        return getQuestionBankVO(questionBank);
    }

    @Override
    public Page<QuestionBank> listQuestionBankByPage(QuestionBankQueryRequest questionBankQueryRequest) {
        long current = questionBankQueryRequest.getCurrent();
        long size = questionBankQueryRequest.getPageSize();
        QueryWrapper<QuestionBank> queryWrapper = getQueryWrapper(questionBankQueryRequest);
        return this.page(new Page<>(current, size), queryWrapper);
    }

    @Override
    public Page<QuestionBankVO> listQuestionBankVOByPage(QuestionBankQueryRequest questionBankQueryRequest) {
        long current = questionBankQueryRequest.getCurrent();
        long size = questionBankQueryRequest.getPageSize();

        // 只查询通过审核的题库
        QuestionBankQueryRequest userQueryRequest = new QuestionBankQueryRequest();
        userQueryRequest.setCurrent((int) current);
        userQueryRequest.setPageSize((int) size);
        userQueryRequest.setReviewStatus(1); // 只显示通过审核的

        QueryWrapper<QuestionBank> queryWrapper = getQueryWrapper(userQueryRequest);
        Page<QuestionBank> questionBankPage = this.page(new Page<>(current, size), queryWrapper);

        // 转换为VO
        Page<QuestionBankVO> questionBankVOPage = new Page<>();
        questionBankVOPage.setCurrent(questionBankPage.getCurrent());
        questionBankVOPage.setSize(questionBankPage.getSize());
        questionBankVOPage.setTotal(questionBankPage.getTotal());

        List<QuestionBankVO> questionBankVOList = getQuestionBankVO(questionBankPage.getRecords());
        questionBankVOPage.setRecords(questionBankVOList);

        return questionBankVOPage;
    }

    @Override
    public QuestionBankVO getQuestionBankVO(QuestionBank questionBank) {
        if (questionBank == null) {
            return null;
        }

        QuestionBankVO questionBankVO = new QuestionBankVO();
        questionBankVO.setId(questionBank.getId());
        questionBankVO.setTitle(questionBank.getTitle());
        questionBankVO.setDescription(questionBank.getDescription());
        questionBankVO.setPicture(questionBank.getPicture());
        questionBankVO.setCreateTime(questionBank.getCreateTime());
        questionBankVO.setUpdateTime(questionBank.getUpdateTime());
        questionBankVO.setEditTime(questionBank.getEditTime());
        questionBankVO.setReviewStatus(questionBank.getReviewStatus());
        questionBankVO.setReviewMessage(questionBank.getReviewMessage());
        questionBankVO.setReviewTime(questionBank.getReviewTime());
        questionBankVO.setPriority(questionBank.getPriority());
        questionBankVO.setViewNum(questionBank.getViewNum());

        // 设置创建者信息
        Long userId = questionBank.getUserId();
        if (userId != null && userId > 0) {
            User user = userService.getById(userId);
            UserVO userVO = userService.getUserVO(user);
            questionBankVO.setUser(userVO);
        }

        // 设置审核人信息
        Long reviewerId = questionBank.getReviewerId();
        if (reviewerId != null && reviewerId > 0) {
            User reviewer = userService.getById(reviewerId);
            UserVO reviewerVO = userService.getUserVO(reviewer);
            questionBankVO.setReviewer(reviewerVO);
        }

        // 获取题目数量
        QueryWrapper<QuestionBankQuestion> countWrapper = new QueryWrapper<>();
        countWrapper.eq("questionBankId", questionBank.getId());
        long questionCount = questionBankQuestionService.count(countWrapper);
        questionBankVO.setQuestionCount((int) questionCount);

        return questionBankVO;
    }

    @Override
    public List<QuestionBankVO> getQuestionBankVO(List<QuestionBank> questionBankList) {
        if (questionBankList == null || questionBankList.isEmpty()) {
            return null;
        }
        return questionBankList.stream().map(this::getQuestionBankVO).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper<QuestionBank> getQueryWrapper(QuestionBankQueryRequest questionBankQueryRequest) {
        QueryWrapper<QuestionBank> queryWrapper = new QueryWrapper<>();
        if (questionBankQueryRequest == null) {
            return queryWrapper;
        }
        Long id = questionBankQueryRequest.getId();
        String title = questionBankQueryRequest.getTitle();
        String description = questionBankQueryRequest.getDescription();
        Long userId = questionBankQueryRequest.getUserId();
        Integer reviewStatus = questionBankQueryRequest.getReviewStatus();
        Long reviewerId = questionBankQueryRequest.getReviewerId();
        Integer priority = questionBankQueryRequest.getPriority();
        Integer minViewNum = questionBankQueryRequest.getMinViewNum();
        Integer maxViewNum = questionBankQueryRequest.getMaxViewNum();
        String sortField = questionBankQueryRequest.getSortField();
        String sortOrder = questionBankQueryRequest.getSortOrder();

        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(reviewStatus), "reviewStatus", reviewStatus);
        queryWrapper.eq(ObjectUtils.isNotEmpty(reviewerId), "reviewerId", reviewerId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(priority), "priority", priority);
        queryWrapper.ge(ObjectUtils.isNotEmpty(minViewNum), "viewNum", minViewNum);
        queryWrapper.le(ObjectUtils.isNotEmpty(maxViewNum), "viewNum", maxViewNum);
        queryWrapper.orderBy(ObjectUtils.isNotEmpty(sortField),
            "ascend".equals(sortOrder), sortField);

        return queryWrapper;
    }

}





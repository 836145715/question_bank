package com.hu.wink.controller;

import com.hu.wink.annotation.AuthCheck;
import com.hu.wink.common.BaseResponse;
import com.hu.wink.common.ErrorCode;
import com.hu.wink.common.ResultUtils;
import com.hu.wink.constant.UserConstant;
import com.hu.wink.exception.BusinessException;
import com.hu.wink.exception.ThrowUtils;
import com.hu.wink.model.entity.QuestionBank;
import com.hu.wink.model.entity.QuestionBankQuestion;
import com.hu.wink.model.entity.Question;
import com.hu.wink.service.QuestionBankQuestionService;
import com.hu.wink.service.QuestionBankService;
import com.hu.wink.service.QuestionService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 题库题目关联接口
 *
 *
 */
@RestController
@RequestMapping("/questionBankQuestion")
@Slf4j
public class QuestionBankQuestionController {

    @Resource
    private QuestionBankQuestionService questionBankQuestionService;

    @Resource
    private QuestionBankService questionBankService;

    @Resource
    private QuestionService questionService;

    /**
     * 添加题目到题库（管理员）
     *
     * @param questionBankId
     * @param questionId
     * @param questionOrder
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> addQuestionToBank(
            @RequestParam long questionBankId,
            @RequestParam long questionId,
            @RequestParam(required = false, defaultValue = "0") int questionOrder,
            HttpServletRequest request) {

        if (questionBankId <= 0 || questionId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 检查题库是否存在
        QuestionBank questionBank = questionBankService.getById(questionBankId);
        ThrowUtils.throwIf(questionBank == null, ErrorCode.NOT_FOUND_ERROR, "题库不存在");

        // 检查题目是否存在
        Question question = questionService.getById(questionId);
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR, "题目不存在");

        // 检查是否已经添加
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<QuestionBankQuestion> queryWrapper =
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        queryWrapper.eq("questionBankId", questionBankId);
        queryWrapper.eq("questionId", questionId);
        QuestionBankQuestion existRelation = questionBankQuestionService.getOne(queryWrapper);
        ThrowUtils.throwIf(existRelation != null, ErrorCode.OPERATION_ERROR, "题目已在题库中");

        // 创建关联
        QuestionBankQuestion questionBankQuestion = new QuestionBankQuestion();
        questionBankQuestion.setQuestionBankId(questionBankId);
        questionBankQuestion.setQuestionId(questionId);
        questionBankQuestion.setQuestionOrder(questionOrder);

        boolean result = questionBankQuestionService.save(questionBankQuestion);
        return ResultUtils.success(result);
    }

    /**
     * 从题库移除题目（管理员）
     *
     * @param questionBankId
     * @param questionId
     * @param request
     * @return
     */
    @PostMapping("/remove")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> removeQuestionFromBank(
            @RequestParam long questionBankId,
            @RequestParam long questionId,
            HttpServletRequest request) {

        if (questionBankId <= 0 || questionId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<QuestionBankQuestion> queryWrapper =
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        queryWrapper.eq("questionBankId", questionBankId);
        queryWrapper.eq("questionId", questionId);

        boolean result = questionBankQuestionService.remove(queryWrapper);
        return ResultUtils.success(result);
    }

    /**
     * 批量添加题目到题库（管理员）
     *
     * @param questionBankId
     * @param questionIds
     * @param request
     * @return
     */
    @PostMapping("/batchAdd")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> batchAddQuestionsToBank(
            @RequestParam long questionBankId,
            @RequestBody List<Long> questionIds,
            HttpServletRequest request) {

        if (questionBankId <= 0 || questionIds == null || questionIds.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 检查题库是否存在
        QuestionBank questionBank = questionBankService.getById(questionBankId);
        ThrowUtils.throwIf(questionBank == null, ErrorCode.NOT_FOUND_ERROR, "题库不存在");

        // 创建关联列表
        List<QuestionBankQuestion> questionBankQuestionList = questionIds.stream()
            .map(questionId -> {
                QuestionBankQuestion questionBankQuestion = new QuestionBankQuestion();
                questionBankQuestion.setQuestionBankId(questionBankId);
                questionBankQuestion.setQuestionId(questionId);
                questionBankQuestion.setQuestionOrder(0); // 默认顺序
                return questionBankQuestion;
            })
            .collect(java.util.stream.Collectors.toList());

        boolean result = questionBankQuestionService.saveBatch(questionBankQuestionList);
        return ResultUtils.success(result);
    }

    /**
     * 获取题库关联的所有题目ID
     *
     * @param questionBankId
     * @return
     */
    @GetMapping("/questionIds")
    public BaseResponse<List<Long>> getQuestionIdsByBankId(@RequestParam long questionBankId) {
        if (questionBankId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 检查题库是否存在且已通过审核
        QuestionBank questionBank = questionBankService.getById(questionBankId);
        ThrowUtils.throwIf(questionBank == null, ErrorCode.NOT_FOUND_ERROR);
        ThrowUtils.throwIf(questionBank.getReviewStatus() != 1, ErrorCode.FORBIDDEN_ERROR, "题库未通过审核");

        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<QuestionBankQuestion> queryWrapper =
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        queryWrapper.eq("questionBankId", questionBankId);
        queryWrapper.orderByAsc("questionOrder");

        List<QuestionBankQuestion> questionBankQuestionList = questionBankQuestionService.list(queryWrapper);

        List<Long> questionIds = questionBankQuestionList.stream()
            .map(QuestionBankQuestion::getQuestionId)
            .collect(java.util.stream.Collectors.toList());

        return ResultUtils.success(questionIds);
    }
}
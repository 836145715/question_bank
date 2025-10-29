package com.hu.wink.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hu.wink.annotation.AuthCheck;
import com.hu.wink.common.BaseResponse;
import com.hu.wink.common.DeleteRequest;
import com.hu.wink.common.ErrorCode;
import com.hu.wink.common.ResultUtils;
import com.hu.wink.constant.UserConstant;
import com.hu.wink.exception.BusinessException;
import com.hu.wink.exception.ThrowUtils;
import com.hu.wink.model.dto.QuestionBankAddRequest;
import com.hu.wink.model.dto.QuestionBankEditRequest;
import com.hu.wink.model.dto.QuestionBankQueryRequest;
import com.hu.wink.model.dto.QuestionBankUpdateRequest;
import com.hu.wink.model.entity.Question;
import com.hu.wink.model.entity.QuestionBank;
import com.hu.wink.model.entity.QuestionBankQuestion;
import com.hu.wink.model.vo.QuestionBankVO;
import com.hu.wink.model.vo.QuestionVO;
import com.hu.wink.service.QuestionBankQuestionService;
import com.hu.wink.service.QuestionBankService;
import com.hu.wink.service.QuestionService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;

import org.springframework.validation.annotation.Validated;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

/**
 * 题库接口
 *
 *
 */

@RestController
@RequestMapping("/questionBank")
@Slf4j
@Validated
public class QuestionBankController {

    @Resource
    private QuestionBankService questionBankService;

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionBankQuestionService questionBankQuestionService;

    // region 管理员功能

    /**
     * 创建题库（管理员）
     *
     * @param questionBankAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "创建题库", description = "管理员创建新的题库")
    public BaseResponse<Long> addQuestionBank(@Valid @RequestBody QuestionBankAddRequest questionBankAddRequest, HttpServletRequest request) {
        long result = questionBankService.addQuestionBank(questionBankAddRequest, request);
        return ResultUtils.success(result);
    }

    /**
     * 更新题库（管理员）
     *
     * @param questionBankUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "更新题库", description = "管理员更新题库信息")
    public BaseResponse<Boolean> updateQuestionBank(@Valid @RequestBody QuestionBankUpdateRequest questionBankUpdateRequest, HttpServletRequest request) {
        boolean result = questionBankService.updateQuestionBank(questionBankUpdateRequest, request);
        return ResultUtils.success(result);
    }

    /**
     * 审核题库（管理员）
     *
     * @param questionBankEditRequest
     * @param request
     * @return
     */
    @PostMapping("/review")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "审核题库", description = "管理员审核题库，设置审核状态")
    public BaseResponse<Boolean> reviewQuestionBank(@Valid @RequestBody QuestionBankEditRequest questionBankEditRequest, HttpServletRequest request) {
        boolean result = questionBankService.reviewQuestionBank(questionBankEditRequest, request);
        return ResultUtils.success(result);
    }

    /**
     * 删除题库（管理员）
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "删除题库", description = "管理员删除题库（逻辑删除）")
    public BaseResponse<Boolean> deleteQuestionBank(@Valid @RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        boolean result = questionBankService.deleteQuestionBank(deleteRequest.getId(), request);
        return ResultUtils.success(result);
    }

    /**
     * 根据ID获取题库（管理员）
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "获取题库详情", description = "根据ID获取题库详情（管理员）")
    public BaseResponse<QuestionBank> getQuestionBankById(@RequestParam @Min(value = 1, message = "ID必须大于0") long id) {
        QuestionBank questionBank = questionBankService.getQuestionBankById(id);
        return ResultUtils.success(questionBank);
    }

    /**
     * 分页获取题库列表（管理员）
     *
     * @param questionBankQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @Operation(summary = "分页获取题库列表", description = "分页获取题库列表（管理员）")
    public BaseResponse<Page<QuestionBank>> listQuestionBankByPage(@Valid @RequestBody QuestionBankQueryRequest questionBankQueryRequest) {
        Page<QuestionBank> questionBankPage = questionBankService.listQuestionBankByPage(questionBankQueryRequest);
        return ResultUtils.success(questionBankPage);
    }

    // region 用户功能

    /**
     * 分页获取题库列表（用户）
     *
     * @param questionBankQueryRequest
     * @return
     */
    @PostMapping("/list/page/vo")
    @Operation(summary = "分页获取题库列表", description = "分页获取题库列表（用户视图）")
    public BaseResponse<Page<QuestionBankVO>> listQuestionBankVOByPage(@Valid @RequestBody QuestionBankQueryRequest questionBankQueryRequest) {
        Page<QuestionBankVO> questionBankVOPage = questionBankService.listQuestionBankVOByPage(questionBankQueryRequest);
        return ResultUtils.success(questionBankVOPage);
    }

    /**
     * 根据ID获取题库详情（用户）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    @Operation(summary = "获取题库详情", description = "根据ID获取题库详情（用户视图）")
    public BaseResponse<QuestionBankVO> getQuestionBankVOById(@RequestParam @Min(value = 1, message = "ID必须大于0") long id) {
        QuestionBankVO questionBankVO = questionBankService.getQuestionBankVOById(id);
        return ResultUtils.success(questionBankVO);
    }

    /**
     * 获取题库下的题目列表
     *
     * @param questionBankId
     * @param current
     * @param pageSize
     * @return
     */
    @GetMapping("/questions")
    @Operation(summary = "获取题库题目列表", description = "获取指定题库下的题目列表（已审核）")
    public BaseResponse<List<QuestionVO>> getQuestionsByQuestionBankId(
            @RequestParam @Min(value = 1, message = "题库ID必须大于0") long questionBankId,
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long pageSize) {

        // 检查题库是否存在且已通过审核
        QuestionBank questionBank = questionBankService.getById(questionBankId);
        ThrowUtils.throwIf(questionBank == null, ErrorCode.NOT_FOUND_ERROR);
        ThrowUtils.throwIf(questionBank.getReviewStatus() != 1, ErrorCode.FORBIDDEN_ERROR, "题库未通过审核");

        // 查询题库题目关联
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<QuestionBankQuestion> queryWrapper =
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        queryWrapper.eq("questionBankId", questionBankId);
        queryWrapper.orderByAsc("questionOrder");

        List<QuestionBankQuestion> questionBankQuestionList = questionBankQuestionService.list(queryWrapper);

        // 获取题目列表
        List<Long> questionIds = questionBankQuestionList.stream()
            .map(QuestionBankQuestion::getQuestionId)
            .collect(java.util.stream.Collectors.toList());

        if (questionIds.isEmpty()) {
            return ResultUtils.success(java.util.Collections.emptyList());
        }

        // 查询题目（只查询已通过审核的）
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Question> questionQueryWrapper =
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        questionQueryWrapper.in("id", questionIds);
        questionQueryWrapper.eq("reviewStatus", 1); // 只显示已通过审核的题目

        // 按照关联表的顺序重新排序
        List<Question> questionList = questionService.list(questionQueryWrapper);
        java.util.Map<Long, Question> questionMap = questionList.stream()
            .collect(java.util.stream.Collectors.toMap(Question::getId, q -> q));

        List<Question> orderedQuestionList = questionIds.stream()
            .map(questionMap::get)
            .filter(java.util.Objects::nonNull)
            .collect(java.util.stream.Collectors.toList());

        // 转换为VO
        List<QuestionVO> questionVOList = questionService.getQuestionVO(orderedQuestionList);

        return ResultUtils.success(questionVOList);
    }
}
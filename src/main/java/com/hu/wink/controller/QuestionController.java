package com.hu.wink.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hu.wink.annotation.AuthCheck;
import com.hu.wink.common.BaseResponse;
import com.hu.wink.common.DeleteRequest;
import com.hu.wink.common.ResultUtils;
import com.hu.wink.constant.UserConstant;
import com.hu.wink.model.dto.QuestionAddRequest;
import com.hu.wink.model.dto.QuestionEditRequest;
import com.hu.wink.model.dto.QuestionQueryRequest;
import com.hu.wink.model.dto.QuestionUpdateRequest;
import com.hu.wink.model.entity.Question;
import com.hu.wink.model.vo.QuestionVO;
import com.hu.wink.service.QuestionService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;

/**
 * 题目接口
 *
 *
 */

@RestController
@RequestMapping("/question")
@Slf4j
@Validated
public class QuestionController {

    @Resource
    private QuestionService questionService;

    // region 管理员功能

    /**
     * 创建题目（管理员）
     *
     * @param questionAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "创建题目", description = "管理员创建新的题目")
    public BaseResponse<Long> addQuestion(@Valid @RequestBody QuestionAddRequest questionAddRequest, HttpServletRequest request) {
        long result = questionService.addQuestion(questionAddRequest, request);
        return ResultUtils.success(result);
    }

    /**
     * 更新题目（管理员）
     *
     * @param questionUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "更新题目", description = "管理员更新题目信息")
    public BaseResponse<Boolean> updateQuestion(@Valid @RequestBody QuestionUpdateRequest questionUpdateRequest, HttpServletRequest request) {
        boolean result = questionService.updateQuestion(questionUpdateRequest, request);
        return ResultUtils.success(result);
    }

    /**
     * 审核题目（管理员）
     *
     * @param questionEditRequest
     * @param request
     * @return
     */
    @PostMapping("/review")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "审核题目", description = "管理员审核题目，设置审核状态")
    public BaseResponse<Boolean> reviewQuestion(@Valid @RequestBody QuestionEditRequest questionEditRequest, HttpServletRequest request) {
        boolean result = questionService.reviewQuestion(questionEditRequest, request);
        return ResultUtils.success(result);
    }

    /**
     * 删除题目（管理员）
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "删除题目", description = "管理员删除题目（逻辑删除）")
    public BaseResponse<Boolean> deleteQuestion(@Valid @RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        boolean result = questionService.deleteQuestion(deleteRequest.getId(), request);
        return ResultUtils.success(result);
    }

    /**
     * 根据ID获取题目
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "获取题目详情", description = "根据ID获取题目详情（管理员）")
    public BaseResponse<Question> getQuestionById(@RequestParam @Min(value = 1, message = "ID必须大于0") long id) {
        Question question = questionService.getQuestionById(id);
        return ResultUtils.success(question);
    }

    /**
     * 分页获取题目列表
     *
     * @param questionQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "分页获取题目列表", description = "分页获取题目列表")
    public BaseResponse<Page<Question>> listQuestionByPage(@Valid @RequestBody QuestionQueryRequest questionQueryRequest) {
        Page<Question> questionPage = questionService.listQuestionByPage(questionQueryRequest);
        return ResultUtils.success(questionPage);
    }

    // region 用户功能

    /**
     * 分页获取题目列表（用户）
     *
     * @param questionQueryRequest
     * @return
     */
    @PostMapping("/list/page/vo")
    @Operation(summary = "分页获取题目列表", description = "分页获取题目列表（用户视图）")
    public BaseResponse<Page<QuestionVO>> listQuestionVOByPage(@Valid @RequestBody QuestionQueryRequest questionQueryRequest) {
        Page<QuestionVO> questionVOPage = questionService.listQuestionVOByPage(questionQueryRequest);
        return ResultUtils.success(questionVOPage);
    }

    /**
     * 根据ID获取题目详情（用户）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    @Operation(summary = "获取题目详情", description = "根据ID获取题目详情（用户视图）")
    public BaseResponse<QuestionVO> getQuestionVOById(@RequestParam @Min(value = 1, message = "ID必须大于0") long id) {
        QuestionVO questionVO = questionService.getQuestionVOById(id);
        return ResultUtils.success(questionVO);
    }

}
package com.hu.wink.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hu.wink.model.dto.QuestionAddRequest;
import com.hu.wink.model.dto.QuestionEditRequest;
import com.hu.wink.model.dto.QuestionQueryRequest;
import com.hu.wink.model.dto.QuestionUpdateRequest;
import com.hu.wink.model.entity.Question;
import com.hu.wink.model.vo.QuestionBankVO;
import com.hu.wink.model.vo.QuestionVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Min;

import java.util.List;

/**
* @author hu
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2025-10-24 21:22:40
*/
public interface QuestionService extends IService<Question> {

    /**
     * 创建题目
     *
     * @param questionAddRequest 题目添加请求
     * @param request HTTP请求
     * @return 题目ID
     */
    long addQuestion(QuestionAddRequest questionAddRequest, HttpServletRequest request);

    /**
     * 更新题目
     *
     * @param questionUpdateRequest 题目更新请求
     * @param request HTTP请求
     * @return 是否成功
     */
    boolean updateQuestion(QuestionUpdateRequest questionUpdateRequest, HttpServletRequest request);

    /**
     * 审核题目
     *
     * @param questionEditRequest 题目审核请求
     * @param request HTTP请求
     * @return 是否成功
     */
    boolean reviewQuestion(QuestionEditRequest questionEditRequest, HttpServletRequest request);

    /**
     * 删除题目
     *
     * @param id 题目ID
     * @param request HTTP请求
     * @return 是否成功
     */
    boolean deleteQuestion(long id, HttpServletRequest request);

    /**
     * 根据ID获取题目（管理员）
     *
     * @param id 题目ID
     * @return 题目信息
     */
    Question getQuestionById(long id);

    /**
     * 根据ID获取题目（用户视图）
     *
     * @param id 题目ID
     * @return 题目视图信息
     */
    QuestionVO getQuestionVOById(long id);

    /**
     * 分页获取题目列表（管理员）
     *
     * @param questionQueryRequest 查询请求
     * @return 分页结果
     */
    Page<Question> listQuestionByPage(QuestionQueryRequest questionQueryRequest);

    /**
     * 分页获取题目列表（用户）
     *
     * @param questionQueryRequest 查询请求
     * @return 分页结果
     */
    Page<QuestionVO> listQuestionVOByPage(QuestionQueryRequest questionQueryRequest);

    /**
     * 获取脱敏的题目信息
     *
     * @param question 题目实体
     * @return 题目视图
     */
    QuestionVO getQuestionVO(Question question);

    /**
     * 获取脱敏的题目信息列表
     *
     * @param questionList 题目实体列表
     * @return 题目视图列表
     */
    List<QuestionVO> getQuestionVO(List<Question> questionList);

    /**
     * 获取查询条件
     *
     * @param questionQueryRequest 查询请求
     * @return 查询包装器
     */
    QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);


    List<QuestionBankVO> getQuestionBanksByQuestionId(@Min(value = 1, message = "题目ID必须大于0") long questionId);
}

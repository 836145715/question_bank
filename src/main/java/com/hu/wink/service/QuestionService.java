package com.hu.wink.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


import com.hu.wink.model.dto.QuestionAddRequest;
import com.hu.wink.model.dto.QuestionEditRequest;
import com.hu.wink.model.dto.QuestionQueryRequest;
import com.hu.wink.model.dto.QuestionUpdateRequest;
import com.hu.wink.model.entity.Question;
import com.hu.wink.model.vo.QuestionVO;
import com.baomidou.mybatisplus.extension.service.IService;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author hu
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2025-10-24 21:22:40
*/
public interface QuestionService extends IService<Question> {

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
     * 创建题目
     *
     * @param questionAddRequest 题目添加请求
     * @param request 请求
     * @return 题目ID
     */
    long addQuestion(QuestionAddRequest questionAddRequest, HttpServletRequest request);

    /**
     * 更新题目
     *
     * @param questionUpdateRequest 题目更新请求
     * @param request 请求
     * @return 是否成功
     */
    boolean updateQuestion(QuestionUpdateRequest questionUpdateRequest, HttpServletRequest request);

    /**
     * 审核题目
     *
     * @param questionEditRequest 题目编辑请求
     * @param request 请求
     * @return 是否成功
     */
    boolean reviewQuestion(QuestionEditRequest questionEditRequest, HttpServletRequest request);

    /**
     * 删除题目
     *
     * @param id 题目ID
     * @param request 请求
     * @return 是否成功
     */
    boolean deleteQuestion(long id, HttpServletRequest request);

    /**
     * 根据ID获取题目
     *
     * @param id 题目ID
     * @return 题目实体
     */
    Question getQuestionById(long id);

    /**
     * 分页获取题目列表
     *
     * @param questionQueryRequest 查询请求
     * @return 题目分页
     */
    Page<Question> listQuestionByPage(QuestionQueryRequest questionQueryRequest);

    /**
     * 分页获取题目VO列表
     *
     * @param questionQueryRequest 查询请求
     * @return 题目VO分页
     */
    Page<QuestionVO> listQuestionVOByPage(QuestionQueryRequest questionQueryRequest);

    /**
     * 根据ID获取题目VO
     *
     * @param id 题目ID
     * @return 题目VO
     */
    QuestionVO getQuestionVOById(long id);

    /**
     * 点赞题目
     *
     * @param id 题目ID
     * @param request 请求
     * @return 是否成功
     */
    boolean thumbQuestion(long id, HttpServletRequest request);

    /**
     * 收藏题目
     *
     * @param id 题目ID
     * @param request 请求
     * @return 是否成功
     */
    boolean favourQuestion(long id, HttpServletRequest request);
}

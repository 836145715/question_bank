package com.hu.wink.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hu.wink.model.dto.QuestionBankAddRequest;
import com.hu.wink.model.dto.QuestionBankEditRequest;
import com.hu.wink.model.dto.QuestionBankQueryRequest;
import com.hu.wink.model.dto.QuestionBankUpdateRequest;
import com.hu.wink.model.entity.QuestionBank;
import com.hu.wink.model.vo.QuestionBankVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author hu
* @description 针对表【question_bank(题库)】的数据库操作Service
* @createDate 2025-10-24 21:21:41
*/
public interface QuestionBankService extends IService<QuestionBank> {

    /**
     * 创建题库
     *
     * @param questionBankAddRequest 题库添加请求
     * @param request HTTP请求
     * @return 题库ID
     */
    long addQuestionBank(QuestionBankAddRequest questionBankAddRequest, HttpServletRequest request);

    /**
     * 更新题库
     *
     * @param questionBankUpdateRequest 题库更新请求
     * @param request HTTP请求
     * @return 是否成功
     */
    boolean updateQuestionBank(QuestionBankUpdateRequest questionBankUpdateRequest, HttpServletRequest request);

    /**
     * 审核题库
     *
     * @param questionBankEditRequest 题库审核请求
     * @param request HTTP请求
     * @return 是否成功
     */
    boolean reviewQuestionBank(QuestionBankEditRequest questionBankEditRequest, HttpServletRequest request);

    /**
     * 删除题库
     *
     * @param id 题库ID
     * @param request HTTP请求
     * @return 是否成功
     */
    boolean deleteQuestionBank(long id, HttpServletRequest request);

    /**
     * 根据ID获取题库（管理员）
     *
     * @param id 题库ID
     * @return 题库信息
     */
    QuestionBank getQuestionBankById(long id);

    /**
     * 根据ID获取题库（用户视图）
     *
     * @param id 题库ID
     * @return 题库视图信息
     */
    QuestionBankVO getQuestionBankVOById(long id);

    /**
     * 分页获取题库列表（管理员）
     *
     * @param questionBankQueryRequest 查询请求
     * @return 分页结果
     */
    Page<QuestionBank> listQuestionBankByPage(QuestionBankQueryRequest questionBankQueryRequest);

    /**
     * 分页获取题库列表（用户）
     *
     * @param questionBankQueryRequest 查询请求
     * @return 分页结果
     */
    Page<QuestionBankVO> listQuestionBankVOByPage(QuestionBankQueryRequest questionBankQueryRequest);

    /**
     * 获取脱敏的题库信息
     *
     * @param questionBank 题库实体
     * @return 题库视图
     */
    QuestionBankVO getQuestionBankVO(QuestionBank questionBank);

    /**
     * 获取脱敏的题库信息列表
     *
     * @param questionBankList 题库实体列表
     * @return 题库视图列表
     */
    List<QuestionBankVO> getQuestionBankVO(List<QuestionBank> questionBankList);

    /**
     * 获取查询条件
     *
     * @param questionBankQueryRequest 查询请求
     * @return 查询包装器
     */
    QueryWrapper<QuestionBank> getQueryWrapper(QuestionBankQueryRequest questionBankQueryRequest);

}

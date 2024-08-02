package com.dongsoj.dongsojbackendquestionservice.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dongsoj.dongsojbackendmodel.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.dongsoj.dongsojbackendmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.dongsoj.dongsojbackendmodel.model.entity.QuestionSubmit;
import com.dongsoj.dongsojbackendmodel.model.entity.User;
import com.dongsoj.dongsojbackendmodel.model.vo.QuestionSubmitVO;

/**
* @author Administrator
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2023-10-22 17:54:24
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {


    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 题目提交（内部服务）
     *
     * @param userId
     * @param postId
     * @return
     */
    int doQuestionSubmitInner(long userId, long postId);

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);


    /**
     * 获取题目封装
     *
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取题目封装
     *
     * @param questionSubmitPage
     * @param loginUser
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);
}

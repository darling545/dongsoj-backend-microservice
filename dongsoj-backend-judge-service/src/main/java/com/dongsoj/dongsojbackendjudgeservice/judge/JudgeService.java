package com.dongsoj.dongsojbackendjudgeservice.judge;


import com.dongsoj.dongsojbackendmodel.model.entity.QuestionSubmit;

/**
 * 判题服务接口
 */
public interface JudgeService {

    /**
     * 判题
     * @param questionSubmitId 提交的题目id
     * @return 暂时返回脱敏题目提交信息
     */
    QuestionSubmit doJudge(long questionSubmitId);
}

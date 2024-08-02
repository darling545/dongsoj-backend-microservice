package com.dongsoj.dongsojbackendjudgeservice.judge.strategy;


import com.dongsoj.dongsojbackendmodel.model.codesandbox.JudgeInfo;

/**
 * 判题策略
 * @author dongs
 */
public interface JudgeStrategy {

    /**
     * 执行判题策略
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext);
}

package com.dongsoj.dongsojbackendserviceclient.service;


import com.dongsoj.dongsojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 判题服务接口
 */
@FeignClient(name = "dongsoj-backend-judge-service",path = "/api/judge/inner")
public interface JudgeFeignClient {

    /**
     * 判题
     * @param questionSubmitId 提交的题目id
     * @return 暂时返回脱敏题目提交信息
     */
    @PostMapping("/do")
    QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId);
}

package com.dongsoj.dongsojbackendjudgeservice.controller.inner;

import com.dongsoj.dongsojbackendjudgeservice.judge.JudgeService;
import com.dongsoj.dongsojbackendmodel.model.entity.QuestionSubmit;
import com.dongsoj.dongsojbackendserviceclient.service.JudgeFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController()
@RequestMapping("/inner")
public class JudgeInnerController implements JudgeFeignClient {

    @Resource
    private JudgeService judgeService;

    /**
     * 判题
     * @param questionSubmitId 提交的题目id
     * @return 暂时返回脱敏题目提交信息
     */
    @Override
    @PostMapping("/do")
    public QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId){
        return judgeService.doJudge(questionSubmitId);
    }
}

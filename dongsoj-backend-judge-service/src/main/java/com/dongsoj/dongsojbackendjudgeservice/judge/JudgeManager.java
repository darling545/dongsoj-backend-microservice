package com.dongsoj.dongsojbackendjudgeservice.judge;


import com.dongsoj.dongsojbackendjudgeservice.judge.strategy.DefaultJudgeStrategy;
import com.dongsoj.dongsojbackendjudgeservice.judge.strategy.JavaLanguageJudgeStrategy;
import com.dongsoj.dongsojbackendjudgeservice.judge.strategy.JudgeContext;
import com.dongsoj.dongsojbackendjudgeservice.judge.strategy.JudgeStrategy;
import com.dongsoj.dongsojbackendmodel.model.codesandbox.JudgeInfo;
import com.dongsoj.dongsojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

@Service
public class JudgeManager {

    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext){
        System.out.println("我在这里执行了判题操作");
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)){
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}

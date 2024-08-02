package com.dongsoj.dongsojbackendjudgeservice.judge.strategy;


import com.dongsoj.dongsojbackendmodel.model.codesandbox.JudgeInfo;
import com.dongsoj.dongsojbackendmodel.model.dto.question.JudgeCase;
import com.dongsoj.dongsojbackendmodel.model.entity.Question;
import com.dongsoj.dongsojbackendmodel.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * 判题上下文类（用于在策略中传递的参数）
 */
@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCase;

    private Question question;

    private QuestionSubmit questionSubmit;

}

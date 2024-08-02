package com.dongsoj.dongsojbackendjudgeservice.judge;

import cn.hutool.json.JSONUtil;

import com.dongsoj.dongsojbackendcommon.common.ErrorCode;
import com.dongsoj.dongsojbackendcommon.exception.BusinessException;
import com.dongsoj.dongsojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.dongsoj.dongsojbackendjudgeservice.judge.codesandbox.CodeSandboxFactory;
import com.dongsoj.dongsojbackendjudgeservice.judge.codesandbox.CodeSandboxProxy;
import com.dongsoj.dongsojbackendjudgeservice.judge.strategy.JudgeContext;
import com.dongsoj.dongsojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.dongsoj.dongsojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import com.dongsoj.dongsojbackendmodel.model.codesandbox.JudgeInfo;
import com.dongsoj.dongsojbackendmodel.model.dto.question.JudgeCase;
import com.dongsoj.dongsojbackendmodel.model.entity.Question;
import com.dongsoj.dongsojbackendmodel.model.entity.QuestionSubmit;
import com.dongsoj.dongsojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.dongsoj.dongsojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {

    @Value("${codesandbox.type:example}")
    private String type;

    @Resource
    private QuestionFeignClient questionFeignClient;



    @Resource
    private JudgeManager judgeManager;


    /**
     * @param questionSubmitId 提交的题目id
     * @return
     */
    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        // 根据id获取提交信息（代码，语言）
        QuestionSubmit questionSubmit = questionFeignClient.getQuestionSubmitById(questionSubmitId);
        if (questionSubmit == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"提交信息不存在");
        }
        // 获取题库中的题目信息
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionFeignClient.getQuestionById(questionId);
        if (question == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目不存在");
        }
        // 判断判题的状态，如果不是等待中，就不需要重复执行
        if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"题目正在判题中");
        }
        // 更改判题状态，设置等待中为"判题中"
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean update = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!update){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目状态更新失败");
        }
        // 调用代码沙箱，获取执行结果
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        codeSandbox = new CodeSandboxProxy(codeSandbox);
        String code = questionSubmit.getCode();
        String language = questionSubmit.getLanguage();
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseStrList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaseStrList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        List<String> outputList = executeCodeResponse.getOutputList();
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setJudgeCase(judgeCaseStrList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);
        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        //出错设置状态为错误
        String message = judgeInfo.getMessage();
        if (!"Accepted".equals(message)){
            questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.FAILED.getValue());
        }else {
            Integer acceptedNum = question.getAcceptedNum();
            Integer submitNum = question.getSubmitNum();
            //通过数+1
            acceptedNum=acceptedNum+1;
            question.setAcceptedNum(acceptedNum);
            //总数+1
            submitNum=submitNum+1;
            question.setSubmitNum(submitNum);
            questionFeignClient.updateQuestion(question);
            questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        }
//        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        update = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!update){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目状态更新错误");
        }
        QuestionSubmit questionSubmitResult = questionFeignClient.getQuestionSubmitById(questionId);
        return questionSubmitResult;
    }
}

package com.dongsoj.dongsojbackendjudgeservice.judge.codesandbox.impl;


import com.dongsoj.dongsojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.dongsoj.dongsojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.dongsoj.dongsojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import com.dongsoj.dongsojbackendmodel.model.codesandbox.JudgeInfo;
import com.dongsoj.dongsojbackendmodel.model.enums.JudgeInfoMessageEnum;
import com.dongsoj.dongsojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 实例代码沙箱
 */
@Slf4j
public class ExampleCodeSandbox implements CodeSandbox {
    /**
     * @param executeCodeRequest 执行代码的请求类
     * @return 返回响应类
     */
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        System.out.println("示例代码沙箱");
        return executeCodeResponse;
    }
}

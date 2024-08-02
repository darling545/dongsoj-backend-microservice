package com.dongsoj.dongsojbackendjudgeservice.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.dongsoj.dongsojbackendmodel.model.codesandbox.JudgeInfo;
import com.dongsoj.dongsojbackendmodel.model.dto.question.JudgeCase;
import com.dongsoj.dongsojbackendmodel.model.dto.question.JudgeConfig;
import com.dongsoj.dongsojbackendmodel.model.entity.Question;
import com.dongsoj.dongsojbackendmodel.model.enums.JudgeInfoMessageEnum;


import java.util.List;


/**
 * 默认判题策略
 */
public class DefaultJudgeStrategy implements JudgeStrategy{
    /**
     * @param judgeContext
     * @return
     */
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        Long memory = judgeInfo.getMemory();
        Long time = judgeInfo.getTime();
        // 两者分别代表预期和实际输出 todo 待分辨
        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();
        //
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCase();
        Question question = judgeContext.getQuestion();
        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
        JudgeInfo judgeInfoResponse = new JudgeInfo();
        judgeInfoResponse.setMemory(memory);
        judgeInfoResponse.setTime(time);
        // 首先判断代码沙箱执行结果的输出用例个数是否和预期的输出用例个数相同
        if (outputList.size() != inputList.size()){
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        // 判断输出的用例和测试输出用例是否相同
        for (int i = 0; i < judgeCaseList.size(); i++){
            JudgeCase judgeCase = judgeCaseList.get(i);
            if (!judgeCase.getOutput().equals(outputList.get(i))){
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
                return judgeInfoResponse;
            }
        }
        // 判断判题限制
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr,JudgeConfig.class);
        Long needMemoryLimit = judgeConfig.getMemoryLimit();
        Long needTimeLimit = judgeConfig.getTimeLimit();
        if (memory > needMemoryLimit){
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        if (time > needTimeLimit){
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
        return judgeInfoResponse;
    }
}

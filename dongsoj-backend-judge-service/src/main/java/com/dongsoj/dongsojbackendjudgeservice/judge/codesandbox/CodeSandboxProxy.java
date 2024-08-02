package com.dongsoj.dongsojbackendjudgeservice.judge.codesandbox;


import com.dongsoj.dongsojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.dongsoj.dongsojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 代码沙箱代理类
 */
@Slf4j
public class CodeSandboxProxy implements CodeSandbox{

    //这个代码沙箱的接口只会被改变一次
    private final CodeSandbox codeSandbox;

    public CodeSandboxProxy(CodeSandbox codeSandbox) {
        this.codeSandbox = codeSandbox;
    }

    /**
     * 被代理的方法
     * @param executeCodeRequest 执行代码的请求类
     * @return 返回正常的被代理的返回对象
     */
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("代码沙箱请求信息:" + executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        log.info("代码沙箱响应信息:" + executeCodeResponse.toString());
        return executeCodeResponse;
    }
}

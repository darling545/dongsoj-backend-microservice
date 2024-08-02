package com.dongsoj.dongsojbackendjudgeservice.judge.codesandbox.impl;


import com.dongsoj.dongsojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.dongsoj.dongsojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.dongsoj.dongsojbackendmodel.model.codesandbox.ExecuteCodeResponse;

/**
 * 第三方代码沙箱（调用第三方【其他人】开发的接口）
 */
public class ThirdPartyCodeSandbox implements CodeSandbox {
    /**
     * @param executeCodeRequest 执行代码的请求类
     * @return 返回执行代码的响应值
     */
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}

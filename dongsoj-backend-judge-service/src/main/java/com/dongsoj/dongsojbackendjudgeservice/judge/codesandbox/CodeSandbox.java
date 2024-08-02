package com.dongsoj.dongsojbackendjudgeservice.judge.codesandbox;


import com.dongsoj.dongsojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.dongsoj.dongsojbackendmodel.model.codesandbox.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
 * @author dongs
 */
public interface CodeSandbox {
   /**
    * 执行代码
    * @param executeCodeRequest 执行代码的请求类
    * @return 返回响应类
    */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}

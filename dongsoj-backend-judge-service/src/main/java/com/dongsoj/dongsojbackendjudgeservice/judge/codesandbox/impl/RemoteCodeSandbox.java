package com.dongsoj.dongsojbackendjudgeservice.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

import com.dongsoj.dongsojbackendcommon.common.ErrorCode;
import com.dongsoj.dongsojbackendcommon.exception.BusinessException;
import com.dongsoj.dongsojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.dongsoj.dongsojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.dongsoj.dongsojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import org.apache.commons.lang3.StringUtils;

/**
 * 远程代码沙箱（实际调用远程接口的沙箱）
 */
public class RemoteCodeSandbox implements CodeSandbox {

    // 定义鉴权请求头和密钥
    private static final String AUTH_REQUEST_HEADER = "auth";

    private static final String AUTH_REQUEST_SECRET = "secretKey";


    /**
     * @param executeCodeRequest 执行代码的请求类
     * @return 返回执行代码响应值
     */
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        // 创建请求，使用hutool工具类，发送Http请求，完成请求的发送，连接到代码沙箱的接口
        String url = "http://localhost:8109/executeCode";
        String json = JSONUtil.toJsonStr(executeCodeRequest);
        String responseStr = HttpUtil.createPost(url)
                .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET)
                .body(json)
                .execute()
                .body();
        if (StringUtils.isBlank(responseStr)){
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR,
                    "executeCode remoteSandbox error, message = " + responseStr);
        }
        return JSONUtil.toBean(responseStr, ExecuteCodeResponse.class);

//        return null;
    }
}

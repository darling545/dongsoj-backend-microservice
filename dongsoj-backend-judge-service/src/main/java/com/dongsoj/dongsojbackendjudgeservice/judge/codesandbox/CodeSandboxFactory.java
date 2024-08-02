package com.dongsoj.dongsojbackendjudgeservice.judge.codesandbox;


import com.dongsoj.dongsojbackendjudgeservice.judge.codesandbox.impl.ExampleCodeSandbox;
import com.dongsoj.dongsojbackendjudgeservice.judge.codesandbox.impl.RemoteCodeSandbox;
import com.dongsoj.dongsojbackendjudgeservice.judge.codesandbox.impl.ThirdPartyCodeSandbox;

/**
 * 工厂模式（通过传递参数的形式选择创建的对象）
 */
public class CodeSandboxFactory {

    /**
     * 创建代码沙箱示例
     * @param type 代码沙箱的类型
     * @return 返回选择的代码沙箱
     */
    public static CodeSandbox newInstance(String type){
        switch (type){
            case "example" :
                return new ExampleCodeSandbox();
            case "remote" :
                return new RemoteCodeSandbox();
            case "thirdParty":
                return new ThirdPartyCodeSandbox();
            default:
                return new ExampleCodeSandbox();
        }
    }

}

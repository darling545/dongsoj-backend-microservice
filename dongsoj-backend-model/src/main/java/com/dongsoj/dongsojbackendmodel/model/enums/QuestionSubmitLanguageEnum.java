package com.dongsoj.dongsojbackendmodel.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum QuestionSubmitLanguageEnum {

    JAVA("java","java"),
    CPLUSPLUS("cpp","cpp"),
    GOLANG("golang","golang");

    private final String text;

    private final String value;

    QuestionSubmitLanguageEnum(String text,String value){
        this.text = text;
        this.value = value;
    }

    /**
     *  获取值列表
     * @return 返回枚举值属性
     */
    public static List<String> getValues(){
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据枚举值获取枚举
     * @param value 枚举值
     * @return 对应的枚举
     */
    public static QuestionSubmitLanguageEnum getEnumByValue(String value){
        if (ObjectUtils.isEmpty(value)){
            return null;
        }

        for (QuestionSubmitLanguageEnum anEnum : QuestionSubmitLanguageEnum.values()) {
            if (anEnum.value.equals(value)){
                return anEnum;
            }
        }

        return null;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }
}

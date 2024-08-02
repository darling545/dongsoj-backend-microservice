package com.dongsoj.dongsojbackendserviceclient.service;


import com.dongsoj.dongsojbackendmodel.model.entity.Question;
import com.dongsoj.dongsojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


/**
* @author Administrator
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2023-10-22 17:49:28
*/
@FeignClient(name = "dongsoj-backend-question-service",path = "/api/question/inner")
public interface QuestionFeignClient {

    @GetMapping("/get/id")
    Question getQuestionById(@RequestParam("questionId") long questionId);

    @GetMapping("/question_submit/get/id")
    QuestionSubmit getQuestionSubmitById(@RequestParam("questionId") long questionSubmitId);

    @PostMapping("/question_submit/update")
    boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit);


    @PostMapping("/question/save")
    boolean updateQuestion(@RequestBody Question question);

}

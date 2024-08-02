package com.dongsoj.dongsojbackendquestionservice.controller.inner;

import com.dongsoj.dongsojbackendmodel.model.entity.Question;
import com.dongsoj.dongsojbackendmodel.model.entity.QuestionSubmit;
import com.dongsoj.dongsojbackendquestionservice.service.QuestionService;
import com.dongsoj.dongsojbackendquestionservice.service.QuestionSubmitService;
import com.dongsoj.dongsojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController()
@RequestMapping("/inner")
public class QuestionInnerController implements QuestionFeignClient {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @GetMapping("/get/id")
    @Override
    public Question getQuestionById(@RequestParam("questionId") long questionId){
        return questionService.getById(questionId);
    }

    @GetMapping("/question_submit/get/id")
    @Override
    public QuestionSubmit getQuestionSubmitById(@RequestParam("questionId") long questionSubmitId){
        return questionSubmitService.getById(questionSubmitId);
    }

    @PostMapping("/question_submit/update")
    @Override
    public boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit){
        return questionSubmitService.updateById(questionSubmit);
    }

    @PostMapping("/question/save")
    @Override
    public boolean updateQuestion(@RequestBody Question question) {
        return questionService.updateById(question);
    }
}

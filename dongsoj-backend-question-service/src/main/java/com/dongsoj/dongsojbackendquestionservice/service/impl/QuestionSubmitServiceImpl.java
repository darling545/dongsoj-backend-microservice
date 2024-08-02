package com.dongsoj.dongsojbackendquestionservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dongsoj.dongsojbackendcommon.common.ErrorCode;
import com.dongsoj.dongsojbackendcommon.constant.CommonConstant;
import com.dongsoj.dongsojbackendcommon.exception.BusinessException;
import com.dongsoj.dongsojbackendcommon.utils.SqlUtils;
import com.dongsoj.dongsojbackendmodel.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.dongsoj.dongsojbackendmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.dongsoj.dongsojbackendmodel.model.entity.Question;
import com.dongsoj.dongsojbackendmodel.model.entity.QuestionSubmit;
import com.dongsoj.dongsojbackendmodel.model.entity.User;
import com.dongsoj.dongsojbackendmodel.model.enums.QuestionSubmitLanguageEnum;
import com.dongsoj.dongsojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.dongsoj.dongsojbackendmodel.model.vo.QuestionSubmitVO;
import com.dongsoj.dongsojbackendquestionservice.mapper.QuestionSubmitMapper;
import com.dongsoj.dongsojbackendquestionservice.rabbitmq.MyMessageProducer;
import com.dongsoj.dongsojbackendquestionservice.service.QuestionService;
import com.dongsoj.dongsojbackendquestionservice.service.QuestionSubmitService;
import com.dongsoj.dongsojbackendserviceclient.service.JudgeFeignClient;
import com.dongsoj.dongsojbackendserviceclient.service.UserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
* @author Administrator
* @description 针对表【question_submit(题目提交)】的数据库操作Service实现
* @createDate 2023-10-22 17:54:23
*/
@Service
@Slf4j
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService {

    @Resource
    private QuestionService questionService;

    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    @Lazy
    private JudgeFeignClient judgeFeignClient;

    @Resource
    private MyMessageProducer myMessageProducer;


    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest 题目提交信息
     * @param loginUser
     * @return
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (languageEnum == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"编程语言错误");
        }
        Long questionId = questionSubmitAddRequest.getQuestionId();
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已提交
        long userId = loginUser.getId();
        // 每个用户串行提交
        // 锁必须要包裹住事务方法
//        QuestionSubmitService questionSubmitService = (QuestionSubmitService) AopContext.currentProxy();
//        synchronized (String.valueOf(userId).intern()) {
//            return questionSubmitService.doQuestionSubmitInner(userId, questionId);
//        }
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setLanguage(language);
        // 设置初始状态
        // todo 使用枚举值进行代替僵硬的的确定值 √
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo("{}");
        boolean save = this.save(questionSubmit);
        if (!save){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"数据插入失败");
        }
        Long questionSubmitId = questionSubmit.getId();
        myMessageProducer.sendMessage("code_exchange","my_routingKey",String.valueOf(questionSubmitId));
//        CompletableFuture.runAsync(() -> {
//            judgeFeignClient.doJudge(questionSubmitId);
//        });
        Integer submitNum = question.getSubmitNum();
        submitNum=submitNum+1;
        question.setSubmitNum(submitNum);
        questionService.updateById(question);
        return questionSubmitId;
    }

    /**
     * 封装了事务的方法
     *
     * @param userId
     * @param questionId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int doQuestionSubmitInner(long userId, long questionId) {
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        QueryWrapper<QuestionSubmit> thumbQueryWrapper = new QueryWrapper<>(questionSubmit);
        QuestionSubmit oldQuestionSubmit = this.getOne(thumbQueryWrapper);
        boolean result;
        // 已点赞
        if (oldQuestionSubmit != null) {
            result = this.remove(thumbQueryWrapper);
            if (result) {
                // 点赞数 - 1
                result = questionService.update()
                        .eq("id", questionId)
                        .gt("thumbNum", 0)
                        .setSql("thumbNum = thumbNum - 1")
                        .update();
                return result ? -1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        } else {
            // 未点赞
            result = this.save(questionSubmit);
            if (result) {
                // 点赞数 + 1
                result = questionService.update()
                        .eq("id", questionId)
                        .setSql("thumbNum = thumbNum + 1")
                        .update();
                return result ? 1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
    }

    /**
     * 获得查询包装类(通过用户根据哪些字段进行查询，根据前端传来的请求对象，得到Mybatis支持的QueryWrapper对象)
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null){
            return queryWrapper;
        }
        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();

        queryWrapper.eq(StringUtils.isNotBlank(language),"language",language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId),"userId",userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId),"questionId",questionId);
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(status) != null,"status",status);
        queryWrapper.eq("isDelete",false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),sortOrder.equals(CommonConstant.SORT_ORDER_ASC),sortField);
        return queryWrapper;
    }

    /**
     * 获取单条信息
     * @param questionSubmit
     * @param loginUser 登录用户
     * @return
     */
    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        long userId = loginUser.getId();
        if (userId != questionSubmit.getUserId() && !userFeignClient.isAdmin(loginUser)){
            questionSubmitVO.setCode(null);
        }
        return questionSubmitVO;
    }

    /**
     * @param questionSubmitPage
     * @param loginUser 登录用户
     * @return
     */
    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage,
                                                    User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(),
                questionSubmitPage.getSize(),questionSubmitPage.getTotal());
        if (CollectionUtils.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }
        List<QuestionSubmitVO> questionSubmitVOList =
                questionSubmitList.stream().map(questionSubmit -> getQuestionSubmitVO(questionSubmit,loginUser))
                        .collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }


}





package com.dongsoj.dongsojbackendjudgeservice.rabbitmq;

import com.dongsoj.dongsojbackendcommon.common.ErrorCode;
import com.dongsoj.dongsojbackendcommon.exception.BusinessException;
import com.dongsoj.dongsojbackendjudgeservice.judge.JudgeService;
import com.dongsoj.dongsojbackendmodel.model.entity.Question;
import com.dongsoj.dongsojbackendmodel.model.entity.QuestionSubmit;
import com.dongsoj.dongsojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.dongsoj.dongsojbackendserviceclient.service.QuestionFeignClient;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class MyMessageConsumer {

    @Resource
    private JudgeService judgeService;

    @Resource
    private QuestionFeignClient questionFeignClient;

    // 指定程序监听的消息队列和确认机制
    @SneakyThrows
    @RabbitListener(queues = {"code_queue"}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        log.info("receiveMessage message = {}", message);
        long questionSubmitId = Long.parseLong(message);
//        if (message == null){
//            channel.basicNack(deliveryTag, false, false);
//            throw new BusinessException(ErrorCode.PARAMS_ERROR,"消息为空");
//        }
        try {
            judgeService.doJudge(questionSubmitId);
//            QuestionSubmit questionSubmit = questionFeignClient.getQuestionSubmitById(questionSubmitId);
//            if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.SUCCEED.getValue())){
//                channel.basicNack(deliveryTag,false,false);
//                throw new BusinessException(ErrorCode.OPERATION_ERROR,"判题失败");
//            }
//            log.info("新提交的信息:" + questionSubmit);
//            // 设置通过数
//            Long questionId = questionSubmit.getQuestionId();
//            log.info("题目id号:" + questionId);
//            Question question = questionFeignClient.getQuestionById(questionId);
//            Integer acceptedNum = question.getAcceptedNum();
//            Question updateQuestion = new Question();
//            synchronized (question.getAcceptedNum()){
//                acceptedNum = acceptedNum + 1;
//                updateQuestion.setId(questionId);
//                updateQuestion.setAcceptedNum(acceptedNum);
//                boolean save = questionFeignClient.updateQuestion(updateQuestion);
//                if (!save){
//                    throw new BusinessException(ErrorCode.OPERATION_ERROR,"更新题目失败");
//                }
//            }
            channel.basicAck(deliveryTag,false);
//            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            channel.basicNack(deliveryTag, false, false);
//            throw new RuntimeException(e);
        }
    }

}

package com.dongsoj.dongsojbackenduserservice.controller.inner;

import com.dongsoj.dongsojbackendmodel.model.entity.User;
import com.dongsoj.dongsojbackendserviceclient.service.UserFeignClient;
import com.dongsoj.dongsojbackenduserservice.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 该服务仅内部调用，不是给前端调用的
 * @author dongs
 */
@RestController()
@RequestMapping("/inner")
public class UserInnerController implements UserFeignClient {

    @Resource
    private UserService userService;

    /**
     * 按照id搜索用户
     * @param userId
     * @return
     */
    @Override
    @GetMapping("/get/id")
    public User getById(@RequestParam("userId") long userId){
        return userService.getById(userId);
    }

    /**
     * 按照id列表搜索用户
     * @param idList
     * @return
     */
    @Override
    @GetMapping("/get/ids")
    public List<User> listByIds(@RequestParam("idList") Collection<Long> idList){
        return userService.listByIds(idList);
    }
}

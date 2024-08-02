package com.dongsoj.dongsojbackendserviceclient.service;


import com.dongsoj.dongsojbackendcommon.common.ErrorCode;
import com.dongsoj.dongsojbackendcommon.exception.BusinessException;

import com.dongsoj.dongsojbackendmodel.model.entity.User;
import com.dongsoj.dongsojbackendmodel.model.enums.UserRoleEnum;
import com.dongsoj.dongsojbackendmodel.model.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

import static com.dongsoj.dongsojbackendcommon.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户服务
 *
 * @author DongS
 *
 */
@FeignClient(value = "dongsoj-backend-user-service", path = "/api/user/inner")
public interface UserFeignClient {


    /**
     * 按照id搜索用户
     * @param userId
     * @return
     */
    @GetMapping("/get/id")
    User getById(@RequestParam("userId") long userId);

    /**
     * 按照id列表搜索用户
     * @param idList
     * @return
     */
    @GetMapping("/get/ids")
    List<User> listByIds(@RequestParam("idList") Collection<Long> idList);
//    /**
//     * 用户登录（微信开放平台）
//     *
//     * @param wxOAuth2UserInfo 从微信获取的用户信息
//     * @param request
//     * @return 脱敏后的用户信息
//     */
//    LoginUserVO userLoginByMpOpen(WxOAuth2UserInfo wxOAuth2UserInfo, HttpServletRequest request);

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    default User getLoginUser(HttpServletRequest request){
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 可以考虑在这里做全局权限校验
        return currentUser;
    }


    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    default boolean isAdmin(User user){
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }



    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */
    default UserVO getUserVO(User user){
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }



}

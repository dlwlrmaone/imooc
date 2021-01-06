package com.imooc.service;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;

/**
 * 用户相关接口
 * @author dlwlrmaone
 */
public interface UserService {

    /**
     * 判断用户名是否存在
     * @param username
     * @return
     */
    boolean queryUsernameIsExist(String username);

    /**
     * 用户注册
     * @param userBO
     * @return
     */
    Users createUser(UserBO userBO);

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    Users UsersLogin(String username,String password);

}

package com.imooc.service.impl;

import com.imooc.enumclass.SexEnum;
import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;
import com.imooc.service.UserService;
import com.imooc.utils.DateUtil;
import com.imooc.utils.MD5Utils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

/**
 * 用户登录相关实现
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private Sid sid;

    private static final String USER_FACE_URL = "img/iu_0001.jpeg";

    /**
     * 用户名校验
     * @param username
     * @return
     */
    //SUPPORTS事务类型常用于查询
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {

        //创建查询实例对象
        Example userExample = new Example(Users.class);
        //构建查询条件
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andEqualTo("username",username);

        Users users = usersMapper.selectOneByExample(userExample);
        //减少if/else的使用
        return users == null ? false : true;
    }

    /**
     * 用户注册
     * @param userBO
     * @return
     */
    //REQUIRED事务类型常用于创建
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users createUser(UserBO userBO) {

        String userId = sid.nextShort();

        Users users = new Users();
        users.setId(userId);
        users.setUsername(userBO.getUsername());
        try {
            users.setPassword(MD5Utils.getMD5Str(userBO.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //默认用户昵称同用户名
        users.setNickname(userBO.getUsername());
        //默认头像
        users.setFace(USER_FACE_URL);
        //默认生日
        users.setBirthday(DateUtil.stringToDate("1900-01-01"));
        //默认性别为保密，使用枚举类来表示
        users.setSex(SexEnum.secret.type);
        users.setCreatedTime(new Date());
        users.setUpdatedTime(new Date());

        usersMapper.insert(users);

        return users;
    }

    /**
     * 用户登陆
     * @param username
     * @param password
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users userLogin(String username, String password) {

        //创建查询实例对象
        Example userExample = new Example(Users.class);
        //构建查询条件
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andEqualTo("username",username);
        userCriteria.andEqualTo("password",password);

        Users user = usersMapper.selectOneByExample(userExample);
        return user;
    }
}

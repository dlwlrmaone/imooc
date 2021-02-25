package com.imooc.service.impl.center;

import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;
import com.imooc.service.center.CenterUserService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 用户中心展示相关实现
 */
@Service
public class CenterUserServiceImpl implements CenterUserService {

    @Autowired
    public UsersMapper usersMapper;

    @Autowired
    public Sid sid;

    /**
     * 根据用户ID查询用户信息
     * @param userId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users getUserInfo(String userId) {

        Users centerUser = usersMapper.selectByPrimaryKey(userId);
        //不将密码传到前端
        centerUser.setPassword(null);
        return centerUser;
    }

    /**
     * 用户中心-用户信息修改
     * @param userId
     * @param centerUserBO
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users updateUserInfo(String userId, CenterUserBO centerUserBO) {

        Users updateUser = new Users();
        BeanUtils.copyProperties(centerUserBO,updateUser);
        updateUser.setId(userId);
        updateUser.setUpdatedTime(new Date());

        usersMapper.updateByPrimaryKeySelective(updateUser);
        return getUserInfo(userId);
    }
}

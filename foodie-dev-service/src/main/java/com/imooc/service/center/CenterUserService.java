package com.imooc.service.center;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;

/**
 * center用户中心
 */
public interface CenterUserService {

    /**
     * 根据用户ID查询用户信息
     * @param userId
     * @return
     */
    Users getUserInfo(String userId);

    /**
     * 用户中心-用户信息修改
     * @param userId
     * @param centerUserBO
     */
    Users updateUserInfo(String userId, CenterUserBO centerUserBO);

    /**
     * 用户中心-用户头像更新
     * @param userId
     * @param faceUrl
     * @return
     */
    Users updateUserFace(String userId,String faceUrl);
}

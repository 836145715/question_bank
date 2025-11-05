package com.hu.wink.constant;


/**
 * Author: ZhuMingX
 * CreateTime: 2025-11-05
 * Version: 1.0
 */

public interface RedisConstant {

    String USER_SIGN_IN_KEY = "user:signin";

    /**
     * 获取用户签到记录key
     * @param year
     * @param userId
     * @return
     */
    static String getUserSignInKey(int year,long userId){
        return USER_SIGN_IN_KEY + ":" + year + ":" + userId;
    }

}

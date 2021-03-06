package service;

import entity.TPmUser;

/**
 * @author AIERXUAN
 */
public interface TPmUserService {
    /**
     * 根据eid(OpenId)获取用户pm_user_id
     * @param openId 云之家
     * @return 用户String
     */
    String selectUser(String openId);

    /**
     * 获取组织信息
     * @return 组织信息
     */
    String selectOrgUrl();
}

package com.geek.login.service;

import com.geek.model.user.pojos.ApUser;

/**
 * 对称加密  DES  AES。
 * 散列算法 MD5。加盐 salt。
 */
public interface IValidateService {

    /**
     * DES 验证。
     *
     * @param user
     * @param db
     * @return
     */
    boolean validateDES(ApUser user, ApUser db);

    /**
     * MD5 验证。
     *
     * @param user
     * @param db
     * @return
     */
    boolean validateMD5(ApUser user, ApUser db);

    /**
     * MD5 加盐验证。
     *
     * @param user
     * @param db
     * @return
     */
    boolean validateWithSalt(ApUser user, ApUser db);

}

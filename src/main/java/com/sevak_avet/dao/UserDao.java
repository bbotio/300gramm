package com.sevak_avet.dao;

import com.sevak_avet.domain.User;

import java.util.List;

/**
 * Created by Sevak Avetisyan
 * Date: 5/11/15
 * Time: 5:59 PM
 */
public interface UserDao {
    User getUserInfo(String username);

    void save(User info);

    void update(User newInfo);

    List<User> getAllUsersInfo();
}

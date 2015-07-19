package com.sevak_avet.dao;

import com.sevak_avet.domain.Blacklist;
import com.sevak_avet.domain.User;

import java.util.List;
import java.util.Set;

/**
 * Created by Avetisyan Sevak
 * Date: 18.07.2015
 * Time: 19:47
 */
public interface BlacklistDao extends AbstractDao<Blacklist> {
    Blacklist getBlacklist(String username);
    Blacklist getBlacklist(User user);

    Set<String> getBlackListSet(String username);
    Set<String> getBlackListSet(User user);

    boolean isEmpty(String username);
    boolean isEmpty(User user);
}

package com.sevak_avet.dao;

import com.sevak_avet.domain.AntiSpam;
import com.sevak_avet.domain.User;

import java.util.Set;

/**
 * Created by savetisyan on 05.07.15.
 */
public interface AntiSpamDao extends AbstractDao<AntiSpam> {
    AntiSpam getAntiSpam(User user);

    AntiSpam getAntiSpam(String username);

    Set<String> getBadWords(User user);

    Boolean isAntiSpamEnabled(User user);
}

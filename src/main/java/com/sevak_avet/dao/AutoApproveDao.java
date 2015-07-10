package com.sevak_avet.dao;

import com.sevak_avet.domain.AutoApprove;
import com.sevak_avet.domain.User;

import java.time.LocalTime;

/**
 * Created by Avetisyan Sevak
 * Date: 15.05.2015
 * Time: 11:05
 */
public interface AutoApproveDao extends AbstractDao<AutoApprove> {
    AutoApprove getAutoApprove(User user);

    AutoApprove getAutoApprove(String username);

    Integer getUserPeriod(User user);

    Boolean isAutoApproveEnabled(User user);
}

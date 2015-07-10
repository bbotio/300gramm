package com.sevak_avet.dao;

import com.sevak_avet.domain.User;

/**
 * Created by savetisyan on 05.07.15.
 */
public interface AbstractDao<T> {
    void save(T obj);

    void update(T obj);
}

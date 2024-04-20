package com.overthinker.cloud.service;

import com.overthinker.cloud.entities.Pay;

import java.util.List;

public interface Payservice {
    int add(Pay pay);
    int delete(Integer id);
    int update(Pay pay);
    Pay getById(Integer id);
    List<Pay> getAll();
}

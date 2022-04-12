package com.imooc.flashsale.service;

import com.imooc.flashsale.dao.TestDataDao;
import com.imooc.flashsale.domain.TestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestDataService {

    @Autowired private TestDataDao testDataDao;

    public TestData getById(int id) {
        return testDataDao.getById(id);
    }
}

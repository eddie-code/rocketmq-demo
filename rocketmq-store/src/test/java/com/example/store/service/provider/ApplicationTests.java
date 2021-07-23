package com.example.store.service.provider;

import com.example.store.mapping.StoreMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author eddie.lee
 * @ProjectName rocketmq-demo
 * @Package com.example.store.service.provider
 * @ClassName ApplicationTests
 * @description
 * @date created in 2021-07-22 22:02
 * @modified by
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Autowired
    private StoreMapper storeMapper;

    @Test
    public void testSelectVersion() {
        int version = storeMapper.selectVersion("1", "001");
        System.out.println("version: " + version);
    }

    @Test
    public void testSelectStoreCount() {
        int storeCount = storeMapper.selectStoreCount("1", "001");
        System.out.println("storeCount: " + storeCount);
    }

}

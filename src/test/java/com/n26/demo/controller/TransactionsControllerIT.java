package com.n26.demo.controller;

import com.n26.demo.DemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionsControllerIT {

    @Test
    public void testPostTransaction() {

    }

    @Test
    public void testDeleteTransaction() {

    }
}

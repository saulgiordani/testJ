package com.n26.demo.controller;

import com.n26.demo.DemoApplication;
import com.n26.demo.Helper.CommonHelper;
import com.n26.demo.categories.IntegrationTests;
import com.n26.demo.entity.TransactionDto;
import com.n26.demo.mock.MockData;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StatisticsControllerIT {

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    @Category(IntegrationTests.class)
    public void testGetStatistics() throws JSONException {
        String uri = "http://localhost:" + port + "/statistics";
        HttpEntity<String> entity = new HttpEntity<String>(null, null);
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        String expected = "{'sum':104.2, 'avg':11.58, 'max':20.76, 'min':10.24, 'count':9}";
        JSONAssert.assertEquals(expected, response.getBody(), true);
    }

    @Before
    public void mockTransactionsData() {
        MockData.transactions = CommonHelper.initMockData();
    }
}

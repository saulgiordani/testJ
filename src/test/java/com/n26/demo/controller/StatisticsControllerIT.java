package com.n26.demo.controller;

import com.n26.demo.DemoApplication;
import com.n26.demo.entity.TransactionDto;
import com.n26.demo.mock.MockData;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.util.List;

import static com.n26.demo.mock.MockData.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StatisticsControllerIT {

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void testGetStatistics() throws JSONException {
        String uri = "http://localhost:" + port + "/transactions";
        HttpEntity<String> entity = new HttpEntity<String>(null, null);
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        String expected = "{\"amount\": \"12.3343\",\"timestamp\": \"2018-07-17T09:59:51.312Z\"}";
        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Before
    public void mockTransactionsData() throws ParseException {
        transactions.add(new TransactionDto("10.299", "2018-07-17T09:59:51.312Z"));
        transactions.add(new TransactionDto("10.299", "2018-07-17T09:59:51.312Z"));
        transactions.add(new TransactionDto("10.299", "2018-07-17T09:59:51.312Z"));
        transactions.add(new TransactionDto("10.299", "2018-07-17T09:59:51.312Z"));
        transactions.add(new TransactionDto("10.299", "2018-07-17T09:59:51.312Z"));
        transactions.add(new TransactionDto("10.299", "2018-07-17T09:59:51.312Z"));
        transactions.add(new TransactionDto("10.299", "2018-07-17T09:59:51.312Z"));
        transactions.add(new TransactionDto("10.299", "2018-07-17T09:59:51.312Z"));
        transactions.add(new TransactionDto("10.299", "2018-07-17T09:59:51.312Z"));
        transactions.add(new TransactionDto("10.299", "2018-07-17T09:59:51.312Z"));
        transactions.add(new TransactionDto("10.299", "2018-07-17T09:59:51.312Z"));
    }
}

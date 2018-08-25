package com.n26.demo.controller;

import com.n26.demo.DemoApplication;
import com.n26.demo.categories.IntegrationTests;
import com.n26.demo.mock.MockData;
import org.json.JSONException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionsControllerIT {

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    @Category(IntegrationTests.class)
    public void testPostTransactionCreated() throws JSONException {
        String amount = "12.3343";
        String timestamp = generateValidTimestamp();

        String uri = "http://localhost:" + port + "/transactions";
        HttpEntity<String> entity =
                new HttpEntity<String>("{'amount': '" + amount + "', 'timestamp': '" + timestamp + "'}",
                        null);
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @Category(IntegrationTests.class)
    public void testPostTransactionNoContent() throws JSONException {
        String amount = "12.3343";
        String timestamp = generateOldTimestampOlder();

        String uri = "http://localhost:" + port + "/transactions";
        HttpEntity<String> entity =
                new HttpEntity<String>("{'amount': '" + amount + "', 'timestamp': '" + timestamp + "'}",
                        null);
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @Category(IntegrationTests.class)
    public void testPostTransactionUnProcessable() throws JSONException {
        String amount = "12.3343";
        String timestamp = generateFutureTimestamp();

        String uri = "http://localhost:" + port + "/transactions";
        HttpEntity<String> entity =
                new HttpEntity<String>("{'amount': '" + amount + "', 'timestamp': '" + timestamp + "'}",
                        null);
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    @Category(IntegrationTests.class)
    public void testDeleteTransaction() throws JSONException {
        testPostTransactionCreated();
        assertTrue(MockData.transactions.size() == 1);

        String uri = "http://localhost:" + port + "/transactions";
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.DELETE, null, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(MockData.transactions.size() == 0);
    }

    private String generateValidTimestamp() {
        return ZonedDateTime.now(ZoneId.of("UTC")).minusSeconds(30).toString().replace("[UTC]", "");
    }

    private String generateOldTimestampOlder() {
        return ZonedDateTime.now(ZoneId.of("UTC")).minusSeconds(70).toString().replace("[UTC]", "");
    }

    private String generateFutureTimestamp() {
        return ZonedDateTime.now(ZoneId.of("UTC")).plusSeconds(30).toString().replace("[UTC]", "");
    }
}

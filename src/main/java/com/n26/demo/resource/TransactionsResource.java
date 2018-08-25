package com.n26.demo.resource;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.n26.demo.Helper.CommonHelper;
import com.n26.demo.entity.TransactionDto;
import com.n26.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.*;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/transactions")
public class TransactionsResource {

    @Autowired
    private TransactionService transactionService;

    @RequestMapping(method = RequestMethod.POST, consumes = "text/plain")
    @Scope("request")
    public ResponseEntity postTransaction(@RequestBody String jsonInString) {

        TransactionDto transactionDto = null;
        Instant instant = LocalDateTime.now(ZoneId.of("UTC")).toInstant(OffsetDateTime.now().getOffset());
        Timestamp now = Timestamp.from(instant);

        try {
            // check if json is valid. The following line will cause an exception if the json is not valid
            JsonObject obj = new JsonParser().parse(jsonInString).getAsJsonObject();

            try {
                String amount = obj.get("amount").toString().replaceAll("\"", "");
                String timestamp = obj.get("timestamp").toString().replaceAll("\"", "");
                // the instantiation of the object will throw an exception if some of the values are not parsable
                transactionDto = new TransactionDto(amount, timestamp);

                // check if the time is in the future
                if (CommonHelper.getDateDiff(transactionDto.getTimestamp().getTime(), now.getTime(), TimeUnit.SECONDS) > 0) {
                    return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
                }
            } catch (Exception e) {
                return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } catch (IllegalStateException ex) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        // check if transaction is older than 60 seconds, if not, add it to the list
        try {
            if (CommonHelper.getDateDiff(now.getTime(), transactionDto.getTimestamp().getTime(), TimeUnit.SECONDS) > 60) {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            else {
                transactionService.addTransaction(transactionDto);
                return new ResponseEntity(HttpStatus.CREATED);
            }
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void deleteTransations() {
        transactionService.deleteTransations();
    }
}
